package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static com.google.common.collect.ImmutableSet.of;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class CreateDataProperties_Serialization_TestCase {
    
    @Test
    public void shouldSerializeAction() throws IOException {
        var action = CreateDataPropertiesAction.create(ProjectId.getNil(),
                                                             "P\nQ",
                                                             "en", of(),
                ChangeRequestId.valueOf("45d17149-693b-4e84-bf1c-1ba3f95ff047"));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = CreateDataPropertiesResult.create(ProjectId.getNil(),
                                                             ImmutableSet.of(), ChangeRequestId.valueOf(UUID.randomUUID().toString()));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
