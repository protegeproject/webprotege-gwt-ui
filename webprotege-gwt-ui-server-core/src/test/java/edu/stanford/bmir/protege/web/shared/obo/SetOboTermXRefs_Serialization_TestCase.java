package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetOboTermXRefs_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetOboTermXRefsAction.create(mockProjectId(),
                                                  mockOWLClass(), Collections.emptyList());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetOboTermXRefsResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}