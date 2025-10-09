package edu.stanford.bmir.protege.web.shared.event;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.*;
import static edu.stanford.bmir.protege.web.MockingUtils.mockUserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
public class EventList_Serialization_TestCase {

    @Test
    public void shouldSerializeEvent() throws IOException {
        ImmutableList<WebProtegeEvent> events = ImmutableList.of(new ClassFrameChangedEvent(mockOWLClass(),
                                                                  mockProjectId(),
                                                                  mockUserId(), ChangeRequestId.valueOf("6681a7df-5c47-459e-9ffe-6742322c2a4f")));

        var eventList = EventList.create(EventTag.get(2), events, EventTag.get(3));
        JsonSerializationTestUtil.testSerialization(eventList, EventList.class);
    }
}
