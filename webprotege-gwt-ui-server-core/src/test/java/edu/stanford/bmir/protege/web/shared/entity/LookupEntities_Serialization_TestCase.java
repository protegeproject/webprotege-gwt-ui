package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class LookupEntities_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = LookupEntitiesAction.create(mockProjectId(),
                                                 new EntityLookupRequest("Search"));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = LookupEntitiesResult.create(Collections.emptySet());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}