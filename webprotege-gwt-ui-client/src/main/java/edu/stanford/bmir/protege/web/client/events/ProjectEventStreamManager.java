package edu.stanford.bmir.protege.web.client.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetUserInfoAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.TranslateEventListAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Owns the browser-native {@link com.google.gwt.core.client.JavaScriptObject
 * EventSource} that streams project change events from the gateway, replacing
 * the StompJs WebSocket subscription (#306). Received frames are fed, unchanged,
 * into {@link ProjectEventDispatcher} through the same
 * {@link TranslateEventListAction} round-trip the WebSocket path used, so no new
 * deserialization happens on the client.
 *
 * <h3>Connecting</h3>
 * {@code EventSource} cannot set request headers, so the stream is authorized
 * with a short-lived, project-scoped ticket carried in the query string (#305):
 * {@link #start()} fetches a fresh bearer token via {@link GetUserInfoAction}
 * (the servlet-copied access token expires in ~300s, so it is always minted
 * fresh), posts it to {@code /data/events/ticket}, then opens
 * {@code /data/projects/{projectId}/events?ticket=...}. All URLs are relative,
 * so the browser resolves them same-origin against the page -- the {@code /data/}
 * path fronts the gateway behind nginx.
 *
 * <h3>Reconnecting</h3>
 * The decision logic lives here in Java; the JSNI is a thin wrapper. On
 * {@code onerror} the browser reports {@code readyState}:
 * <ul>
 *   <li>{@code CONNECTING} -- the browser is auto-reconnecting and will resend
 *   {@code Last-Event-ID} itself; nothing to do.</li>
 *   <li>{@code CLOSED} -- a fatal error (a non-2xx redemption such as an expired
 *   ticket -> 401 does <em>not</em> auto-retry). Re-mint a ticket and open a
 *   <em>new</em> {@code EventSource}, passing the last id we saw as
 *   {@code &lastEventId=} because a fresh {@code EventSource} never sends the
 *   {@code Last-Event-ID} header.</li>
 * </ul>
 */
@ProjectSingleton
public class ProjectEventStreamManager {

    private static final Logger logger = Logger.getLogger(ProjectEventStreamManager.class.getName());

    static final String TICKET_URL = "/data/events/ticket";

    /**
     * {@code EventSource.readyState} values (per the WHATWG spec). Mirrored here
     * so the reconnect decision can be made and tested in plain Java.
     */
    static final int READY_STATE_CONNECTING = 0;
    static final int READY_STATE_OPEN = 1;
    static final int READY_STATE_CLOSED = 2;

    private final ProjectId projectId;

    private final DispatchServiceManager dispatchServiceManager;

    private final ProjectEventDispatcher dispatcher;

    /**
     * The live {@code EventSource}. Held so the connection lives exactly as long
     * as the project view does and is torn down explicitly in {@link #stop()}.
     */
    private JavaScriptObject eventSource;

    /**
     * The id of the last frame received on the wire. Used as {@code lastEventId}
     * when we open a replacement stream after a fatal error, so the server
     * resumes where the dead connection left off. This tracks the transport
     * position (what arrived), which the dispatcher's own bookmark (what was
     * applied) may lag behind; the dispatcher's #297 catch-up reconciles any
     * difference.
     */
    private String lastEventId = null;

    /**
     * Set once {@link #stop()} has been called so the error the browser fires as
     * a deliberately-closed connection tears down does not trigger a reopen.
     */
    private boolean closed = false;

    /**
     * True while the {@code EventSource} is actually open. The watchdog uses it
     * to tell "connected" apart from "an open attempt died somewhere along the
     * token/ticket/connect chain".
     */
    private boolean streamLive = false;

    /** Guards against stacking more than one pending watchdog check. */
    private boolean watchdogArmed = false;

    /**
     * How long an open attempt gets before the watchdog declares it dead and
     * tries again. Long enough for the token round-trip, ticket mint, and
     * connect; short enough that a viewer does not sit blind for long.
     */
    static final int WATCHDOG_DELAY_MS = 10_000;

    @Inject
    public ProjectEventStreamManager(ProjectId projectId,
                                     DispatchServiceManager dispatchServiceManager,
                                     ProjectEventDispatcher dispatcher) {
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.dispatcher = dispatcher;
    }

    /**
     * Opens the event stream for the project. Called when the project view is
     * displayed.
     */
    public void start() {
        closed = false;
        acquireTicketAndOpen();
    }

    /**
     * Closes the event stream and stops it reconnecting. Called when the project
     * view is disposed.
     */
    public void stop() {
        closed = true;
        closeEventSource();
    }

    /**
     * Mints a fresh stream ticket and opens (or reopens) the {@code EventSource}
     * with it. Visible (non-private) so JVM unit tests can override it to verify
     * the reconnect decision without touching the network or JSNI.
     */
    void acquireTicketAndOpen() {
        if (closed) {
            return;
        }
        // The token fetch, the ticket mint, and the connect can each fail with
        // nothing but a log line -- most plainly when the machine is offline, in
        // which case the browser also abandons the old EventSource for good. The
        // watchdog retries the whole chain until a stream is actually live, so a
        // failed open is never a dead end.
        armWatchdog();
        // Always fetch a fresh bearer: the servlet-copied access token lives
        // ~300s, so a cached one may already be expired by reconnect time.
        dispatchServiceManager.execute(new GetUserInfoAction(),
                                       userInfo -> requestTicket(userInfo.getToken()));
    }

    private void armWatchdog() {
        if (watchdogArmed) {
            return;
        }
        watchdogArmed = true;
        scheduleWatchdog(WATCHDOG_DELAY_MS);
    }

    /**
     * The watchdog check: if the stream is not live by the time it fires, the
     * open attempt died somewhere -- start another one (which re-arms the
     * watchdog, so retrying continues until the stream opens or the project
     * view is disposed).
     */
    void watchdogFired() {
        watchdogArmed = false;
        if (closed || streamLive) {
            return;
        }
        logger.info("Event stream did not come up; retrying");
        acquireTicketAndOpen();
    }

    /**
     * Schedules {@link #watchdogFired()} after the given delay. Package-visible
     * so JVM unit tests can override it -- a GWT timer cannot run off-browser.
     */
    void scheduleWatchdog(int delayMs) {
        new com.google.gwt.user.client.Timer() {
            @Override
            public void run() {
                watchdogFired();
            }
        }.schedule(delayMs);
    }

    private void requestTicket(String bearerToken) {
        if (closed) {
            return;
        }
        try {
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, TICKET_URL);
            requestBuilder.setHeader("Authorization", "Bearer " + bearerToken);
            requestBuilder.setHeader("Content-Type", "application/json");
            requestBuilder.setRequestData("{\"projectId\":\"" + projectId.getId() + "\"}");
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (closed) {
                        return;
                    }
                    if (response.getStatusCode() == Response.SC_OK) {
                        String ticket = parseTicket(response.getText());
                        if (ticket != null) {
                            openStream(ticket);
                        } else {
                            logger.warning("Stream ticket response contained no ticket field");
                        }
                    } else {
                        logger.warning("Stream ticket request failed with status " + response.getStatusCode());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    logger.log(Level.WARNING, "Stream ticket request errored: " + exception.getMessage());
                }
            });
            requestBuilder.send();
        } catch (RequestException e) {
            logger.log(Level.WARNING, "Could not send stream ticket request: " + e.getMessage());
        }
    }

    private static String parseTicket(String responseText) {
        JSONValue value = JSONParser.parseStrict(responseText);
        JSONObject object = value.isObject();
        if (object == null) {
            return null;
        }
        JSONValue ticketValue = object.get("ticket");
        if (ticketValue == null) {
            return null;
        }
        JSONString ticketString = ticketValue.isString();
        return ticketString == null ? null : ticketString.stringValue();
    }

    private void openStream(String ticket) {
        if (closed) {
            return;
        }
        openEventSource(buildStreamUrl(ticket, lastEventId));
    }

    /**
     * Builds the relative stream URL. A fresh {@code EventSource} never sends
     * the {@code Last-Event-ID} header, so when we reopen after a fatal error
     * the resume position is carried as {@code &lastEventId=} instead. The
     * ticket is a URL-safe token (#305) and the id is an integer, so neither
     * needs escaping.
     */
    static String buildStreamUrl(ProjectId projectId, String ticket, String lastEventId) {
        StringBuilder url = new StringBuilder("/data/projects/")
                .append(projectId.getId())
                .append("/events?ticket=")
                .append(ticket);
        if (lastEventId != null && !lastEventId.isEmpty()) {
            url.append("&lastEventId=").append(lastEventId);
        }
        return url.toString();
    }

    private String buildStreamUrl(String ticket, String lastEventId) {
        return buildStreamUrl(projectId, ticket, lastEventId);
    }

    /**
     * Called from JSNI when the stream (re)connects. Re-asserts the dispatcher's
     * open-time anchor -- a no-op once anchored, so a reconnect never disturbs a
     * caught-up dispatcher, but it retries an anchor that failed on first open.
     */
    void handleOpen() {
        streamLive = true;
        dispatcher.start();
    }

    /**
     * Called from JSNI for each {@code project-events} frame. Records the
     * transport resume position, then feeds the frame body into the dispatcher
     * through the existing {@link TranslateEventListAction} round-trip.
     */
    void handleMessage(String data, String eventId) {
        if (eventId != null && !eventId.isEmpty()) {
            lastEventId = eventId;
        }
        dispatchServiceManager.execute(TranslateEventListAction.create(data),
                                       (GetProjectEventsResult result) -> dispatcher.dispatchEvents(result.getEvents()));
    }

    /**
     * Called from JSNI on {@code onerror}. {@code CONNECTING} means the browser
     * is auto-reconnecting (it resends {@code Last-Event-ID} itself), so there
     * is nothing to do; {@code CLOSED} is fatal and needs a fresh ticket and a
     * brand-new stream.
     */
    void handleError(int readyState) {
        streamLive = false;
        if (closed) {
            // We closed the stream deliberately (project disposed); ignore the
            // error fired as the connection tears down.
            return;
        }
        if (readyState == READY_STATE_CLOSED) {
            logger.info("Event stream closed fatally; re-acquiring a ticket and reopening");
            acquireTicketAndOpen();
        } else {
            // The browser is retrying on its own, but arm the watchdog anyway:
            // if its retries never land (some browsers give up silently after a
            // network change) a full reopen takes over.
            armWatchdog();
        }
    }

    String getLastEventId() {
        return lastEventId;
    }

    // Package-visible rather than private so JVM unit tests can stub the JSNI
    // out (a native method cannot run off the browser); production keeps the
    // real EventSource wiring below.
    native void openEventSource(String url)/*-{
        try {
            var that = this;

            // If a previous connection is still around (e.g. a reopen after a
            // fatal error), close it rather than leaking it.
            var existing = this.@edu.stanford.bmir.protege.web.client.events.ProjectEventStreamManager::eventSource;
            if (existing) {
                existing.close();
            }

            var eventSource = new $wnd.EventSource(url);

            eventSource.addEventListener('project-events', function(event) {
                that.@edu.stanford.bmir.protege.web.client.events.ProjectEventStreamManager::handleMessage(Ljava/lang/String;Ljava/lang/String;)(event.data, event.lastEventId);
            });

            eventSource.onopen = function() {
                that.@edu.stanford.bmir.protege.web.client.events.ProjectEventStreamManager::handleOpen()();
            };

            eventSource.onerror = function() {
                that.@edu.stanford.bmir.protege.web.client.events.ProjectEventStreamManager::handleError(I)(eventSource.readyState);
            };

            this.@edu.stanford.bmir.protege.web.client.events.ProjectEventStreamManager::eventSource = eventSource;

        } catch (e) {
            $wnd.console.log('An error has occurred opening the event stream ' + e);
        }
    }-*/;

    native void closeEventSource()/*-{
        try {
            var eventSource = this.@edu.stanford.bmir.protege.web.client.events.ProjectEventStreamManager::eventSource;
            if (eventSource) {
                eventSource.close();
                this.@edu.stanford.bmir.protege.web.client.events.ProjectEventStreamManager::eventSource = null;
            }
        } catch (e) {
            $wnd.console.log('An error has occurred closing the event stream ' + e);
        }
    }-*/;
}
