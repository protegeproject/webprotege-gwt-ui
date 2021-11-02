package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
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


    @Inject
    public JsonRpcHttpResponseHandler(@Nonnull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <R extends Result> R getResultForResponse(Action action,
                                                     HttpResponse<String> httpResponse,
                                                     UserId userId) throws PermissionDeniedException, ActionExecutionException {
        try {
            if(httpResponse.statusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new PermissionDeniedException(userId);
            }
            if(httpResponse.statusCode() != 200) {
                throw new ActionExecutionException("Internal Server Error (" + httpResponse.statusCode() + ")");
            }

            var responseBody = httpResponse.body();
            var jsonRpcResponse = objectMapper.readValue(responseBody, JsonRpcResponse.class);
            if(jsonRpcResponse.getError().isPresent()) {
                var error = jsonRpcResponse.getError().get();
                logger.error("An exception occurred when executing an action ({}): {} Code: {}", action.getClass().getSimpleName(), error.getMessage(), error.getCode());
                if(error.getCode() == HTTP_403_FORBIDDEN) {
                    throw new PermissionDeniedException(error.getMessage(),
                                                        userId);
                }
                else {
                    throw new ActionExecutionException(error.getMessage() + "(" + error.getCode() + ")");
                }
            }
            return (R) jsonRpcResponse.getResult().get();
        } catch (JsonProcessingException e) {
            logger.error("Error whilst deserializing response.  Body: {}", httpResponse.body(), e);
            throw new ActionExecutionException("Internal server error (JSON Deserialization Error)");
        }
    }
}
