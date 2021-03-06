package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class EntityDiscussionThread_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        var thread = new EntityDiscussionThread(ThreadId.create(),
                                                ProjectId.getNil(), MockingUtils.mockOWLClass(),
                                                Status.CLOSED, ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(thread, EntityDiscussionThread.class);
    }
}
