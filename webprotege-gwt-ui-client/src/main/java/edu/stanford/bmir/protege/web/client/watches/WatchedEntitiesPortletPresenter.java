package edu.stanford.bmir.protege.web.client.watches;

import com.google.gwt.user.client.Timer;
import edu.stanford.bmir.protege.web.client.change.ChangeListPresenter;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.WATCH_CHANGES;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_CAPABILITIES_CHANGED;
import static edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent.ON_WATCH_ADDED;
import static edu.stanford.bmir.protege.web.shared.watches.WatchRemovedEvent.ON_WATCH_REMOVED;

@Portlet(id = "portlets.WatchedEntities", title = "Watched Entities")
public class WatchedEntitiesPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final LoggedInUserProvider loggedInUserProvider;

    private final ChangeListPresenter presenter;

    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    private final Timer refreshTimer = new Timer() {
        @Override
        public void run() {
            onRefresh();
        }
    };

    private final ChangeListView changeListView;

    private Optional<PortletUi> ui = Optional.empty();

    @Inject
    public WatchedEntitiesPortletPresenter(ChangeListPresenter presenter,
                                           LoggedInUserProjectCapabilityChecker capabilityChecker,
                                           SelectionModel selectionModel,
                                           @Nonnull SelectedPathsModel selectedPathsModel,
                                           ProjectId projectId,
                                           LoggedInUserProvider loggedInUserProvider,
                                           DisplayNameRenderer displayNameRenderer,
                                           DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.loggedInUserProvider = loggedInUserProvider;
        this.presenter = presenter;
        this.capabilityChecker = capabilityChecker;
        changeListView = presenter.getView();

    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        ui = Optional.of(portletUi);
        portletUi.setWidget(changeListView);
        portletUi.addAction(new PortletAction("Refresh", this::onRefresh));

        eventBus.addProjectEventHandler(getProjectId(),
                                        ON_WATCH_ADDED,
                                        event -> refreshDelayed());
        eventBus.addProjectEventHandler(getProjectId(),
                                        ON_WATCH_REMOVED,
                                        event -> refreshDelayed());
        eventBus.addProjectEventHandler(getProjectId(),
                ON_CAPABILITIES_CHANGED,
                                        event -> onRefresh());
        portletUi.setForbiddenMessage("You do not have permission to watch changes in this project");

        // TODO: Check this

        onRefresh();
    }

    @Override
    protected void handleReloadRequest() {
        onRefresh();
    }

    private void refreshDelayed() {
        refreshTimer.cancel();
        refreshTimer.schedule(2000);
    }

    private void onRefresh() {

        ui.ifPresent(portletUi -> {
            capabilityChecker.hasCapability(WATCH_CHANGES,
                                            canWatch -> {
                                                if (canWatch) {
                                                    presenter.displayChangesForWatches(loggedInUserProvider.getCurrentUserId());
                                                    portletUi.setForbiddenVisible(false);
                                                }
                                                else {
                                                    portletUi.setForbiddenVisible(false);
                                                }
                                            });
        });

    }
}
