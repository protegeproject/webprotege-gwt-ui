package edu.stanford.bmir.protege.web.client.change.combined;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.filter.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.VIEW_CHANGES;
import static edu.stanford.bmir.protege.web.shared.event.UiHistoryChangedEvent.UI_HISTORY_CHANGED;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_CAPABILITIES_CHANGED;

@Portlet(id = "portlets.CombinedProjectHistory",
        title = "iCat-X Project History",
        tooltip = "Displays a list of all changes that have been made to the project")
public class CombinedProjectHistoryPortletPresenter extends AbstractWebProtegePortletPresenter {

    public static final FilterId SHOW_DETAILS_FILTER = new FilterId("Show details");

    private final CombinedChangeListPresenter presenter;

    private final UIAction refreshAction;

    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

    private final FilterView filterView;

    private final Messages messages;

    private final ChangeListView changeListView;

    @Inject
    public CombinedProjectHistoryPortletPresenter(CombinedChangeListPresenter presenter,
                                                  LoggedInUserProjectCapabilityChecker capabilityChecker,
                                                  FilterView filterView,
                                                  SelectionModel selectionModel,
                                                  ProjectId projectId,
                                                  Messages messages,
                                                  DisplayNameRenderer displayNameRenderer,
                                                  DispatchServiceManager dispatch,
                                                  SelectedPathsModel selectedPathsModel) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.presenter = presenter;
        this.capabilityChecker = capabilityChecker;
        this.filterView = filterView;
        this.messages = messages;
        presenter.setDownloadVisible(true);
        changeListView = presenter.getView();


        filterView.addFilter(SHOW_DETAILS_FILTER, FilterSetting.ON);
        filterView.addValueChangeHandler(event -> changeListView.setDetailsVisible(event.getValue()
                .hasSetting(SHOW_DETAILS_FILTER,
                        FilterSetting.ON)));
        refreshAction = new PortletAction(messages.refresh(), "wp-btn-g--refresh", this::reload);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setForbiddenMessage(messages.change_permissionDenied());
        portletUi.setWidget(changeListView.asWidget());
        portletUi.addAction(refreshAction);
        portletUi.setToolbarVisible(true);
        portletUi.setFilterView(filterView);
        presenter.setHasBusy(portletUi);
        eventBus.addProjectEventHandler(getProjectId(), ProjectChangedEvent.TYPE, this::handleProjectChanged);
        eventBus.addApplicationEventHandler(ON_CAPABILITIES_CHANGED, event -> reload());
        eventBus.addProjectEventHandler(getProjectId(), UI_HISTORY_CHANGED, this::handleHistoryChanged);
        reload();
    }

    @Override
    protected void handleReloadRequest() {
        reload();
    }

    private void handleProjectChanged(ProjectChangedEvent event) {
        if (lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        refreshAction.setEnabled(true);
        lastRevisionNumber = event.getRevisionNumber();
    }

    private void handleHistoryChanged(UiHistoryChangedEvent event) {
        refreshAction.setEnabled(true);
    }

    private void reload() {
        refreshAction.setEnabled(false);
        capabilityChecker.hasCapability(VIEW_CHANGES,
                canViewChanges -> {
                    if (canViewChanges) {
                        presenter.setRevertChangesVisible(true);
                        presenter.displayChangesForProject();
                        setForbiddenVisible(false);
                    } else {
                        setForbiddenVisible(true);
                    }
                });

    }
}
