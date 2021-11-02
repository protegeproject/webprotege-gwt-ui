package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetAuthenticatedUserDetailsAction_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetAuthenticatedUserDetailsAction.create();
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetAuthenticatedUserDetailsResult.create(UserDetails.getGuestUserDetails(),
                                                                                          ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
