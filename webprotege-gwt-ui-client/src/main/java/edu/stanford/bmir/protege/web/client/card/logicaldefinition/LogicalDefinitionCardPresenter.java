package edu.stanford.bmir.protege.web.client.card.logicaldefinition;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.card.CustomContentEntityCardPresenter;
import edu.stanford.bmir.protege.web.client.card.EntityCardEditorPresenter;
import edu.stanford.bmir.protege.web.client.card.EntityCardUi;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.LogicalConditions;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.webprotege.shared.annotations.Card;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Card(id = "logicaldefinition.card")
public class LogicalDefinitionCardPresenter implements CustomContentEntityCardPresenter, EntityCardEditorPresenter {

    private static final Logger logger = Logger.getLogger(LogicalDefinitionCardPresenter.class.getName());


    private final LogicalDefinitionCardView view;

    private final DispatchServiceManager dispatch;
    private final ProjectId projectId;

    private final HandlerManager handlerManager = new HandlerManager(this);


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
        view.setLogicalDefinitionChangeHandler(()->this.handlerManager.fireEvent(new DirtyChangedEvent()));
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
        view.setEntity(view.getEntity());
        fireEvent(new DirtyChangedEvent());
    }

    @Override
    public void finishEditing(String commitMessage) {
        view.saveValues(commitMessage);
        fireEvent(new DirtyChangedEvent());
    }

    @Override
    public boolean isDirty() {
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
}
