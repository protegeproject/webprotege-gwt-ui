package edu.stanford.bmir.protege.web.server.session;

import javax.annotation.Nonnull;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
public class WebProtegeSessionFactory {

    @Nonnull
    public static WebProtegeSession getSession(@Nonnull HttpServletRequest httpServletRequest) {
        var httpSession = httpServletRequest.getSession();
        var userToken = getUserToken(httpServletRequest);
        return new WebProtegeSessionImpl(httpSession, userToken.orElse(null));
    }


    private static Optional<UserToken> getUserToken(HttpServletRequest httpServletRequest) {
        var cookies = httpServletRequest.getCookies();
        return Arrays.stream(cookies != null ? cookies : new Cookie[0])
                     .filter(cookie -> cookie.getName().equalsIgnoreCase(UserToken.COOKIE_NAME))
                     .map(cookie -> UserToken.create(cookie.getValue()))
                     .findFirst();
    }

}
