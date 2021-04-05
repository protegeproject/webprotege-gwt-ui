package edu.stanford.bmir.protege.web.shared.tag;

import edu.stanford.bmir.protege.web.shared.color.Color;
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
public class AddProjectTag_TestCase {

    @Test
    public void shouldSerializeAddProjectTagAction() throws IOException {
        var action = AddProjectTagAction.create(ProjectId.getNil(),
                                                "TheLabel",
                                                "TheDescription",
                                                Color.getWhite(),
                                                Color.get(240, 230, 50));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }
}
