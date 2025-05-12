package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class DeleteEntities_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = new DeleteEntitiesAction(
                ChangeRequestId.get(UUID.randomUUID().toString()),
                ProjectId.getNil(),
                ImmutableSet.of()
        );
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = new DeleteEntitiesResult(ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
