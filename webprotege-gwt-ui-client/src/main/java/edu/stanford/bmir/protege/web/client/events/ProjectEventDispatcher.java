package edu.stanford.bmir.protege.web.client.events;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Applies project events that arrive from the server to the client-side event
 * bus, keeping a bookmark ({@link #nextTag}) of how far the client has caught
 * up. Events are delivered by the {@link ProjectEventStreamManager} over the
 * SSE stream; this class owns everything downstream of the transport:
 *
 * <ul>
 *   <li>the #191 stale-window guard, so a window that has already been applied
 *   (or an older window arriving late) is never replayed;</li>
 *   <li>the #297 contiguity check plus catch-up fetch, so a window that does
 *   not continue from the bookmark triggers a pull of the missing span rather
 *   than being applied with a gap;</li>
 *   <li>the #301 open-time head anchor, so a freshly-opened project starts at
 *   "now" instead of replaying the whole archived history from ordinal zero.</li>
 * </ul>
 *
 * <p>The 10-second polling timer this class grew out of was removed with the
 * migration to SSE (#306): the stream's automatic reconnect plus
 * {@code Last-Event-ID} resume covers the safety-net role the poll used to
 * play, and the only remaining server pull is the catch-up fetch and the
 * open-time anchor -- both of which reuse {@link GetProjectEventsAction}.</p>
 *
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@ProjectSingleton
public class ProjectEventDispatcher {

    Logger logger = Logger.getLogger("ProjectEventDispatcher");

    private final DispatchServiceManager dispatchServiceManager;

    /**
     * The client's bookmark: the tag up to which project events have been
     * applied. {@code null} until the open-time anchor (see
     * {@link #requestAnchor()}) resolves. While unanchored the dispatcher
     * neither fetches from a bookmark nor applies pushed windows, so opening a
     * project never replays the entire archived history from ordinal zero (#301).
     */
    private EventTag nextTag = null;

    private final ProjectId projectId;

    private final EventBus eventBus;

    private final LoggedInUserProvider loggedInUserProvider;

    private final ChangeRequestEventAwaiter eventAwaiter;

    private final DispatchErrorMessageDisplay errorMessageDisplay;

    /**
     * True while a catch-up fetch for missed events is on the wire. Prevents a
     * burst of gapped windows from starting a fetch per window.
     */
    private boolean catchUpInFlight = false;

    /**
     * The highest endTag observed on windows that arrived (and were discarded)
     * while a catch-up fetch was in flight. If the fetch completes below this
     * mark, one follow-up fetch is issued to cover the remainder.
     */
    private EventTag catchUpHighWaterMark = null;

    /**
     * True while the open-time head anchor request is on the wire. Stops a
     * second {@link #start()} (e.g. a stream reconnect re-asserting the anchor)
     * from firing a second anchor before the first resolves. Cleared on both
     * success and failure so a failed anchor is retried by the next
     * {@link #start()}.
     */
    private boolean anchorInFlight = false;

    @Inject
    public ProjectEventDispatcher(ProjectId projectId,
                                  EventBus eventBus,
                                  DispatchServiceManager dispatchServiceManager,
                                  LoggedInUserProvider loggedInUserProvider,
                                  ChangeRequestEventAwaiter eventAwaiter,
                                  DispatchErrorMessageDisplay errorMessageDisplay) {
        this.eventBus = eventBus;
        this.loggedInUserProvider = loggedInUserProvider;
        this.eventAwaiter = eventAwaiter;
        this.errorMessageDisplay = errorMessageDisplay;
        this.projectId = checkNotNull(projectId, "projectId must not be null");
        this.dispatchServiceManager = dispatchServiceManager;
    }

    /**
     * Anchors the dispatcher at the current head, if it is not already anchored.
     * Called when the project opens and again whenever the event stream
     * (re)connects: while unanchored, pushed windows are dropped rather than
     * replaying the whole archive from ordinal zero (#301). The request is
     * idempotent -- once anchored, or while an anchor is already in flight, it
     * is a no-op -- so a reconnect re-asserting the anchor never disturbs a
     * dispatcher that is already caught up.
     */
    public void start() {
        requestAnchor();
    }

    private void requestAnchor() {
        if(nextTag != null || anchorInFlight) {
            return;
        }
        anchorInFlight = true;
        dispatchServiceManager.execute(GetProjectEventsAction.anchor(projectId),
                                       new DispatchServiceCallback<GetProjectEventsResult>(errorMessageDisplay) {
            @Override
            public void handleSuccess(GetProjectEventsResult result) {
                anchorInFlight = false;
                // Anchor the bookmark at the current head. Set nextTag directly
                // rather than routing the response through dispatchEvents: the
                // anchor window is empty, and dispatchEvents' empty-list early
                // return would leave the bookmark null forever.
                nextTag = result.getEvents().getEndTag();
                GWT.log("[ProjectEventDispatcher] Anchored " + projectId + " at " + nextTag);
            }

            @Override
            public void handleErrorFinally(Throwable throwable) {
                // Leave nextTag null so the next start() re-attempts the anchor.
                anchorInFlight = false;
                logger.log(Level.WARNING, "Anchor fetch for project events failed: " + throwable.getMessage());
            }
        });
    }


    public void dispatchEvents(EventList<?> eventList) {
        if(nextTag == null) {
            // Unanchored: a pushed window arrived before the open-time anchor
            // resolved. Dropping it is safe -- once anchored we start at the
            // head and, if the dropped window was contiguous, the next window's
            // gap triggers a catch-up fetch that re-delivers it. Applying it now
            // would also dereference a null nextTag in the contiguity checks
            // below (#297).
            GWT.log("[ProjectEventDispatcher] Dropping events received before anchor for " + projectId);
            return;
        }
        GWT.log("[ProjectEventDispatcher] Retrieved " + eventList.getEvents().size() + " events from server. From " + eventList.getStartTag() + " to " + eventList.getEndTag() + " current next tag " + nextTag);

        if(eventList.isEmpty()) {
            return;
        }
        EventTag eventListEndTag = eventList.getEndTag();
        if(nextTag.isGreaterOrEqualTo(eventListEndTag)) {
            // Events can still reach this method over two paths -- the SSE push
            // and the catch-up/anchor pull -- both of which end up here. This
            // window has already been dispatched via the other path (or is an
            // older window arriving late), so replaying it would re-apply stale
            // changes over newer ones: e.g. re-adding a removed parent to the
            // hierarchy or resurrecting a deleted class (#191).
            GWT.log("[ProjectEventDispatcher] Skipping already-dispatched events (up to " + eventListEndTag + ")");
            return;
        }
        if(eventList.getStartTag().getOrdinal() != nextTag.getOrdinal()) {
            // The window does not continue from the bookmark: either it jumps
            // ahead (events in between were lost somewhere upstream) or it
            // partially overlaps what was already applied. Events carry no
            // individual tags, so an overlapping window cannot be sliced --
            // in both cases the only safe recovery is to discard this window
            // and pull everything from the bookmark forward (#297).
            handleNonContiguousWindow(eventList);
            return;
        }
        applyWindow(eventList);
    }

    private void applyWindow(EventList<?> eventList) {
        try {
            for (WebProtegeEvent<?> event : eventList.getEvents()) {
                if (event.getSource() != null) {
                    eventBus.fireEventFromSource(event.asGWTEvent(), event.getSource());
                } else {
                    eventBus.fireEvent(event.asGWTEvent());
                }
            }
            // After dispatching the events handle in the one-shot event awaiter
            eventAwaiter.handleEvents(eventList.getEvents());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while sending events " + e.getMessage());
        } finally {
            // The guards above ensure this only ever moves the tag forward.
            nextTag = eventList.getEndTag();
        }
    }

    private void handleNonContiguousWindow(EventList<?> eventList) {
        int gapSize = eventList.getStartTag().getOrdinal() - nextTag.getOrdinal();
        GWT.log("[ProjectEventDispatcher] Missed events detected.  Bookmark: " + nextTag
                        + ", incoming window: " + eventList.getStartTag() + " to " + eventList.getEndTag()
                        + ", gap size: " + gapSize);
        logger.warning("Missed events detected for " + projectId + ".  Bookmark: " + nextTag
                               + ", incoming window: " + eventList.getStartTag() + " to " + eventList.getEndTag()
                               + ", gap size: " + gapSize);
        if(catchUpInFlight) {
            EventTag incomingEndTag = eventList.getEndTag();
            if(catchUpHighWaterMark == null || incomingEndTag.getOrdinal() > catchUpHighWaterMark.getOrdinal()) {
                catchUpHighWaterMark = incomingEndTag;
            }
            return;
        }
        startCatchUpFetch();
    }

    private void startCatchUpFetch() {
        catchUpInFlight = true;
        // The pull has no upper bound: it returns everything from the bookmark
        // to the head, including the discarded window's events, so filling the
        // gap and re-delivering the jumped-ahead window collapse into this one
        // fetch. Its response starts exactly at the bookmark, so it passes the
        // contiguity check and applies.
        EventTag fetchedFrom = nextTag;
        dispatchServiceManager.execute(GetProjectEventsAction.create(nextTag, projectId),
                                       new DispatchServiceCallback<GetProjectEventsResult>(errorMessageDisplay) {
            @Override
            public void handleSuccess(GetProjectEventsResult result) {
                // Clear the guard before re-entering dispatchEvents so the
                // fetched window is not treated as having arrived mid-flight.
                catchUpInFlight = false;
                dispatchEvents(result.getEvents());
                EventTag highWaterMark = catchUpHighWaterMark;
                catchUpHighWaterMark = null;
                boolean madeProgress = nextTag.getOrdinal() > fetchedFrom.getOrdinal();
                if(madeProgress && highWaterMark != null && !nextTag.isGreaterOrEqualTo(highWaterMark)) {
                    // Windows beyond the fetch's coverage arrived while it was
                    // in flight; issue one follow-up to cover the remainder.
                    // Without progress we stop instead: the missing events are
                    // not in the archive (yet), and refetching immediately
                    // would spin -- the next pushed window retries.
                    startCatchUpFetch();
                }
            }

            @Override
            public void handleErrorFinally(Throwable throwable) {
                // Never leave the guard wedged: the next pushed window
                // re-triggers catch-up.
                catchUpInFlight = false;
                catchUpHighWaterMark = null;
                logger.log(Level.WARNING, "Catch-up fetch for missed events failed: " + throwable.getMessage());
            }
        });
    }

}
