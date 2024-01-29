package edu.stanford.bmir.protege.web.server.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.session.UserToken;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.auth.AuthenticationResponse;
import edu.stanford.bmir.protege.web.shared.auth.PerformLoginAction;
import edu.stanford.bmir.protege.web.shared.auth.PerformLoginResult;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-14
 */
public class PerformLoginHandler {

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    @Inject
    public PerformLoginHandler(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Nonnull
    public PerformLoginResult performLogin(@Nonnull PerformLoginAction action,
                                           @Nonnull Consumer<UserToken> userTokenConsumer) {
        try {
            var body = objectMapper.writeValueAsString(action);
            var httpRequest = HttpRequest.newBuilder()
                       .uri(URI.create("http://localhost:8082/authenticate"))
                       .POST(HttpRequest.BodyPublishers.ofString(body))
                       .build();
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if(httpResponse.statusCode() == 200) {
                httpResponse.headers()
                        .firstValue("set-cookie")
                            .map(val -> val.substring("jsessionid=".length()))
                        .map(UserToken::create)
                        .ifPresent(userTokenConsumer);
                return objectMapper.readValue(httpResponse.body(), PerformLoginResult.class);
            }
            else {
                return getLoginFailResult();
            }
        } catch (InterruptedException | IOException e) {
            return getLoginFailResult();
        }
    }

    private static PerformLoginResult getLoginFailResult() {
        return PerformLoginResult.create(AuthenticationResponse.FAIL,
                                         new UserInSession(UserDetails.getGuestUserDetails(), Collections.emptySet()));
    }
}
