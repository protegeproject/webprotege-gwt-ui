package edu.stanford.bmir.protege.web.client.events;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.inject.EventPollingPeriod;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.perspective.HasChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@ProjectSingleton
public class EventPollingManager {

    Logger logger = Logger.getLogger("EventPollingManager");

    private final DispatchServiceManager dispatchServiceManager;

    private int pollingPeriodInMS;

    private Timer pollingTimer;

    private EventTag nextTag = EventTag.getFirst();

    private final ProjectId projectId;

    private final EventBus eventBus;

    private final LoggedInUserProvider loggedInUserProvider;

    private final ChangeRequestEventAwaiter eventAwaiter;

    @Inject
    public EventPollingManager(@EventPollingPeriod int pollingPeriodInMS,
                               ProjectId projectId,
                               EventBus eventBus,
                               DispatchServiceManager dispatchServiceManager,
                               LoggedInUserProvider loggedInUserProvider, ChangeRequestEventAwaiter eventAwaiter) {
        this.eventBus = eventBus;
        this.loggedInUserProvider = loggedInUserProvider;
        this.eventAwaiter = eventAwaiter;
        if(pollingPeriodInMS < 1) {
            throw new IllegalArgumentException("pollingPeriodInMS must be greater than 0");
        }
        this.pollingPeriodInMS = pollingPeriodInMS;
        this.projectId = checkNotNull(projectId, "projectId must not be null");
        pollingTimer = new Timer() {
            @Override
            public void run() {
                pollForProjectEvents();
            }
        };
        this.dispatchServiceManager = dispatchServiceManager;

    }

    public void start() {
        if(pollingTimer.isRunning()) {
            return;
        }
        pollingTimer.scheduleRepeating(pollingPeriodInMS);
    }

    public void stop() {
        pollingTimer.cancel();
    }


    public void pollForProjectEvents() {
        GWT.log("[Event Polling Manager] Polling for project events for " + projectId + " from " + nextTag);
        dispatchServiceManager.execute(GetProjectEventsAction.create(nextTag, projectId), (GetProjectEventsResult result) -> dispatchEvents(result.getEvents()));
    }


    public void dispatchEvents(EventList<?> eventList) {
        GWT.log("[Event Polling Manager] Retrieved " + eventList.getEvents().size() + " events from server. From " + eventList.getStartTag() + " to " + eventList.getEndTag() + " current next tag " + nextTag);

        if(eventList.isEmpty()) {
            return;
        }
        EventTag eventListStartTag = eventList.getStartTag();
        if(nextTag.isGreaterOrEqualTo(eventListStartTag)) {
            // We haven't missed any events - our next retrieval will be from where we got the event to.
        }
        if (!eventList.isEmpty()) {
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
                nextTag = eventList.getEndTag();
            }
        }
    }

}
