package edu.stanford.bmir.protege.web.client.card.logicaldefinition;

import com.google.auto.factory.*;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.*;
import edu.stanford.bmir.protege.web.client.card.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.*;
import edu.stanford.bmir.protege.web.shared.*;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.LogicalConditions;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.webprotege.shared.annotations.Card;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.*;

@Card(id = "logicaldefinition.card")
public class LogicalDefinitionCardPresenter implements CustomContentEntityCardPresenter, EntityCardEditorPresenter {

    private static final Logger logger = Logger.getLogger(LogicalDefinitionCardPresenter.class.getName());


    private final LogicalDefinitionCardView view;

    private final DispatchServiceManager dispatch;

    private Optional<OWLEntity> selectedEntity = Optional.empty();

    private OWLEntity renderedEntity = null;
    private final ProjectId projectId;

    private final HandlerManager handlerManager = new HandlerManager(this);

    private final DisplayContextManager displayContextManager = new DisplayContextManager(context -> {
    });

    private boolean inFocus = false;

    private boolean readOnly = true;

    private ImmutableSet<Capability> capabilities = ImmutableSet.of();

    @Inject
    @AutoFactory
    public LogicalDefinitionCardPresenter(LogicalDefinitionCardView view,
                                          @Provided DispatchServiceManager dispatch,
                                          @Provided ProjectId projectId) {
        this.view = view;
        this.dispatch = dispatch;
        this.projectId = projectId;
    }

    @Override
    public void start(EntityCardUi ui, WebProtegeEventBus eventBus) {
        ui.setWidget(view);
        view.setLogicalDefinitionChangeHandler(() -> this.handlerManager.fireEvent(new DirtyChangedEvent()));
    }

    @Override
    public void requestFocus() {
        if(this.selectedEntity.isPresent() && !this.selectedEntity.get().equals(this.renderedEntity)){
            this.renderedEntity = selectedEntity.get();
            dispatch.execute(GetEntityRenderingAction.create(projectId, renderedEntity),
                    (result) -> view.setEntityData(result.getEntityData()));
            view.setReadOnly(this.readOnly);
            view.setEntity(renderedEntity);
            this.inFocus = true;
        }
    }

    @Override
    public void stop() {
        this.view.dispose();
    }

    @Override
    public void setEntity(OWLEntity entity) {
        logger.log(Level.INFO, "Set logical definitions card entity: " + entity.toStringID());
        this.selectedEntity = Optional.of(entity);
    }

    @Override
    public void clearEntity() {
        logger.log(Level.INFO, "logical definitions card clear entity !");
        view.dispose();
        fireEvent(new DirtyChangedEvent());
        this.renderedEntity = null;
    }

    @Override
    public void beginEditing() {
        if(inFocus) {
            view.switchToEditable();
        }
        this.readOnly = false;
    }

    @Override
    public void cancelEditing() {
        if(isDirty()) {
            view.resetPristineState();
        }
        view.switchToReadOnly();
        this.readOnly = true;
    }

    @Override
    public void finishEditing(String commitMessage) {
        this.readOnly = true;
        if(isDirty()) {
            view.saveValues(commitMessage);
            view.switchToReadOnly();
            fireEvent(new DirtyChangedEvent());
        }
        view.switchToReadOnly();
    }

    @Override
    public boolean isDirty() {
        if (this.view.isReadOnly()) {
            return false;
        }
        Optional<LogicalConditions> currLogicalDefinition = Optional.ofNullable(view.getEditedData());
        Optional<LogicalConditions> pristineLogicalDefinition = Optional.ofNullable(view.getPristineData());
        logger.log(Level.INFO, "Pristine linSpec data: " + pristineLogicalDefinition);
        logger.log(Level.INFO, "Edited linSpec data: " + currLogicalDefinition);
        return !pristineLogicalDefinition.equals(currLogicalDefinition);
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
    }
}
