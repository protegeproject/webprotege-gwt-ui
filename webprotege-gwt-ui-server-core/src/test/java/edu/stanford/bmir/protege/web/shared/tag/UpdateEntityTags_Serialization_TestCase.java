package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class UpdateEntityTags_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = UpdateEntityTagsAction.create(ChangeRequestId.get("12345678-1234-1234-1234-123456789abc"),
                                                   mockProjectId(),
                                                   mockOWLClass(),
                                                   ImmutableSet.of(TagId.createTagId()),
                                                   ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = UpdateEntityTagsResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}