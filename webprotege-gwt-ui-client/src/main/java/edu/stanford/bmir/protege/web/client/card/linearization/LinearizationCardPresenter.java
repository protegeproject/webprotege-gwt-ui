package edu.stanford.bmir.protege.web.client.card.linearization;

import com.google.auto.factory.*;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.*;
import edu.stanford.bmir.protege.web.client.app.NothingSelectedView;
import edu.stanford.bmir.protege.web.client.card.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.linearization.LinearizationCapabilities;
import edu.stanford.bmir.protege.web.client.ui.*;
import edu.stanford.bmir.protege.web.shared.*;
import edu.stanford.bmir.protege.web.shared.access.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyParentsAction;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Card;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.*;

@Card(id = "linearization.card")
public class LinearizationCardPresenter implements CustomContentEntityCardPresenter, EntityCardEditorPresenter {

    private static final Logger logger = Logger.getLogger(LinearizationCardPresenter.class.getName());


    private final LinearizationCardView view;

    private Map<String, LinearizationDefinition> definitionMap = new HashMap<>();

    private final Map<String, String> entityParentsMap = new HashMap<>();

    private final DispatchServiceManager dispatch;

    private final ProjectId projectId;

    private Optional<OWLEntity> selectedEntity = Optional.empty();

    private OWLEntity renderedEntity;

    private Optional<WhoficEntityLinearizationSpecification> pristineLinearizationData = Optional.empty();

    private final HandlerManager handlerManager = new HandlerManager(this);

    private final DisplayContextManager displayContextManager = new DisplayContextManager(context -> {
    });

    private final NothingSelectedView nothingSelectedView;

    private EntityCardUi entityCardUi;


    private ImmutableSet<Capability> capabilities = ImmutableSet.of();



    private boolean canViewResiduals;
    private boolean canEditResiduals;
    private boolean isReadOnly = true;

    @Inject
    @AutoFactory
    public LinearizationCardPresenter(LinearizationCardView view,
                                      @Provided DispatchServiceManager dispatch,
                                      @Provided ProjectId projectId, NothingSelectedView nothingSelectedView) {
        this.view = view;
        this.dispatch = dispatch;
        this.projectId = projectId;
        this.nothingSelectedView = nothingSelectedView;
        this.view.setProjectId(projectId);
    }

    @Override
    public void start(EntityCardUi ui, WebProtegeEventBus eventBus) {
        this.entityCardUi = ui;
        ui.setWidget(view);
    }

    @Override
    public void requestFocus() {
        if (selectedEntity.isPresent() && !selectedEntity.get().equals(this.renderedEntity)) {
            this.renderedEntity = selectedEntity.get();
            displayEntity(renderedEntity);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void setEntity(OWLEntity entity) {
        if (entity != null && projectId != null) {
            this.selectedEntity = Optional.of(entity);
        }
    }

    private void displayEntity(OWLEntity entity) {
        this.entityParentsMap.clear();
        dispatch.execute(GetContextAwareLinearizationDefinitionAction.create(entity.getIRI(), Arrays.asList(LinearizationCapabilities.EDIT_LINEARIZATION_ROW,
                LinearizationCapabilities.VIEW_LINEARIZATION_ROW), projectId), linearizationDefResult -> {
            for (LinearizationDefinition definition : linearizationDefResult.getDefinitionList()) {
                this.definitionMap.put(definition.getLinearizationUri(), definition);
            }
            view.setLinearizationDefinitonMap(this.definitionMap);
            view.setCanEditResiduals(canEditResiduals);
            view.setCanViewResiduals(canViewResiduals);

            dispatch.execute(GetEntityLinearizationAction.create(entity.getIRI().toString(), projectId), response -> {

                this.view.dispose();
                if (response.getWhoficEntityLinearizationSpecification() != null &&
                        response.getWhoficEntityLinearizationSpecification().getLinearizationSpecifications() != null &&
                        !response.getWhoficEntityLinearizationSpecification().getLinearizationSpecifications().isEmpty()) {
                    this.entityCardUi.setWidget(view);
                    dispatch.execute(GetHierarchyParentsAction.create(projectId, entity, ClassHierarchyDescriptor.get()), hierarchyParentsResult -> {
                        if (hierarchyParentsResult.getParents() != null) {
                            hierarchyParentsResult.getParents().forEach(parent -> this.entityParentsMap.put(parent.getEntity().toStringID(), parent.getBrowserText()));
                        }
                        view.setEntityParentsMap(this.entityParentsMap);
                        view.setWhoFicEntity(response.getWhoficEntityLinearizationSpecification());
                        view.setLinearizationChangeEventHandler(() -> handlerManager.fireEvent(new DirtyChangedEvent()));
                        if (!isReadOnly) {
                            view.setEditable();
                        }
                        pristineLinearizationData = Optional.ofNullable(view.getLinSpec());
                        fireEvent(new DirtyChangedEvent());
                    });
                } else {
                    this.entityCardUi.setWidget(nothingSelectedView);
                }
            });
        });
    }

    @Override
    public void clearEntity() {
        this.isReadOnly = true;
        view.dispose();
        fireEvent(new DirtyChangedEvent());
        renderedEntity = null;
    }

    @Override
    public void beginEditing() {
        pristineLinearizationData = Optional.ofNullable(view.getLinSpec());
        this.isReadOnly = false;
        view.setEditable();
    }

    @Override
    public void cancelEditing() {
        this.isReadOnly = true;
        view.setReadOnly();
        fireEvent(new DirtyChangedEvent());
    }

    @Override
    public void finishEditing(String commitMessage) {
        this.isReadOnly = true;
        view.saveValues(commitMessage);
        pristineLinearizationData = Optional.ofNullable(view.getLinSpec());
        fireEvent(new DirtyChangedEvent());
    }

    @Override
    public boolean isDirty() {
        Optional<WhoficEntityLinearizationSpecification> currSpec = Optional.ofNullable(view.getLinSpec());
        logger.log(Level.FINE, "Pristine linSpec data: " + pristineLinearizationData);
        logger.log(Level.FINE, "Edited linSpec data: " + currSpec);
        if (this.view.isReadOnly()) {
            return false;
        }
        //todo this is because sometimes view is not loaded on quick entity changes
        if (currSpec.isPresent() && pristineLinearizationData.isPresent() && !currSpec.get().getEntityIRI().equals(pristineLinearizationData.get().getEntityIRI())) {
            logger.warning("Pristine entity is different from changed entity. Pristine " + pristineLinearizationData.get().getEntityIRI() + " changed : " + currSpec.get().getEntityIRI());
            return false;
        }

        return !pristineLinearizationData.equals(currSpec);
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return handlerManager.addHandler(DirtyChangedEvent.TYPE, handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public boolean isEditor() {
        return true;
    }

    @Override
    public DisplayContextBuilder fillDisplayContextBuilder() {
        return displayContextManager.fillDisplayContextBuilder();
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        displayContextManager.setParentDisplayContextBuilder(parent);
    }

    @Override
    public void setCapabilities(ImmutableSet<Capability> capabilities) {
        this.capabilities = capabilities;
        canEditResiduals = CardCapabilityChecker.hasCapability(ContextAwareBuiltInCapability.EDIT_LINEARIZATION_RESIDUALS.getCapability(), capabilities);
        canViewResiduals = canEditResiduals || CardCapabilityChecker.hasCapability(ContextAwareBuiltInCapability.VIEW_LINEARIZATION_RESIDUALS.getCapability(), capabilities);
    }
}
