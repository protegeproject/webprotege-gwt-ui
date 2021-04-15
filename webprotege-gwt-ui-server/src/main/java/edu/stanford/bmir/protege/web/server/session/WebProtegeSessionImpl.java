package edu.stanford.bmir.protege.web.server.session;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class WebProtegeSessionImpl implements WebProtegeSession {

    private final HttpSession httpSession;

    @Nullable
    private UserToken userToken;

    @Inject
    public WebProtegeSessionImpl(@Nonnull HttpSession httpSession,
                                 @Nullable UserToken userToken) {
        this.httpSession = checkNotNull(httpSession);
        this.userToken = userToken;
    }


    @Override
    public void removeAttribute(WebProtegeSessionAttribute<?> attribute) {
        httpSession.removeAttribute(checkNotNull(attribute.getAttributeName()));
    }

    @Override
    public <T> void setAttribute(WebProtegeSessionAttribute<T> attribute, T value) {
        httpSession.setAttribute(checkNotNull(attribute.getAttributeName()), checkNotNull(value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAttribute(WebProtegeSessionAttribute<T> attribute) {
        T value = (T) httpSession.getAttribute(attribute.getAttributeName());
        return Optional.ofNullable(value);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("WebProtegeSession")
                          .addValue(httpSession)
                          .toString();
    }

    @Override
    public UserId getUserInSession() {
        return getAttribute(WebProtegeSessionAttribute.LOGGED_IN_USER).orElse(UserId.getGuest());
    }

    @Override
    public void setUserInSession(UserId userId) {
        if (!userId.isGuest()) {
            setAttribute(WebProtegeSessionAttribute.LOGGED_IN_USER, checkNotNull(userId));
        }
    }

    @Override
    public void clearUserInSession() {
        removeAttribute(WebProtegeSessionAttribute.LOGGED_IN_USER);
    }

    @Override
    public Optional<UserToken> getUserSessionToken() {
        return Optional.ofNullable(userToken);
    }
}
