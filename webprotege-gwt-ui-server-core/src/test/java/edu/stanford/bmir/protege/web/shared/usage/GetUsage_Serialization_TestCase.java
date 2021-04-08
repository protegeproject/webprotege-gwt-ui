package edu.stanford.bmir.protege.web.shared.usage;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetUsage_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetUsageAction.create(mockOWLClass(), mockProjectId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetUsageResult.create(mockProjectId(), mockOWLClassNode(), Collections.emptySet(), 22);
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}