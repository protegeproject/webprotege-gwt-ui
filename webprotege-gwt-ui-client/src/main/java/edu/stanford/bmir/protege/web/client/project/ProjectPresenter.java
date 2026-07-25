package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.CapabilityScreener;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.ProjectEventDispatcher;
import edu.stanford.bmir.protege.web.client.events.ProjectEventStreamManager;
import edu.stanford.bmir.protege.web.client.perspective.PerspectivePresenter;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveSwitcherPresenter;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.tag.ProjectTagsStyleManager;
import edu.stanford.bmir.protege.web.client.topbar.TopBarPresenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.LoadProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.GetProjectTagsAction;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.VIEW_PROJECT;

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

    private final CapabilityScreener capabilityScreener;

    private final ProjectEventDispatcher projectEventDispatcher;

    private final ProjectEventStreamManager projectEventStreamManager;

    private final WebProtegeEventBus eventBus;

    private final ProjectTagsStyleManager projectTagsStyleManager;

    private final LargeNumberOfChangesManager largeNumberOfChangesHandler;

    @Inject
    public ProjectPresenter(ProjectId projectId,
                            ProjectView view,
                            BusyView busyView,
                            DispatchServiceManager dispatchServiceManager,
                            ProjectEventDispatcher projectEventDispatcher,
                            ProjectEventStreamManager projectEventStreamManager,
                            TopBarPresenter topBarPresenter,
                            PerspectiveSwitcherPresenter linkBarPresenter,
                            PerspectivePresenter perspectivePresenter,
                            CapabilityScreener capabilityScreener,
                            WebProtegeEventBus eventBus,
                            ProjectTagsStyleManager projectTagsStyleManager,
                            LargeNumberOfChangesManager largeNumberOfChangesHandler) {
        this.projectId = projectId;
        this.view = view;
        this.busyView = busyView;
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectEventDispatcher = projectEventDispatcher;
        this.projectEventStreamManager = projectEventStreamManager;
        this.capabilityScreener = capabilityScreener;
        this.topBarPresenter = topBarPresenter;
        this.linkBarPresenter = linkBarPresenter;
        this.perspectivePresenter = perspectivePresenter;
        this.eventBus = eventBus;
        this.projectTagsStyleManager = projectTagsStyleManager;
        this.largeNumberOfChangesHandler = largeNumberOfChangesHandler;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus,
                      @Nonnull ProjectViewPlace place) {
        GWT.log("[ProjectPresenter] Starting project presenter " + eventBus.getClass().getName());
        busyView.setMessage("Loading project.  Please wait.");
        container.setWidget(busyView);
        capabilityScreener.checkCapability(VIEW_PROJECT.getCapability(),
                                           container,
                                           () -> displayProject(container, eventBus, place));
    }

    private void displayProject(@Nonnull AcceptsOneWidget container,
                                @Nonnull EventBus eventBus,
                                @Nonnull ProjectViewPlace place) {
        dispatchServiceManager.execute(new LoadProjectAction(projectId),
                                       result -> handleProjectLoaded(container, eventBus, place));
        // Open the live event stream. The stream manager mints its own
        // short-lived ticket (#305) and connects the EventSource (#306); frames
        // flow into the ProjectEventDispatcher.
        projectEventStreamManager.start();
    }

    private void handleProjectLoaded(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus, @Nonnull ProjectViewPlace place) {
        // Anchor the project-events delta channel at the current head BEFORE the
        // batched portlet state queries below are flushed. start() anchors the
        // event stream at "now"; issuing it here -- outside the batch, so its
        // request goes on the wire ahead of executeCurrentBatch() -- lets the
        // state queries observe a snapshot at or after the anchor. If the
        // anchor instead resolved after the state queries, an edit landing in
        // between would be lost: the loaded state would predate it while the
        // stream started after it (#301). Any gap between the anchor and the
        // first streamed window is recovered by the dispatcher's catch-up
        // fetch (#297); the stream itself reconnects and resumes on a glitch.
        projectEventDispatcher.start();

        dispatchServiceManager.beginBatch();
        topBarPresenter.start(view.getTopBarContainer(), eventBus, place);
        linkBarPresenter.start(view.getPerspectiveLinkBarViewContainer(), eventBus, place);
        perspectivePresenter.start(view.getPerspectiveViewContainer(), eventBus, place);
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
        projectEventStreamManager.stop();
        eventBus.dispose();
    }

    @Override
    public String toString() {
        return toStringHelper("ProjectPresenter")
                .addValue(projectId)
                .toString();
    }
}
