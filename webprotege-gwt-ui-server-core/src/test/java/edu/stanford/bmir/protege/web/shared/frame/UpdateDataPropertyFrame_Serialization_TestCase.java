package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class UpdateDataPropertyFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = UpdateDataPropertyFrameAction.create(ChangeRequestId.get(UUID.randomUUID().toString()),
                                                            mockProjectId(),
                                                          PlainDataPropertyFrame.get(
                                                                  mockOWLDataProperty(), ImmutableSet.of(),
                                                                  ImmutableSet.of(),
                                                                  ImmutableSet.of(),
                                                                  true
                                                          ),
                                                          PlainDataPropertyFrame.get(
                                                                  mockOWLDataProperty(),
                                                                  ImmutableSet.of(),
                                                                  ImmutableSet.of(),
                                                                  ImmutableSet.of(),
                                                                  true
                                                          ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }
}