package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.PermissionScreener;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.EventPollingManager;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivePresenter;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveSwitcherPresenter;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.tag.ProjectTagsStyleManager;
import edu.stanford.bmir.protege.web.client.topbar.TopBarPresenter;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetUserInfoAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.TranslateEventListAction;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsAction;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.MoreObjects.toStringHelper;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
@ProjectSingleton
public class ProjectPresenter implements HasDispose, HasProjectId {

    private final ProjectId projectId;

    private final ProjectView view;

    private final BusyView busyView;

    private final DispatchServiceManager dispatchServiceManager;

    private final TopBarPresenter topBarPresenter;

    private final PerspectiveSwitcherPresenter linkBarPresenter;

    private final PerspectivePresenter perspectivePresenter;

    private final PermissionScreener permissionScreener;

    private final EventPollingManager eventPollingManager;

    private final WebProtegeEventBus eventBus;

    private final ProjectTagsStyleManager projectTagsStyleManager;

    private final LargeNumberOfChangesManager largeNumberOfChangesHandler;

    private final LoggedInUserProvider loggedInUserProvider;

    private static final Logger logger = Logger.getLogger(ProjectPresenter.class.getName());


    @Inject
    public ProjectPresenter(ProjectId projectId,
                            ProjectView view,
                            BusyView busyView,
                            DispatchServiceManager dispatchServiceManager,
                            EventPollingManager eventPollingManager,
                            TopBarPresenter topBarPresenter,
                            PerspectiveSwitcherPresenter linkBarPresenter,
                            PerspectivePresenter perspectivePresenter,
                            PermissionScreener permissionScreener,
                            WebProtegeEventBus eventBus,
                            ProjectTagsStyleManager projectTagsStyleManager,
                            LargeNumberOfChangesManager largeNumberOfChangesHandler, LoggedInUserProvider loggedInUserProvider) {
        this.projectId = projectId;
        this.view = view;
        this.busyView = busyView;
        this.dispatchServiceManager = dispatchServiceManager;
        this.eventPollingManager = eventPollingManager;
        this.permissionScreener = permissionScreener;
        this.topBarPresenter = topBarPresenter;
        this.linkBarPresenter = linkBarPresenter;
        this.perspectivePresenter = perspectivePresenter;
        this.eventBus = eventBus;
        this.projectTagsStyleManager = projectTagsStyleManager;
        this.largeNumberOfChangesHandler = largeNumberOfChangesHandler;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus,
                      @Nonnull ProjectViewPlace place) {
        logger.log(Level.FINE, "[ProjectPresenter] Starting project presenter " + eventBus.getClass().getName());
        busyView.setMessage("Loading project.  Please wait.");
        container.setWidget(busyView);
        permissionScreener.checkPermission(VIEW_PROJECT.getActionId(),
                                           container,
                                           () -> displayProject(container, eventBus, place));
    }

    private void displayProject(@Nonnull AcceptsOneWidget container,
                                @Nonnull EventBus eventBus,
                                @Nonnull ProjectViewPlace place) {
        dispatchServiceManager.execute(new LoadProjectAction(projectId),
                                       result -> handleProjectLoaded(container, eventBus, place));
        dispatchServiceManager.execute(new GetUserInfoAction(), r -> {
            subscribeToWebsocket(projectId.getId(),  r.getToken(), this.loggedInUserProvider.getCurrentUserId().getUserName());

        });

    }

    private void handleProjectLoaded(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus, @Nonnull ProjectViewPlace place) {
        dispatchServiceManager.beginBatch();
        topBarPresenter.start(view.getTopBarContainer(), eventBus, place);
        linkBarPresenter.start(view.getPerspectiveLinkBarViewContainer(), eventBus, place);
        perspectivePresenter.start(view.getPerspectiveViewContainer(), eventBus, place);
       // eventPollingManager.start();
        eventBus.addHandlerToSource(LargeNumberOfChangesEvent.LARGE_NUMBER_OF_CHANGES,
                                    projectId,
                                    largeNumberOfChangesHandler);
        container.setWidget(view);

        dispatchServiceManager.execute(GetProjectTagsAction.create(projectId),
                                       r -> projectTagsStyleManager.setProjectTags(r.getTags(), view));
        dispatchServiceManager.executeCurrentBatch();
    }

    @Override
    public void dispose() {
        topBarPresenter.dispose();
        linkBarPresenter.dispose();
        perspectivePresenter.dispose();
        eventPollingManager.stop();
    }

    @Override
    public String toString() {
        return toStringHelper("ProjectPresenter")
                .addValue(projectId)
                .toString();
    }

    public void dispatchEventsFromWebsocket(String data) {
        dispatchServiceManager.execute(TranslateEventListAction.create(data), (GetProjectEventsResult result) -> eventPollingManager.dispatchEvents(result.getEvents()));

    }
    /*TODO change the hardcoded broker URL and get it from a config class */
    public native void subscribeToWebsocket(String projectId, String token, String userId)/*-{
        try {
            var that = this;

            var stompClient = new $wnd.StompJs.Client({
                brokerURL: 'ws://webprotege-local.edu/wsapps',
                debug: function(str) {
                    console.log(str);
                },
                reconnectDelay: 30000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
            });


            stompClient.onConnect = function(frame) {
                 var headers = {
                    'token': token,
                    'userId': userId,
                    'Authorization' : 'Bearer ' + token
                  };
                stompClient.subscribe('/topic/project-events/' + projectId, function(message) {
                    that.@edu.stanford.bmir.protege.web.client.project.ProjectPresenter::dispatchEventsFromWebsocket(Ljava/lang/String;)(message.body);

                }, headers);
            };
            stompClient.onWebSocketError = function(error) {
                console.error('Error with websocket', error);
            };
            stompClient.onStompError = function(frame) {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            };


            stompClient.activate();

        } catch (e) {
            $wnd.console.log('An error has occurred in the websocket connection/subscription ' + e)
        }
    }-*/;
}
