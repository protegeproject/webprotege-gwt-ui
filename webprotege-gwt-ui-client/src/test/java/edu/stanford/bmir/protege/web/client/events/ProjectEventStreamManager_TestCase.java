package edu.stanford.bmir.protege.web.client.events;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Unit tests for the reconnect decision logic of {@link ProjectEventStreamManager}.
 * The JSNI EventSource wiring is stubbed out (it cannot run off the browser), so
 * these cover the pure-Java seam: the {@code onerror} readyState discrimination,
 * the {@code Last-Event-ID} tracking used to resume a replacement stream, and the
 * resume URL that carries it.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProjectEventStreamManager_TestCase {

    private static final ProjectId PROJECT_ID = ProjectId.getNil();

    @Mock
    private DispatchServiceManager dispatchServiceManager;

    @Mock
    private ProjectEventDispatcher dispatcher;

    private RecordingStreamManager manager;

    @Before
    public void setUp() {
        manager = new RecordingStreamManager(PROJECT_ID, dispatchServiceManager, dispatcher);
    }

    /**
     * A subclass that records reopen attempts and stubs the native EventSource
     * calls so the reconnect decision can be exercised entirely on the JVM.
     */
    private static class RecordingStreamManager extends ProjectEventStreamManager {

        int reopenCount = 0;

        int watchdogScheduleCount = 0;

        int livenessScheduleCount = 0;

        int closeCount = 0;

        double fakeNowMillis = 0;

        RecordingStreamManager(ProjectId projectId,
                               DispatchServiceManager dispatchServiceManager,
                               ProjectEventDispatcher dispatcher) {
            super(projectId, dispatchServiceManager, dispatcher);
        }

        @Override
        void acquireTicketAndOpen() {
            reopenCount++;
        }

        @Override
        void scheduleWatchdog(int delayMs) {
            // Recorded instead of scheduled: a GWT timer cannot run off-browser.
            watchdogScheduleCount++;
        }

        @Override
        void scheduleLiveness(int delayMs) {
            livenessScheduleCount++;
        }

        @Override
        double now() {
            return fakeNowMillis;
        }

        @Override
        void openEventSource(String url) {
            // stubbed: no browser
        }

        @Override
        void closeEventSource() {
            closeCount++;
        }
    }

    @Test
    public void shouldNotReopenWhileTheBrowserIsAutoReconnecting() {
        manager.handleError(ProjectEventStreamManager.READY_STATE_CONNECTING);

        // CONNECTING: the browser will resend Last-Event-ID itself; do nothing
        // immediately -- but the watchdog is armed in case its retries never land.
        assertThat(manager.reopenCount, is(0));
        assertThat(manager.watchdogScheduleCount, is(1));
    }

    @Test
    public void shouldRetryTheOpenWhenTheWatchdogFiresWithoutALiveStream() {
        // An open attempt died silently (e.g. the ticket mint failed offline).
        manager.handleError(ProjectEventStreamManager.READY_STATE_CONNECTING);

        manager.watchdogFired();

        assertThat(manager.reopenCount, is(1));
    }

    @Test
    public void shouldNotRetryOnceTheStreamIsLive() {
        manager.handleError(ProjectEventStreamManager.READY_STATE_CONNECTING);
        manager.handleOpen();

        manager.watchdogFired();

        assertThat(manager.reopenCount, is(0));
    }

    @Test
    public void shouldNotRetryAfterTheProjectIsDisposed() {
        manager.handleError(ProjectEventStreamManager.READY_STATE_CONNECTING);
        manager.stop();

        manager.watchdogFired();

        assertThat(manager.reopenCount, is(0));
    }

    @Test
    public void shouldReopenWhenALiveStreamGoesSilentPastTheHeartbeatWindow() {
        // The half-open zombie: the browser still reports the stream open, but a
        // network drop killed delivery without ever firing an error.
        manager.handleOpen();
        manager.fakeNowMillis = ProjectEventStreamManager.STALE_STREAM_THRESHOLD_MS + 1_000;

        manager.livenessCheckFired();

        assertThat(manager.closeCount, is(1));
        assertThat(manager.reopenCount, is(1));
    }

    @Test
    public void shouldKeepCheckingWhileHeartbeatsArrive() {
        manager.handleOpen();
        manager.fakeNowMillis = 40_000;
        manager.handleHeartbeat();
        manager.fakeNowMillis = 60_000;

        manager.livenessCheckFired();

        // Twenty seconds since the last heartbeat: healthy, just re-armed.
        assertThat(manager.reopenCount, is(0));
        assertThat(manager.livenessScheduleCount, is(2));
    }

    @Test
    public void shouldStopLivenessCheckingOnceDisposed() {
        manager.handleOpen();
        manager.stop();
        manager.fakeNowMillis = ProjectEventStreamManager.STALE_STREAM_THRESHOLD_MS + 1_000;

        manager.livenessCheckFired();

        assertThat(manager.reopenCount, is(0));
    }

    @Test
    public void shouldArmOnlyOneWatchdogAtATime() {
        manager.handleError(ProjectEventStreamManager.READY_STATE_CONNECTING);
        manager.handleError(ProjectEventStreamManager.READY_STATE_CONNECTING);

        assertThat(manager.watchdogScheduleCount, is(1));
    }

    @Test
    public void shouldReopenAfterAFatalError() {
        manager.handleError(ProjectEventStreamManager.READY_STATE_CLOSED);

        // CLOSED: EventSource will not retry after a non-2xx (e.g. expired
        // ticket -> 401), so we re-mint and open a fresh stream.
        assertThat(manager.reopenCount, is(1));
    }

    @Test
    public void shouldNotReopenAfterTheStreamHasBeenStopped() {
        manager.stop();

        manager.handleError(ProjectEventStreamManager.READY_STATE_CLOSED);

        // The stream was closed deliberately (project disposed); the teardown
        // error must not trigger a reopen.
        assertThat(manager.reopenCount, is(0));
    }

    @Test
    public void shouldTrackTheLastEventIdFromReceivedFrames() {
        assertThat(manager.getLastEventId(), is(nullValue()));

        manager.handleMessage("{}", "7");
        assertThat(manager.getLastEventId(), is("7"));

        manager.handleMessage("{}", "8");
        assertThat(manager.getLastEventId(), is("8"));
    }

    @Test
    public void shouldIgnoreABlankEventIdAndKeepThePreviousResumePosition() {
        manager.handleMessage("{}", "7");
        manager.handleMessage("{}", "");

        assertThat(manager.getLastEventId(), is("7"));
    }

    @Test
    public void shouldReanchorTheDispatcherWhenTheStreamConnects() {
        manager.handleOpen();

        org.mockito.Mockito.verify(dispatcher).start();
    }

    // ------------------------------------------------------------------
    // Resume URL: a fresh EventSource never sends the Last-Event-ID header,
    // so the position is carried as a query parameter on a manual reopen.
    // ------------------------------------------------------------------

    @Test
    public void shouldBuildAFreshStreamUrlWithoutALastEventId() {
        String url = ProjectEventStreamManager.buildStreamUrl(PROJECT_ID, "tkt-123", null);

        assertThat(url, containsString("/data/projects/" + PROJECT_ID.getId() + "/events"));
        assertThat(url, containsString("ticket=tkt-123"));
        assertThat(url, not(containsString("lastEventId")));
    }

    @Test
    public void shouldCarryTheLastEventIdOnAResumeUrl() {
        String url = ProjectEventStreamManager.buildStreamUrl(PROJECT_ID, "tkt-123", "42");

        assertThat(url, containsString("ticket=tkt-123"));
        assertThat(url, containsString("lastEventId=42"));
    }
}
