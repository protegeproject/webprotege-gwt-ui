package edu.stanford.bmir.protege.web.client.change.combined;

import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.selection.*;
import edu.stanford.bmir.protege.web.shared.change.GetEntityEarliestChangeTimestampAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.VIEW_CHANGES;
import static edu.stanford.bmir.protege.web.shared.event.UiHistoryChangedEvent.UI_HISTORY_CHANGED;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_CAPABILITIES_CHANGED;

@Portlet(id = "portlets.CombinedChangesByEntity",
        title = "iCat-X Entity Changes",
        tooltip = "Displays a list of combined project changes for the selected entity.")
public class CombinedEntityChangesPortletPresenter extends AbstractWebProtegePortletPresenter {

    private static final String FORBIDDEN_MESSAGE = "You do not have permission to view changes for this project";

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final CombinedChangeListPresenter presenter;

    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    private UIAction oldHistoryLink;

    private final DispatchServiceManager dispatch;

    private PortletUi portletUi;

    @Inject
    public CombinedEntityChangesPortletPresenter(SelectionModel selectionModel,
                                                 LoggedInUserProjectCapabilityChecker capabilityChecker,
                                                 ProjectId projectId,
                                                 CombinedChangeListPresenter presenter,
                                                 DisplayNameRenderer displayNameRenderer,
                                                 DispatchServiceManager dispatch,
                                                 SelectedPathsModel selectedPathsModel) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.presenter = presenter;
        this.capabilityChecker = capabilityChecker;
        this.dispatch = dispatch;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.portletUi = portletUi;
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
        capabilityChecker.hasCapability(VIEW_CHANGES, canViewChanges -> {
            if (canViewChanges) {
                setForbiddenVisible(false);
                getSelectedEntity().ifPresent(entity -> {
                            presenter.displayChangesForEntity(entity);
                            loadAndShowOldHistoryLink(entity);
                        }
                );
            } else {
                setForbiddenVisible(true);
                presenter.clear();
            }
        });
    }

    private void loadAndShowOldHistoryLink(OWLEntity entity) {
        if (oldHistoryLink != null) {
            portletUi.removeAction(oldHistoryLink);
        }

        dispatch.execute(
                GetEntityEarliestChangeTimestampAction.create(getProjectId(), entity.getIRI()),
                result -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Click here to see prior changes");
                    if (result.getEarliestTimestamp() != null) {
                        long ts = result.getEarliestTimestamp();
                        String dateStr = com.google.gwt.i18n.client.DateTimeFormat
                                .getFormat("dd/MM/yyyy HH:mm")
                                .format(new java.util.Date(ts));
                        sb.append(" to ");
                        sb.append(dateStr);
                    }

                    String url = "https://icat-history.azurewebsites.net/changes/"
                            + com.google.gwt.http.client.URL.encodeQueryString(entity.getIRI().toString())
                            + ".html";

                    oldHistoryLink = new PortletAction(
                             sb.toString(),
                            "wp-btn-g--history",
                            () -> com.google.gwt.user.client.Window.open(url, "_blank", "")
                    );
                    portletUi.addAction(oldHistoryLink);
                }
        );
    }

}
