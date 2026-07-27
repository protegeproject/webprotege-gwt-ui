package edu.stanford.bmir.protege.web.client.events;

import com.google.common.collect.ImmutableList;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Regression tests for #191: project events reach the client over two
 * concurrent paths (the SSE push and the catch-up/anchor pull), both of
 * which funnel into {@link ProjectEventDispatcher#dispatchEvents(EventList)}.
 * A window of events that has already been dispatched must not be replayed
 * when the other path delivers it again (or delivers an older window late) —
 * replaying old hierarchy events re-adds removed parents/relationships and
 * resurrects deleted classes in the UI.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProjectEventDispatcher_TestCase {

    private ProjectEventDispatcher manager;

    @Mock
    private EventBus eventBus;

    @Mock
    private DispatchServiceManager dispatchServiceManager;

    @Mock
    private LoggedInUserProvider loggedInUserProvider;

    @Mock
    private ChangeRequestEventAwaiter eventAwaiter;

    @Mock
    private DispatchErrorMessageDisplay errorMessageDisplay;

    @Captor
    private ArgumentCaptor<GetProjectEventsAction> actionCaptor;

    @Captor
    @SuppressWarnings("rawtypes")
    private ArgumentCaptor<DispatchServiceCallback> callbackCaptor;

    @Before
    public void setUp() {
        manager = newManager();
        // #301: the dispatcher now opens unanchored (nextTag == null). Anchor it
        // at tag 0 so the existing #191/#297 dispatch tests exercise the same
        // bookmark they always started from; the reset() inside clears the
        // anchor request so it does not perturb the dispatch-count assertions.
        anchorAtHead(0);
    }

    private ProjectEventDispatcher newManager() {
        return new ProjectEventDispatcher(ProjectId.getNil(),
                                          eventBus,
                                          dispatchServiceManager,
                                          loggedInUserProvider,
                                          eventAwaiter,
                                          errorMessageDisplay);
    }

    /**
     * Drives {@link #manager}'s open-time head anchor to completion so it is
     * bookmarked at {@code headTag}, then clears the mock's interaction history
     * so the anchor request does not count towards the dispatch assertions in
     * the tests that follow.
     */
    @SuppressWarnings("unchecked")
    private void anchorAtHead(int headTag) {
        manager.start();
        ArgumentCaptor<DispatchServiceCallback> anchorCallback = ArgumentCaptor.forClass(DispatchServiceCallback.class);
        verify(dispatchServiceManager).execute(any(GetProjectEventsAction.class), anchorCallback.capture());
        anchorCallback.getValue().onSuccess(resultOf(EventList.create(EventTag.get(headTag), ImmutableList.of(), EventTag.get(headTag))));
        reset(dispatchServiceManager);
    }

    @SuppressWarnings("unchecked")
    private static WebProtegeEvent<?> eventBackedBy(Event<?> gwtEvent) {
        WebProtegeEvent<Object> event = mock(WebProtegeEvent.class);
        when(event.asGWTEvent()).thenReturn((Event<Object>) gwtEvent);
        return event;
    }

    private static EventList<WebProtegeEvent<?>> window(int startTag, int endTag, WebProtegeEvent<?> event) {
        return EventList.create(EventTag.get(startTag),
                                ImmutableList.of(event),
                                EventTag.get(endTag));
    }

    @Test
    public void shouldDispatchAndAdvanceOnForwardProgression() {
        Event<?> gwtEventA = mock(Event.class);
        Event<?> gwtEventB = mock(Event.class);
        WebProtegeEvent<?> eventA = eventBackedBy(gwtEventA);
        WebProtegeEvent<?> eventB = eventBackedBy(gwtEventB);

        manager.dispatchEvents(window(0, 5, eventA));
        manager.dispatchEvents(window(5, 8, eventB));

        verify(eventBus).fireEvent(gwtEventA);
        verify(eventBus).fireEvent(gwtEventB);
        verify(eventAwaiter, times(2)).handleEvents(any());
    }

    @Test
    public void shouldNotReplayAWindowThatWasAlreadyDispatched() {
        Event<?> gwtEvent = mock(Event.class);
        WebProtegeEvent<?> event = eventBackedBy(gwtEvent);

        // Same window arrives twice -- e.g. once over the SSE push and
        // once from a catch-up fetch that was already in flight.
        manager.dispatchEvents(window(0, 5, event));
        manager.dispatchEvents(window(0, 5, event));

        verify(eventBus, times(1)).fireEvent(gwtEvent);
        verify(eventAwaiter, times(1)).handleEvents(any());
    }

    @Test
    public void shouldDropAnOlderWindowArrivingAfterANewerOne() {
        Event<?> newGwtEvent = mock(Event.class);
        Event<?> staleGwtEvent = mock(Event.class);
        WebProtegeEvent<?> newEvent = eventBackedBy(newGwtEvent);
        WebProtegeEvent<?> staleEvent = eventBackedBy(staleGwtEvent);

        // A slow fetch issued before the SSE delivery returns late,
        // carrying an older window of already-applied events.
        manager.dispatchEvents(window(0, 10, newEvent));
        manager.dispatchEvents(window(2, 5, staleEvent));

        verify(eventBus, never()).fireEvent(staleGwtEvent);
    }

    @Test
    public void shouldNotMoveTheTagBackwardWhenAnOlderWindowArrives() {
        Event<?> gwtEventA = mock(Event.class);
        Event<?> staleGwtEvent = mock(Event.class);
        Event<?> gwtEventC = mock(Event.class);
        WebProtegeEvent<?> eventA = eventBackedBy(gwtEventA);
        WebProtegeEvent<?> staleEvent = eventBackedBy(staleGwtEvent);
        WebProtegeEvent<?> eventC = eventBackedBy(gwtEventC);

        manager.dispatchEvents(window(0, 10, eventA));
        // Late-arriving older window: must not rewind the high-water mark...
        manager.dispatchEvents(window(2, 5, staleEvent));
        // ...otherwise this already-applied window would be replayed too.
        manager.dispatchEvents(window(6, 10, eventC));

        verify(eventBus).fireEvent(gwtEventA);
        verify(eventBus, never()).fireEvent(staleGwtEvent);
        verify(eventBus, never()).fireEvent(gwtEventC);
    }

    @Test
    public void shouldFetchWhenTheHeartbeatHeadIsAheadOfTheBookmark() {
        // Bookmark at 5; a heartbeat reports the server head at 9 -- events were
        // lost on a connection that never errored, with nothing after them to
        // expose the gap. The heartbeat is what exposes it.
        manager.dispatchEvents(window(0, 5, eventBackedBy(mock(Event.class))));

        manager.ensureCaughtUpTo(9);

        verify(dispatchServiceManager, atLeastOnce()).execute(actionCaptor.capture(), callbackCaptor.capture());
        assertThat(actionCaptor.getValue().getSinceTag(), is(equalTo(EventTag.get(5))));
    }

    @Test
    public void shouldNotFetchWhenTheHeartbeatHeadMatchesTheBookmark() {
        manager.dispatchEvents(window(0, 5, eventBackedBy(mock(Event.class))));
        int before = org.mockito.Mockito.mockingDetails(dispatchServiceManager).getInvocations().size();

        manager.ensureCaughtUpTo(5);

        assertThat(org.mockito.Mockito.mockingDetails(dispatchServiceManager).getInvocations().size(), is(before));
    }

    // ------------------------------------------------------------------
    // #297: a window that does not continue exactly from the bookmark
    // means events were missed. The window must not be applied; the
    // missing span is recovered through a catch-up fetch from the
    // bookmark, which supersedes the discarded window.
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private DispatchServiceCallback<GetProjectEventsResult> capturedCatchUpCallback() {
        verify(dispatchServiceManager, atLeastOnce()).execute(actionCaptor.capture(), callbackCaptor.capture());
        return callbackCaptor.getValue();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static GetProjectEventsResult resultOf(EventList<WebProtegeEvent<?>> window) {
        GetProjectEventsResult result = mock(GetProjectEventsResult.class);
        when(result.getEvents()).thenReturn((EventList) window);
        return result;
    }

    @Test
    public void shouldNotApplyAGappedWindowAndShouldFetchFromTheBookmark() {
        Event<?> appliedGwtEvent = mock(Event.class);
        Event<?> gappedGwtEvent = mock(Event.class);
        WebProtegeEvent<?> applied = eventBackedBy(appliedGwtEvent);
        WebProtegeEvent<?> gapped = eventBackedBy(gappedGwtEvent);

        manager.dispatchEvents(window(0, 5, applied));
        // Jumps ahead: events 5..7 were lost somewhere upstream.
        manager.dispatchEvents(window(7, 9, gapped));

        verify(eventBus, never()).fireEvent(gappedGwtEvent);
        capturedCatchUpCallback();
        assertThat(actionCaptor.getValue().getSinceTag(), is(equalTo(EventTag.get(5))));
    }

    @Test
    public void shouldApplyTheCatchUpResultAndResumeNormalDispatch() {
        Event<?> recoveredGwtEvent = mock(Event.class);
        Event<?> nextGwtEvent = mock(Event.class);
        WebProtegeEvent<?> recovered = eventBackedBy(recoveredGwtEvent);
        WebProtegeEvent<?> next = eventBackedBy(nextGwtEvent);

        manager.dispatchEvents(window(0, 5, eventBackedBy(mock(Event.class))));
        manager.dispatchEvents(window(7, 9, eventBackedBy(mock(Event.class))));

        // The catch-up fetch returns everything from the bookmark to the
        // head, including the discarded window's events.
        capturedCatchUpCallback().onSuccess(resultOf(window(5, 9, recovered)));

        verify(eventBus).fireEvent(recoveredGwtEvent);
        // The bookmark advanced to 9, so a contiguous follow-on window applies.
        manager.dispatchEvents(window(9, 11, next));
        verify(eventBus).fireEvent(nextGwtEvent);
    }

    @Test
    public void shouldRefetchInsteadOfApplyingAPartiallyOverlappingWindow() {
        Event<?> overlappingGwtEvent = mock(Event.class);
        WebProtegeEvent<?> overlapping = eventBackedBy(overlappingGwtEvent);

        manager.dispatchEvents(window(0, 10, eventBackedBy(mock(Event.class))));
        // Starts before the bookmark but ends past it: events are not
        // individually tagged, so the unseen tail cannot be sliced out.
        manager.dispatchEvents(window(6, 12, overlapping));

        verify(eventBus, never()).fireEvent(overlappingGwtEvent);
        capturedCatchUpCallback();
        assertThat(actionCaptor.getValue().getSinceTag(), is(equalTo(EventTag.get(10))));
    }

    @Test
    public void shouldNotStartASecondFetchWhileOneIsInFlightButShouldFollowUp() {
        manager.dispatchEvents(window(0, 5, eventBackedBy(mock(Event.class))));
        manager.dispatchEvents(window(7, 9, eventBackedBy(mock(Event.class))));
        // A further window arrives while the fetch is on the wire.
        manager.dispatchEvents(window(11, 13, eventBackedBy(mock(Event.class))));

        verify(dispatchServiceManager, times(1)).execute(any(GetProjectEventsAction.class), any(DispatchServiceCallback.class));

        // The fetch completes below the high-water mark (13), so exactly one
        // follow-up fetch is issued from the advanced bookmark.
        capturedCatchUpCallback().onSuccess(resultOf(window(5, 9, eventBackedBy(mock(Event.class)))));

        verify(dispatchServiceManager, times(2)).execute(actionCaptor.capture(), callbackCaptor.capture());
        assertThat(actionCaptor.getValue().getSinceTag(), is(equalTo(EventTag.get(9))));
    }

    @Test
    public void shouldRecoverFromAFailedCatchUpFetch() {
        manager.dispatchEvents(window(0, 5, eventBackedBy(mock(Event.class))));
        manager.dispatchEvents(window(7, 9, eventBackedBy(mock(Event.class))));

        capturedCatchUpCallback().onFailure(new RuntimeException("boom"));

        // The in-flight guard was cleared, so the next gapped window starts a
        // fresh fetch rather than being silently dropped.
        manager.dispatchEvents(window(7, 9, eventBackedBy(mock(Event.class))));
        verify(dispatchServiceManager, times(2)).execute(any(GetProjectEventsAction.class), any(DispatchServiceCallback.class));
    }

    @Test
    public void shouldNotSpinWhenTheCatchUpFetchMakesNoProgress() {
        manager.dispatchEvents(window(0, 5, eventBackedBy(mock(Event.class))));
        manager.dispatchEvents(window(7, 9, eventBackedBy(mock(Event.class))));
        // Another window arrives mid-flight, raising the high-water mark.
        manager.dispatchEvents(window(11, 13, eventBackedBy(mock(Event.class))));

        // The archive has not caught up yet: the fetch returns nothing and
        // the bookmark does not advance. No immediate refetch -- the next
        // pushed window retries instead.
        capturedCatchUpCallback().onSuccess(resultOf(EventList.create(EventTag.get(5), ImmutableList.of(), EventTag.get(5))));

        verify(dispatchServiceManager, times(1)).execute(any(GetProjectEventsAction.class), any(DispatchServiceCallback.class));
    }

    // ------------------------------------------------------------------
    // #301: a freshly-opened project must not replay the whole archive.
    // The dispatcher opens unanchored and first asks only for the current head
    // (an anchor request); it neither fetches from a bookmark nor applies
    // pushed windows until that anchor resolves.
    // ------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private DispatchServiceCallback<GetProjectEventsResult> capturedAnchorCallback() {
        verify(dispatchServiceManager, atLeastOnce()).execute(actionCaptor.capture(), callbackCaptor.capture());
        return callbackCaptor.getValue();
    }

    @Test
    public void shouldRequestTheHeadAnchorRatherThanPollingWhileUnanchored() {
        ProjectEventDispatcher unanchored = newManager();

        unanchored.start();

        // The first request is the head anchor (latestOnly), not a since-tag
        // fetch that would drag down the entire history from ordinal zero.
        verify(dispatchServiceManager).execute(actionCaptor.capture(), any(DispatchServiceCallback.class));
        assertThat(actionCaptor.getValue().isLatestOnly(), is(true));
        verify(eventBus, never()).fireEvent(any(Event.class));
    }

    @Test
    public void shouldAnchorTheBookmarkAtTheResponseEndTag() {
        ProjectEventDispatcher unanchored = newManager();
        unanchored.start();

        // The anchor response carries no events, only the current head (42).
        capturedAnchorCallback().onSuccess(resultOf(EventList.create(EventTag.get(42), ImmutableList.of(), EventTag.get(42))));

        // Bookmarked at 42: a window that continues from 42 now applies.
        Event<?> gwtEvent = mock(Event.class);
        unanchored.dispatchEvents(window(42, 45, eventBackedBy(gwtEvent)));
        verify(eventBus).fireEvent(gwtEvent);
    }

    @Test
    public void shouldDropWindowsArrivingBeforeTheAnchorWithoutWedging() {
        ProjectEventDispatcher unanchored = newManager();

        // A pushed window arrives before the dispatcher has anchored.
        Event<?> earlyGwtEvent = mock(Event.class);
        unanchored.dispatchEvents(window(0, 5, eventBackedBy(earlyGwtEvent)));

        verify(eventBus, never()).fireEvent(earlyGwtEvent);
        // Dropping the window must not start any fetch...
        verify(dispatchServiceManager, never()).execute(any(GetProjectEventsAction.class), any(DispatchServiceCallback.class));

        // ...and the dispatcher is not wedged: it still anchors and then dispatches.
        unanchored.start();
        capturedAnchorCallback().onSuccess(resultOf(EventList.create(EventTag.get(10), ImmutableList.of(), EventTag.get(10))));
        Event<?> laterGwtEvent = mock(Event.class);
        unanchored.dispatchEvents(window(10, 12, eventBackedBy(laterGwtEvent)));
        verify(eventBus).fireEvent(laterGwtEvent);
    }

    @Test
    public void shouldRetryTheAnchorAfterAFailure() {
        ProjectEventDispatcher unanchored = newManager();
        unanchored.start();

        capturedAnchorCallback().onFailure(new RuntimeException("boom"));

        // The in-flight guard was cleared and the bookmark is still unset, so
        // the next start() issues a fresh anchor rather than giving up.
        unanchored.start();
        verify(dispatchServiceManager, times(2)).execute(any(GetProjectEventsAction.class), any(DispatchServiceCallback.class));
    }

    @Test
    public void shouldNotIssueASecondAnchorWhileOneIsInFlight() {
        ProjectEventDispatcher unanchored = newManager();

        unanchored.start();
        // A second start() fires before the anchor resolves.
        unanchored.start();

        verify(dispatchServiceManager, times(1)).execute(any(GetProjectEventsAction.class), any(DispatchServiceCallback.class));
    }
}
