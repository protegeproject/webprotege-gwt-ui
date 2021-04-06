package edu.stanford.bmir.protege.web.shared.app;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class GetApplicationSettings_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetApplicationSettingsAction.create();
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetApplicationSettingsResult.create(new ApplicationSettings(
                "Name",
                new EmailAddress("Email"),
                new ApplicationLocation("scheme", "host", "path", 20),
                AccountCreationSetting.ACCOUNT_CREATION_ALLOWED,
                Collections.emptyList(),
                ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED,
                Collections.emptyList(),
                ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED,
                Collections.emptyList(),
                NotificationEmailsSetting.SEND_NOTIFICATION_EMAILS,
                300L
        ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
