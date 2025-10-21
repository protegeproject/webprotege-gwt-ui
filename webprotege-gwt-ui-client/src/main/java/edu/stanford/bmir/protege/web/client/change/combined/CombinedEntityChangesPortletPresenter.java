package edu.stanford.bmir.protege.web.client.change.combined;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.app.ApplicationEnvironmentManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.client.selection.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.icd.OldHistoryEncoding;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.VIEW_CHANGES;
import static edu.stanford.bmir.protege.web.shared.event.UiHistoryChangedEvent.UI_HISTORY_CHANGED;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_CAPABILITIES_CHANGED;

@Portlet(id = "portlets.CombinedChangesByEntity",
        title = "iCat-X Entity Changes",
        tooltip = "Displays a list of combined project changes for the selected entity.")
public class CombinedEntityChangesPortletPresenter extends AbstractWebProtegePortletPresenter {

    private static final Logger logger = Logger.getLogger(CombinedEntityChangesPortletPresenter.class.getName());

    private static final String FORBIDDEN_MESSAGE = "You do not have permission to view changes for this project";

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final CombinedChangeListPresenter presenter;

    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    private PortletAction oldHistoryLink;

    private final DispatchServiceManager dispatch;

    private final ActiveProjectManager activeProjectManager;

    private Optional<PortletUi> portletUi;

    private final Messages messages;

    private final ApplicationEnvironmentManager applicationEnvironmentManager;

    @Inject
    public CombinedEntityChangesPortletPresenter(SelectionModel selectionModel,
                                                 LoggedInUserProjectCapabilityChecker capabilityChecker,
                                                 ProjectId projectId,
                                                 CombinedChangeListPresenter presenter,
                                                 DisplayNameRenderer displayNameRenderer,
                                                 DispatchServiceManager dispatch,
                                                 SelectedPathsModel selectedPathsModel,
                                                 ActiveProjectManager activeProjectManager,
                                                 Messages messages,
                                                 ApplicationEnvironmentManager applicationEnvironmentManager) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.activeProjectManager = activeProjectManager;
        this.presenter = presenter;
        this.capabilityChecker = capabilityChecker;
        this.dispatch = dispatch;
        this.messages = messages;
        this.applicationEnvironmentManager = applicationEnvironmentManager;
        activeProjectManager.getActiveProjectDetails(projectDetails -> {
            if(projectDetails.isPresent()) {
                String formatted = formatEpochMillis(projectDetails.get().getCreatedAt());
                oldHistoryLink = new PortletAction(
                        messages.change_priorChanges(formatted),
                        "wp-btn-g--olderHistory",
                        this::handleOldHistoryClickAction);
            } else {
                String dateStrFromEnv = applicationEnvironmentManager.getAppEnvVariables()
                        .getEntityOldHistoryAndNotesDate();
                oldHistoryLink = new PortletAction(
                        messages.change_priorChanges(dateStrFromEnv),
                        "wp-btn-g--olderHistory",
                        this::handleOldHistoryClickAction
                );
            }
        });
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.portletUi = Optional.of(portletUi);
        portletUi.addAction(oldHistoryLink);
        oldHistoryLink.setEnabled(false);
        portletUi.setToolbarVisible(true);
        eventBus.addProjectEventHandler(getProjectId(),
                ProjectChangedEvent.TYPE, this::handleProjectChanged);
        eventBus.addProjectEventHandler(getProjectId(), UI_HISTORY_CHANGED, this::handleHistoryChanged);
        eventBus.addApplicationEventHandler(ON_CAPABILITIES_CHANGED, event -> updateDisplayForSelectedEntity());
        portletUi.setWidget(presenter.getView().asWidget());
        portletUi.setForbiddenMessage(FORBIDDEN_MESSAGE);
        setDisplaySelectedEntityNameAsSubtitle(true);
        updateDisplayForSelectedEntity();
    }

    private void handleHistoryChanged(UiHistoryChangedEvent uiHistoryChangedEvent) {
        for (String entityIri : uiHistoryChangedEvent.getAfectedEntityIris()) {
            if (getSelectionModel().getSelection().isPresent() &&
                    getSelectionModel().getSelection().get().toStringID().equals(entityIri)) {
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
        boolean entitySelected = getSelectedEntity().isPresent();

        oldHistoryLink.setEnabled(entitySelected);
        oldHistoryLink.setVisible(entitySelected);
        capabilityChecker.hasCapability(VIEW_CHANGES, canViewChanges -> {
            if (canViewChanges) {
                setForbiddenVisible(false);
                getSelectedEntity().ifPresent(presenter::displayChangesForEntity
                );
            } else {
                setForbiddenVisible(true);
                presenter.clear();
            }
        });
    }

    private void handleOldHistoryClickAction() {
        boolean entitySelected = getSelectedEntity().isPresent();
        if (entitySelected) {
            OWLEntity entity = getSelectedEntity().get();
            String url = applicationEnvironmentManager.getAppEnvVariables()
                    .getEntityHistoryUrlFormat()
                    .replace("{0}", OldHistoryEncoding.encodeIriToHostedLink(entity.getIRI()));
            Window.open(url, "_blank", "");
        }
    }

    public static String formatEpochMillis(long epochMillis) {
        Date date = new Date(epochMillis); // uses browser's local tz
        String month = DateTimeFormat.getFormat("MMMM").format(date);
        int day = Integer.parseInt(DateTimeFormat.getFormat("d").format(date));
        String year = DateTimeFormat.getFormat("yyyy").format(date);
        return month + " " + day + getSuffix(day) + ", " + year;
    }


    private static String getSuffix(int day) {
        if (day >= 11 && day <= 13) return "th";
        switch (day % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }

}
