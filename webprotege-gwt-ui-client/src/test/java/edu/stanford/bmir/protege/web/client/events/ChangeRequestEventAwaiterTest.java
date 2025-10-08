package edu.stanford.bmir.protege.web.client.events;

import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.perspective.HasChangeRequestId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChangeRequestEventAwaiterTest {

    private ChangeRequestEventAwaiter awaiter;

    @Before
    public void setUp() {
        awaiter = new ChangeRequestEventAwaiter();
    }

    @Test
    public void shouldBufferEventsWhenNoHandlerIsRegistered_thenDeliverOnWait() {
        ChangeRequestId id = ChangeRequestId.valueOf(UUID.randomUUID().toString());
        WebProtegeEvent<?> event = eventWithId(id);

        // No handlers yet: should buffer
        awaiter.handleEvents(Collections.singletonList(event));

        ChangeRequestEventsHandler handler = mock(ChangeRequestEventsHandler.class);

        // Register handler later: should get buffered event immediately
        awaiter.waitForEvents(id, handler);

        ArgumentCaptor<Collection> captor = ArgumentCaptor.forClass(Collection.class);
        verify(handler, times(1)).handle(captor.capture());
        assertEquals(1, captor.getValue().size());
        assertTrue(captor.getValue().contains(event));
    }

    @Test
    public void shouldDeliverBufferedEventsImmediatelyWhenHandlerRegisters() {
        ChangeRequestId id = ChangeRequestId.valueOf(UUID.randomUUID().toString());
        WebProtegeEvent<?> event1 = eventWithId(id);

        // First buffer some events
        awaiter.handleEvents(Collections.singletonList(event1));

        ChangeRequestEventsHandler handler1 = mock(ChangeRequestEventsHandler.class);
        awaiter.waitForEvents(id, handler1);
        verify(handler1, times(1)).handle(anyCollection());

        // Send another event later (no handlers at this time)
        WebProtegeEvent<?> event2 = eventWithId(id);
        awaiter.handleEvents(Collections.singletonList(event2));

        // Second handler registers — should get event2
        ChangeRequestEventsHandler handler2 = mock(ChangeRequestEventsHandler.class);
        awaiter.waitForEvents(id, handler2);

        ArgumentCaptor<Collection> captor = ArgumentCaptor.forClass(Collection.class);
        verify(handler2, times(1)).handle(captor.capture());
        assertEquals(1, captor.getValue().size());
        assertTrue(captor.getValue().contains(event2));
    }

    @Test
    public void shouldDeliverEventsToWaitingHandlers_thenRemoveHandlers() {
        ChangeRequestId id = ChangeRequestId.valueOf(UUID.randomUUID().toString());
        ChangeRequestEventsHandler handler = mock(ChangeRequestEventsHandler.class);

        // Register first — should be "live" delivery
        awaiter.waitForEvents(id, handler);

        WebProtegeEvent<?> event1 = eventWithId(id);
        awaiter.handleEvents(Collections.singletonList(event1));

        ArgumentCaptor<Collection> captor = ArgumentCaptor.forClass(Collection.class);
        verify(handler, times(1)).handle(captor.capture());
        assertEquals(1, captor.getValue().size());
        assertTrue(captor.getValue().contains(event1));

        // Handler is one-shot, should be removed now
        WebProtegeEvent<?> event2 = eventWithId(id);
        awaiter.handleEvents(Collections.singletonList(event2));

        // Register a new handler, should get event2
        ChangeRequestEventsHandler handler2 = mock(ChangeRequestEventsHandler.class);
        awaiter.waitForEvents(id, handler2);

        ArgumentCaptor<Collection> captor2 = ArgumentCaptor.forClass(Collection.class);
        verify(handler2, times(1)).handle(captor2.capture());
        assertEquals(1, captor2.getValue().size());
        assertTrue(captor2.getValue().contains(event2));
    }

    @Test
    public void shouldDeliverEventsToAllHandlersWaitingForSameId() {
        ChangeRequestId id = ChangeRequestId.valueOf(UUID.randomUUID().toString());
        ChangeRequestEventsHandler h1 = mock(ChangeRequestEventsHandler.class);
        ChangeRequestEventsHandler h2 = mock(ChangeRequestEventsHandler.class);

        awaiter.waitForEvents(id, h1);
        awaiter.waitForEvents(id, h2);

        WebProtegeEvent<?> e1 = eventWithId(id);
        WebProtegeEvent<?> e2 = eventWithId(id);
        awaiter.handleEvents(Arrays.asList(e1, e2));

        ArgumentCaptor<Collection> captor1 = ArgumentCaptor.forClass(Collection.class);
        ArgumentCaptor<Collection> captor2 = ArgumentCaptor.forClass(Collection.class);
        verify(h1, times(1)).handle(captor1.capture());
        verify(h2, times(1)).handle(captor2.capture());

        assertEquals(2, captor1.getValue().size());
        assertTrue(captor1.getValue().containsAll(Arrays.asList(e1, e2)));
        assertEquals(2, captor2.getValue().size());
        assertTrue(captor2.getValue().containsAll(Arrays.asList(e1, e2)));
    }

    @Test
    public void shouldIgnoreEventsWithoutOrWithNullChangeRequestId() {
        ChangeRequestId id = ChangeRequestId.valueOf(UUID.randomUUID().toString());
        ChangeRequestEventsHandler handler = mock(ChangeRequestEventsHandler.class);
        awaiter.waitForEvents(id, handler);

        WebProtegeEvent<?> noInterface = mock(WebProtegeEvent.class);
        WebProtegeEvent<?> nullId = eventWithNullId();
        WebProtegeEvent<?> correct = eventWithId(id);

        awaiter.handleEvents(Arrays.asList(noInterface, nullId, correct));

        ArgumentCaptor<Collection> captor = ArgumentCaptor.forClass(Collection.class);
        verify(handler, times(1)).handle(captor.capture());
        assertEquals(1, captor.getValue().size());
        assertTrue(captor.getValue().contains(correct));
    }

    // ------------------------------------------------------------
    // Helper methods
    // ------------------------------------------------------------

    private static WebProtegeEvent<?> eventWithId(ChangeRequestId id) {
        WebProtegeEvent<?> evt = mock(WebProtegeEvent.class, withSettings().extraInterfaces(HasChangeRequestId.class));
        when(((HasChangeRequestId) evt).getChangeRequestId()).thenReturn(id);
        return evt;
    }

    private static WebProtegeEvent<?> eventWithNullId() {
        WebProtegeEvent<?> evt = mock(WebProtegeEvent.class, withSettings().extraInterfaces(HasChangeRequestId.class));
        when(((HasChangeRequestId) evt).getChangeRequestId()).thenReturn(null);
        return evt;
    }
}
