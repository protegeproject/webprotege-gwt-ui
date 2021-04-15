package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.GetClassFrameResult;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
public class GetClassFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetClassFrameAction.create(mockOWLClass(), mockProjectId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetClassFrameResult.get(ClassFrame.get(
                mockOWLClassData(),
                ImmutableSet.of(), ImmutableSet.of()
        ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}