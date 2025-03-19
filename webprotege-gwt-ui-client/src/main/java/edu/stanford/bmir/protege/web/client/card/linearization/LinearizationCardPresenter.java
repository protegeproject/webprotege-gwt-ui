package edu.stanford.bmir.protege.web.client.card.linearization;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.card.CustomContentEntityCardPresenter;
import edu.stanford.bmir.protege.web.client.card.EntityCardEditorPresenter;
import edu.stanford.bmir.protege.web.client.card.EntityCardUi;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyParentsAction;
import edu.stanford.bmir.protege.web.shared.linearization.GetEntityLinearizationAction;
import edu.stanford.bmir.protege.web.shared.linearization.GetLinearizationDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.linearization.WhoficEntityLinearizationSpecification;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Card;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Card(id = "linearization.card")
public class LinearizationCardPresenter implements CustomContentEntityCardPresenter, EntityCardEditorPresenter {

    private static final Logger logger = Logger.getLogger(LinearizationCardPresenter.class.getName());


    private final LinearizationCardView view;

    private Map<String, LinearizationDefinition> definitionMap = new HashMap<>();

    private final Map<String, String> entityParentsMap = new HashMap<>();

    private final DispatchServiceManager dispatch;

    private final ProjectId projectId;

    private Optional<WhoficEntityLinearizationSpecification> pristineLinearizationData = Optional.empty();

    private final HandlerManager handlerManager = new HandlerManager(this);

    @Inject
    @AutoFactory
    public LinearizationCardPresenter(LinearizationCardView view,
                                      @Provided DispatchServiceManager dispatch,
                                      @Provided ProjectId projectId) {
        this.view = view;
        this.dispatch = dispatch;
        this.projectId = projectId;
        this.view.setProjectId(projectId);
    }

    @Override
    public void start(EntityCardUi ui, WebProtegeEventBus eventBus) {
        ui.setWidget(view);
        dispatch.execute(GetLinearizationDefinitionsAction.create(), result -> {
            for (LinearizationDefinition definition : result.getDefinitionList()) {
                this.definitionMap.put(definition.getLinearizationUri(), definition);
            }
            view.setLinearizationDefinitonMap(this.definitionMap);
        });
    }

    @Override
    public void requestFocus() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setEntity(OWLEntity entity) {

        if (entity != null) {
            this.entityParentsMap.clear();
            dispatch.execute(GetEntityLinearizationAction.create(entity.getIRI().toString(), projectId), response -> {

                this.view.dispose();
                if (response.getWhoficEntityLinearizationSpecification() != null &&
                        response.getWhoficEntityLinearizationSpecification().getLinearizationSpecifications() != null) {

                    dispatch.execute(GetHierarchyParentsAction.create(projectId, entity, ClassHierarchyDescriptor.get()), result -> {
                        if (result.getParents() != null) {
                            result.getParents().forEach(parent -> this.entityParentsMap.put(parent.getEntity().toStringID(), parent.getBrowserText()));
                        }
                        view.dispose();
                        view.setEntityParentsMap(this.entityParentsMap);
                        view.setWhoFicEntity(response.getWhoficEntityLinearizationSpecification());
                        view.setLinearizationChangeEventHandler(() -> {
                            handlerManager.fireEvent(new DirtyChangedEvent());
                        });
                        pristineLinearizationData = Optional.ofNullable(view.getLinSpec());
                    });
                }
            });
        }
    }

    @Override
    public void clearEntity() {
        view.dispose();
    }

    @Override
    public void beginEditing() {
        pristineLinearizationData = Optional.ofNullable(view.getLinSpec());
        view.setEditable();
    }

    @Override
    public void cancelEditing() {
        view.setReadOnly();
    }

    @Override
    public void finishEditing(String commitMessage) {
        view.saveValues();
        pristineLinearizationData = Optional.ofNullable(view.getLinSpec());
    }

    @Override
    public boolean isDirty() {
        Optional<WhoficEntityLinearizationSpecification> currSpec = Optional.ofNullable(view.getLinSpec());
        logger.log(Level.INFO, "Pristine linSpec data: " + pristineLinearizationData);
        logger.log(Level.INFO, "Edited linSpec data: " + currSpec);
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
}
