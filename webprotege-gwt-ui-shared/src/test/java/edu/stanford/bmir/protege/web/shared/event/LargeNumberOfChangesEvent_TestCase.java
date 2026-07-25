package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Equality on this event drives the client's handled-events dedup set: it must
 * treat the SAME signal arriving over both delivery paths (push and poll) as
 * one, while distinct later signals for the same project must not be swallowed
 * — comparing the project alone made every prompt after the first vanish (#300).
 */
public class LargeNumberOfChangesEvent_TestCase {

    private static final ProjectId PROJECT_ID = ProjectId.getNil();

    @Test
    public void shouldBeEqualForTheSameEventId() {
        LargeNumberOfChangesEvent eventViaPush = new LargeNumberOfChangesEvent(EventId.get("event-a"), PROJECT_ID);
        LargeNumberOfChangesEvent eventViaPoll = new LargeNumberOfChangesEvent(EventId.get("event-a"), PROJECT_ID);
        assertThat(eventViaPush, is(equalTo(eventViaPoll)));
        assertThat(eventViaPush.hashCode(), is(eventViaPoll.hashCode()));
    }

    @Test
    public void shouldNotBeEqualForDistinctEventIdsInTheSameProject() {
        LargeNumberOfChangesEvent firstSignal = new LargeNumberOfChangesEvent(EventId.get("event-a"), PROJECT_ID);
        LargeNumberOfChangesEvent laterSignal = new LargeNumberOfChangesEvent(EventId.get("event-b"), PROJECT_ID);
        assertThat(firstSignal, is(not(equalTo(laterSignal))));
    }

    @Test
    public void shouldDedupTheSameSignalButKeepDistinctSignalsInAHandledSet() {
        Set<LargeNumberOfChangesEvent> handled = new HashSet<>();
        handled.add(new LargeNumberOfChangesEvent(EventId.get("event-a"), PROJECT_ID));

        assertThat(handled.contains(new LargeNumberOfChangesEvent(EventId.get("event-a"), PROJECT_ID)), is(true));
        assertThat(handled.contains(new LargeNumberOfChangesEvent(EventId.get("event-b"), PROJECT_ID)), is(false));
    }
}
