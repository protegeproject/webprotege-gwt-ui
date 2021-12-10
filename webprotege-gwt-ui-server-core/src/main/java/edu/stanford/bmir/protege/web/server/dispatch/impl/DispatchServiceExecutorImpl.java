package edu.stanford.bmir.protege.web.server.dispatch.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcHttpRequestBuilder;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcHttpResponseHandler;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 */
public class DispatchServiceExecutorImpl implements DispatchServiceExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DispatchServiceExecutorImpl.class.getName());

    public static final int HTTP_403_FORBIDDEN = 403;

    private final HttpClient httpClient;

    private final JsonRpcHttpRequestBuilder requestBuilder;

    private final JsonRpcHttpResponseHandler responseHandler;

    @Inject
    public DispatchServiceExecutorImpl(HttpClient httpClient,
                                       JsonRpcHttpRequestBuilder requestBuilder,
                                       JsonRpcHttpResponseHandler responseHandler) {
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.responseHandler = responseHandler;
    }

    @Override
    public <A extends Action<R>, R extends Result> DispatchServiceResultContainer execute(A action, ExecutionContext executionContext) throws ActionExecutionException, PermissionDeniedException {
        try {
            var result = sendRequest(action, executionContext);
            return DispatchServiceResultContainer.create(result);
        }
        // Rethrow directly
        catch (PermissionDeniedException e) {
            logger.error("An exception was thrown when executing {} {}", action.getClass().getName(), e.getMessage(), e);
            throw e;
        }
        catch(ActionExecutionException e) {
            throw new ActionExecutionException(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred whilst executing an action", e);
            throw new ActionExecutionException(e.getMessage());
        }
    }

    private <A extends Action<R>, R extends Result> R sendRequest(A action,
                                                                  ExecutionContext executionContext) throws IOException, InterruptedException {
        // Workaround
        if(action instanceof GetProjectEventsAction) {
            return (R) GetProjectEventsResult.create(EventList.create(((GetProjectEventsAction) action).getSinceTag(),
                                                                  ImmutableList.of(),
                                                                  EventTag.get(100)));
        }

        try {
            var httpRequest = requestBuilder.getHttpRequestForAction(action, executionContext);

            var httpResponse = httpClient.send(httpRequest,
                                               HttpResponse.BodyHandlers.ofString());

            var userId = executionContext.getUserId();
            if(httpResponse.statusCode() == 400) {
                logger.error("Bad request when executing action: {} {}", action.getClass().getSimpleName(), httpResponse.body());
                if(action instanceof BatchAction) {
                    ((BatchAction) action).getActions()
                                          .stream()
                                          .map(a -> a.getClass().getSimpleName())
                                          .forEach(a -> logger.error("    Nested action: {}", a));
                }
            }
            else if(httpResponse.statusCode() == 401) {
                logger.info("Permission denied for {} when executing {}", executionContext.getUserId(), action.getClass().getSimpleName());
                throw new PermissionDeniedException("Permission denied", executionContext.getUserId());
            }
            else if(httpResponse.statusCode() == 504) {
                logger.error("Gateway timeout when executing action: {} {}", action.getClass().getSimpleName(), httpResponse.body());
                throw new ActionExecutionException("Gateway Timeout (504)");
            }
            return responseHandler.getResultForResponse(action, httpResponse, userId);
        } catch (ConnectException e) {
            logger.error("Could not connect to API Gateway at {}", requestBuilder.getJsonRpcEndPoint().getUri(), e);
            throw new ActionExecutionException("Internal Server Error");
        }
    }

}
