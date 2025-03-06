package edu.stanford.bmir.protege.web.client.change.combined;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;
import static edu.stanford.bmir.protege.web.shared.event.UiHistoryChangedEvent.UI_HISTORY_CHANGED;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

@Portlet(id = "portlets.CombinedChangesByEntity",
        title = "iCat-X Entity Changes",
        tooltip = "Displays a list of combined project changes for the selected entity.")
public class CombinedEntityChangesPortletPresenter extends AbstractWebProtegePortletPresenter {

    private static final String FORBIDDEN_MESSAGE = "You do not have permission to view changes for this project";

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final CombinedChangeListPresenter presenter;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public CombinedEntityChangesPortletPresenter(SelectionModel selectionModel,
                                                 LoggedInUserProjectPermissionChecker permissionChecker,
                                                 ProjectId projectId,
                                                 CombinedChangeListPresenter presenter,
                                                 DisplayNameRenderer displayNameRenderer,
                                                 DispatchServiceManager dispatch,
                                                 SelectedPathsModel selectedPathsModel) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.presenter = presenter;
        this.permissionChecker = permissionChecker;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        eventBus.addProjectEventHandler(getProjectId(),
                ProjectChangedEvent.TYPE, this::handleProjectChanged);
        eventBus.addProjectEventHandler(getProjectId(), UI_HISTORY_CHANGED, this::handleHistoryChanged);
        eventBus.addApplicationEventHandler(ON_PERMISSIONS_CHANGED, event -> updateDisplayForSelectedEntity());
        portletUi.setWidget(presenter.getView().asWidget());
        portletUi.setForbiddenMessage(FORBIDDEN_MESSAGE);
        setDisplaySelectedEntityNameAsSubtitle(true);
        updateDisplayForSelectedEntity();
    }

    private void handleHistoryChanged(UiHistoryChangedEvent uiHistoryChangedEvent) {
        for (String entityIri : uiHistoryChangedEvent.getAfectedEntityIris()) {
            if (getSelectionModel().getSelection().get().toStringID().equals(entityIri)) {
                updateDisplayForSelectedEntity();
                return;
            }
        }
    }

    private void handleProjectChanged(ProjectChangedEvent event) {
        if (lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        lastRevisionNumber = event.getRevisionNumber();
        for (OWLEntityData entityData : event.getSubjects()) {
            if (getSelectionModel().getSelection().equals(Optional.of(entityData.getEntity()))) {
                updateDisplayForSelectedEntity();
                return;
            }
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        updateDisplayForSelectedEntity();
    }

    @Override
    protected void handleReloadRequest() {
        updateDisplayForSelectedEntity();
    }

    private void updateDisplayForSelectedEntity() {
        permissionChecker.hasPermission(VIEW_CHANGES, canViewChanges -> {
            if (canViewChanges) {
                setForbiddenVisible(false);
                ProjectId projectId = getProjectId();
                getSelectedEntity().ifPresent(presenter::displayChangesForEntity);
            } else {
                setForbiddenVisible(true);
                presenter.clear();
            }
        });
    }

}
