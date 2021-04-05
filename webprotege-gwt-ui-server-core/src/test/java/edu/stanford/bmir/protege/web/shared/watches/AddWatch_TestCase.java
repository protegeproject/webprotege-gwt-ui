package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-05
 */
public class AddWatch_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = AddWatchAction.create(ProjectId.getNil(),
                              UserId.getGuest(),
                              Watch.create(UserId.getGuest(), MockingUtils.mockOWLClass(),
                                           WatchType.ENTITY));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }
}
