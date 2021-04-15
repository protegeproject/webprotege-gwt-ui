package edu.stanford.bmir.protege.web.server.session;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-14
 */
@AutoValue
public abstract class UserToken {

    public static String COOKIE_NAME = "webprotege_usertoken";

    public abstract String getToken();

    @Nonnull
    public static UserToken create(String token) {
        return new AutoValue_UserToken(token);
    }
}
