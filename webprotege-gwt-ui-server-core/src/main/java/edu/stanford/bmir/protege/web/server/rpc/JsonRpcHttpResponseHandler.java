package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.net.http.HttpResponse;

import static edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl.HTTP_403_FORBIDDEN;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-09
 */
public class JsonRpcHttpResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(JsonRpcHttpResponseHandler.class);

    @Nonnull
    private final ObjectMapper objectMapper;

    @Nonnull
    private final UserInSessionFactory userInSessionFactory;


    @Inject
    public JsonRpcHttpResponseHandler(@Nonnull ObjectMapper objectMapper,
                                      @Nonnull UserInSessionFactory userInSessionFactory) {
        this.objectMapper = objectMapper;
        this.userInSessionFactory = userInSessionFactory;
    }

    public <R extends Result> R getResultForResponse(HttpResponse<String> httpResponse,
                                                                          UserId userId) throws PermissionDeniedException, ActionExecutionException {
        try {
            if(httpResponse.statusCode() != 200) {
                throw new ActionExecutionException(new Exception("Internal Server Error: HTTP " + httpResponse.statusCode()));
            }

            var responseBody = httpResponse.body();
            var jsonRpcResponse = objectMapper.readValue(responseBody, JsonRpcResponse.class);
            if(jsonRpcResponse.getError().isPresent()) {
                var error = jsonRpcResponse.getError().get();
                if(error.getCode() == HTTP_403_FORBIDDEN) {
                    var userInSession = userInSessionFactory.getUserInSession(userId);
                    throw new PermissionDeniedException(error.getMessage(),
                                                        userInSession);
                }
                else {
                    throw new ActionExecutionException(new Exception(error.getMessage()));
                }
            }
            return (R) jsonRpcResponse.getResult().get();
        } catch (JsonProcessingException e) {
            logger.error("Error whilst deserializing response", e);
            throw new ActionExecutionException(new Exception("Internal server error"));
        }
    }
}
