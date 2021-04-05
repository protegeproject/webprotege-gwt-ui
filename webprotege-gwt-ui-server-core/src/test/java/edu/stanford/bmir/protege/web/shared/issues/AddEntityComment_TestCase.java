package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-05
 */
public class AddEntityComment_TestCase {

    private static final String THE_COMMENT = "The comment";

    @Test
    public void shouldSerializeAction() throws IOException {
        var projectId = ProjectId.getNil();
        var threadId = ThreadId.create();
        var action = AddEntityCommentAction.addComment(projectId,
                                                threadId, THE_COMMENT);
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }
}
