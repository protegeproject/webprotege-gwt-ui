package edu.stanford.bmir.protege.web.client.change;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.VIEW_CHANGES;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_CAPABILITIES_CHANGED;

@Portlet(id = "portlets.ChangesByEntity",
        title = "Entity Changes",
        tooltip = "Displays a list of project changes for the selected entity.")
public class EntityChangesPortletPresenter extends AbstractWebProtegePortletPresenter {

    private static final String FORBIDDEN_MESSAGE = "You do not have permission to view changes for this project";

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final ChangeListPresenter presenter;

    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    @Inject
	public EntityChangesPortletPresenter(SelectionModel selectionModel,
                                         @Nonnull SelectedPathsModel selectedPathsModel,
                                         LoggedInUserProjectCapabilityChecker capabilityChecker,
                                         ProjectId projectId,
                                         ChangeListPresenter presenter,
                                         DisplayNameRenderer displayNameRenderer,
                                         DispatchServiceManager dispatch) {
		super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.presenter = presenter;
        this.capabilityChecker = capabilityChecker;
	}

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        eventBus.addProjectEventHandler(getProjectId(),
                                        ProjectChangedEvent.TYPE, event -> handleProjectChanged(event));
        eventBus.addApplicationEventHandler(ON_CAPABILITIES_CHANGED, event -> updateDisplayForSelectedEntity());
        portletUi.setWidget(presenter.getView().asWidget());
        portletUi.setForbiddenMessage(FORBIDDEN_MESSAGE);
        setDisplaySelectedEntityNameAsSubtitle(true);
        updateDisplayForSelectedEntity();
    }

    private void handleProjectChanged(ProjectChangedEvent event) {
        if(lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        lastRevisionNumber = event.getRevisionNumber();
        for(OWLEntityData entityData : event.getSubjects()) {
            if(getSelectionModel().getSelection().equals(Optional.of(entityData.getEntity()))) {
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
        capabilityChecker.hasCapability(VIEW_CHANGES, canViewChanges -> {
            if (canViewChanges) {
                setForbiddenVisible(false);
                ProjectId projectId = getProjectId();
                getSelectedEntity().ifPresent(presenter::displayChangesForEntity);
            }
            else {
                setForbiddenVisible(true);
                presenter.clear();
            }
        });
	}

}
