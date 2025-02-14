package edu.stanford.bmir.protege.web.shared.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class MoveEntitiesToParent_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = MoveEntitiesToParentAction.create(mockProjectId(), ImmutableSet.of(),
                                                       mockOWLClass(),
                                                       "Test");
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = MoveEntitiesToParentResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}