package edu.stanford.bmir.protege.web.client.events;

import com.google.common.collect.ImmutableList;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Regression tests for #191: project events reach the client over two
 * concurrent paths (websocket push and the polling safety net), both of
 * which funnel into {@link EventPollingManager#dispatchEvents(EventList)}.
 * A window of events that has already been dispatched must not be replayed
 * when the other path delivers it again (or delivers an older window late) —
 * replaying old hierarchy events re-adds removed parents/relationships and
 * resurrects deleted classes in the UI.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class EventPollingManager_TestCase {

    private EventPollingManager manager;

    @Mock
    private EventBus eventBus;

    @Mock
    private DispatchServiceManager dispatchServiceManager;

    @Mock
    private LoggedInUserProvider loggedInUserProvider;

    @Mock
    private ChangeRequestEventAwaiter eventAwaiter;

    @Before
    public void setUp() {
        manager = new EventPollingManager(10_000,
                                          ProjectId.getNil(),
                                          eventBus,
                                          dispatchServiceManager,
                                          loggedInUserProvider,
                                          eventAwaiter);
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

        // Same window arrives twice -- e.g. once over the websocket and
        // once from a poll that was already in flight.
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

        // A slow poll issued before the websocket delivery returns late,
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
}
