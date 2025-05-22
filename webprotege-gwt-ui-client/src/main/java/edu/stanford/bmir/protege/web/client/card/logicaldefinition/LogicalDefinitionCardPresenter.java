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
    private final ProjectId projectId;

    private final HandlerManager handlerManager = new HandlerManager(this);

    private final DisplayContextManager displayContextManager = new DisplayContextManager(context -> {
    });

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

    }

    @Override
    public void stop() {
        this.view.dispose();
    }

    @Override
    public void setEntity(OWLEntity entity) {
        logger.log(Level.INFO, "Set logical definitions card entity: " + entity.toStringID());

        dispatch.execute(GetEntityRenderingAction.create(projectId, entity),
                (result) -> view.setEntityData(result.getEntityData()));
        view.setEntity(entity);
    }

    @Override
    public void clearEntity() {
        logger.log(Level.INFO, "logical definitions card clear entity !");
        view.dispose();
        fireEvent(new DirtyChangedEvent());
    }

    @Override
    public void beginEditing() {
        view.switchToEditable();
    }

    @Override
    public void cancelEditing() {
        view.resetPristineState();
        fireEvent(new DirtyChangedEvent());
    }

    @Override
    public void finishEditing(String commitMessage) {
        view.saveValues(commitMessage);
        fireEvent(new DirtyChangedEvent());
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
