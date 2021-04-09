package edu.stanford.bmir.protege.web.server.dispatch.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcHttpRequestBuilder;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcHttpResponseHandler;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ActionExecutionException;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.rmi.activation.ActivateFailedException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 */
public class DispatchServiceExecutorImpl implements DispatchServiceExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DispatchServiceExecutorImpl.class.getName());

    public static final int HTTP_403_FORBIDDEN = 403;

    private final ObjectMapper objectMapper;

    private final UserInSessionFactory userInSessionFactory;

    private final HttpClient httpClient;

    private final JsonRpcHttpRequestBuilder requestBuilder;

    private final JsonRpcHttpResponseHandler responseHandler;

    @Inject
    public DispatchServiceExecutorImpl(ObjectMapper objectMapper,
                                       UserInSessionFactory userInSessionFactory,
                                       HttpClient httpClient,
                                       JsonRpcHttpRequestBuilder requestBuilder,
                                       JsonRpcHttpResponseHandler responseHandler) {
        this.objectMapper = objectMapper;
        this.userInSessionFactory = userInSessionFactory;
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.responseHandler = responseHandler;
    }

    @Override
    public <A extends Action<R>, R extends Result> DispatchServiceResultContainer execute(A action, RequestContext requestContext, ExecutionContext executionContext) throws ActionExecutionException, PermissionDeniedException {
        try {
            var result = sendRequest(action, executionContext.getUserId());
            return DispatchServiceResultContainer.create(result);
        }
        // Rethrow directly
        catch (PermissionDeniedException | ActionExecutionException e) {
            throw e;
        } catch (Exception e) {
            logger.error("An error occurred whilst executing an action", e);
            throw new ActionExecutionException(e);
        }
    }

    private <A extends Action<R>, R extends Result> R sendRequest(A action,
                                                                  UserId userId) throws IOException, InterruptedException {
        try {
            var httpRequest = requestBuilder.getHttpRequestForAction(action);
            var httpResponse = httpClient.send(httpRequest,
                                               HttpResponse.BodyHandlers.ofString());
            return responseHandler.getResultForResponse(httpResponse, userId);
        } catch (ConnectException e) {
            logger.error("Could not connect to action execution service", e);
            throw new ActionExecutionException(new Exception("Internal server error"));
        }
    }

}
