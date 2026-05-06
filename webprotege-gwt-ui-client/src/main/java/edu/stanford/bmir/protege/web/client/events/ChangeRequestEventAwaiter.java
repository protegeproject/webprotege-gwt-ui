package edu.stanford.bmir.protege.web.client.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.user.client.Timer;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.perspective.HasChangeRequestId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * A project-scoped component that coordinates the delivery of {@link WebProtegeEvent}s
 * associated with specific {@link ChangeRequestId}s.  This can be used to ensure that
 * events associated with a change have happened and have been processed on the client
 * side before any further action is taken.
 * <p>
 * The {@code ChangeRequestEventAwaiter} enables client code to wait for events that are expected
 * to arrive asynchronously as the result of a change request. Events can either be delivered
 * immediately (if already buffered) or stored and delivered later when matching events arrive.
 * </p>
 *
 * <h3>Usage Model</h3>
 * <ul>
 *   <li>Call {@link #waitForEvents(ChangeRequestId, ChangeRequestEventsHandler)} to register a handler
 *   for a particular {@code ChangeRequestId}. If events for that ID have already arrived, they are delivered immediately.</li>
 *   <li>Call {@link #handleEvents(List)} when new events are received. The events are grouped by
 *   {@code ChangeRequestId} and either delivered to waiting handlers or buffered for future delivery.</li>
 * </ul>
 *
 * <p>
 * This class is designed for one-shot event delivery: handlers are removed after they are invoked,
 * and buffered events are consumed upon delivery. Buffered events accumulate only when no handler
 * is currently waiting for a given change request.
 * </p>
 *
 * <p>
 * Thread-safety: This class is intended for use within the GWT client thread and is not thread-safe.
 * </p>
 */
@ProjectSingleton
public class ChangeRequestEventAwaiter {

    private static final Logger logger = Logger.getLogger(ChangeRequestEventAwaiter.class.getName());

    /**
     * How long to wait for matching events before delivering an empty event
     * list to a registered handler. Some change requests do not produce any
     * events that implement {@link HasChangeRequestId} (e.g. individual
     * creation fires only {@code BrowserTextChangedEvent} /
     * {@code NamedIndividualFrameChangedEvent}, which do not carry a
     * {@link ChangeRequestId}). Without this fallback the handler would
     * never fire and downstream callers (e.g. the create-entity flow) would
     * never see their result.
     */
    static final int FALLBACK_TIMEOUT_MS = 1500;

    /**
     * Handlers waiting for events keyed by {@link ChangeRequestId}.
     */
    private final Multimap<ChangeRequestId, ChangeRequestEventsHandler> handlers = HashMultimap.create();

    /**
     * Events that have arrived before any corresponding handler was registered.
     * These are buffered and delivered when a handler later calls {@link #waitForEvents(ChangeRequestId, ChangeRequestEventsHandler)}.
     */
    private final Multimap<ChangeRequestId, WebProtegeEvent<?>> bufferedEvents = HashMultimap.create();

    /**
     * Lazy provider for the project's {@link EventPollingManager}. Lazy
     * because {@link EventPollingManager} already depends on this class
     * (via constructor injection) and a direct field would create a Dagger
     * cycle.
     */
    private final Provider<EventPollingManager> eventPollingManagerProvider;

    @Inject
    public ChangeRequestEventAwaiter(@Nonnull Provider<EventPollingManager> eventPollingManagerProvider) {
        this.eventPollingManagerProvider = eventPollingManagerProvider;
    }

    /**
     * Waits for events associated with the specified {@link ChangeRequestId}.
     * <p>
     * If any events for the given change request have already arrived and been buffered,
     * they are immediately delivered to the provided handler. Otherwise, the handler is registered
     * to receive future events for that ID.
     * </p>
     *
     * @param changeRequestId The ID of the change request to wait for.
     * @param handler         The handler that will be invoked when events for the given ID are available.
     */
    public void waitForEvents(ChangeRequestId changeRequestId, ChangeRequestEventsHandler handler) {
        // Retrieve and remove any buffered events for this ID
        Collection<WebProtegeEvent<?>> alreadyArrivedEvents = bufferedEvents.removeAll(changeRequestId);
        if (alreadyArrivedEvents.isEmpty()) {
            // No buffered events: store the handler for later delivery and
            // arm a fallback timer so the handler still fires if the change
            // request produces no HasChangeRequestId events.
            handlers.put(changeRequestId, handler);
            scheduleFallback(() -> {
                if (handlers.remove(changeRequestId, handler)) {
                    handler.handle(Collections.emptyList());
                }
            });
            // Also trigger an immediate event poll so the matching events
            // arrive within one extra round-trip rather than waiting for
            // the next periodic tick (default 10s — see
            // EventPollingPeriodProvider). Without this kick, every
            // hierarchy create incurs the full FALLBACK_TIMEOUT_MS as
            // dead time before the tree updates and the new node can be
            // selected.
            requestImmediatePoll();
        } else {
            // Deliver buffered events immediately
            handler.handle(alreadyArrivedEvents);
        }
    }

    /**
     * Asks the {@link EventPollingManager} to poll for events right now.
     * Visible for testing — JVM unit tests may override this to capture
     * the call without touching the GWT-only timer / network stack.
     */
    protected void requestImmediatePoll() {
        try {
            eventPollingManagerProvider.get().pollForProjectEvents();
        } catch (RuntimeException e) {
            // Polling is best-effort; the fallback timer guarantees the
            // handler eventually fires either way.
            logger.warning("Immediate event poll failed: " + e.getMessage());
        }
    }

    /**
     * Schedules {@code callback} to run after {@link #FALLBACK_TIMEOUT_MS}
     * milliseconds. Visible for testing — JVM unit tests may override this
     * to run synchronously or skip the schedule entirely (the production
     * implementation uses {@link Timer} which is GWT-only).
     */
    protected void scheduleFallback(@Nonnull Runnable callback) {
        Timer timer = new Timer() {
            @Override
            public void run() {
                callback.run();
            }
        };
        timer.schedule(FALLBACK_TIMEOUT_MS);
    }

    /**
     * Handles a batch of incoming events, grouping them by {@link ChangeRequestId}.
     * <p>
     * For each group of events:
     * <ul>
     *   <li>If one or more handlers are waiting for that change request, the events are delivered
     *   to all of those handlers, and the handlers are then removed.</li>
     *   <li>If no handlers are waiting, the events are buffered so they can be delivered later
     *   when a corresponding handler registers.</li>
     * </ul>
     * </p>
     *
     * @param events The list of events to handle. Must not be {@code null}.
     */
    public void handleEvents(@Nonnull List<WebProtegeEvent> events) {
        if (events.isEmpty()) {
            return;
        }

        // Group events by ChangeRequestId
        Multimap<ChangeRequestId, WebProtegeEvent<?>> eventsByChangeRequestId = HashMultimap.create();

        for (WebProtegeEvent<?> event : events) {
            if (event instanceof HasChangeRequestId) {
                ChangeRequestId id = ((HasChangeRequestId) event).getChangeRequestId();
                if (id != null) {
                    eventsByChangeRequestId.put(id, event);
                } else {
                    logger.fine(() -> "Skipping event with null ChangeRequestId: " + event.getClass().getName());
                }
            }
        }

        if (eventsByChangeRequestId.isEmpty()) {
            return;
        }

        // Deliver or buffer events by ChangeRequestId
        for (ChangeRequestId changeRequestId : eventsByChangeRequestId.keySet()) {
            Collection<WebProtegeEvent<?>> eventsForChangeRequestId = eventsByChangeRequestId.get(changeRequestId);
            Collection<ChangeRequestEventsHandler> handlersForId = handlers.removeAll(changeRequestId);

            if (handlersForId.isEmpty()) {
                // No registered handlers: buffer events for later
                bufferedEvents.putAll(changeRequestId, eventsForChangeRequestId);
                logger.fine(() -> "Buffered " + eventsForChangeRequestId.size() + " events for " + changeRequestId);
            } else {
                // Deliver events to all waiting handlers
                handlersForId.forEach(h -> h.handle(eventsForChangeRequestId));
                logger.fine(() -> "Delivered " + eventsForChangeRequestId.size() + " events to "
                                  + handlersForId.size() + " handlers for " + changeRequestId);
            }
        }
    }
}
