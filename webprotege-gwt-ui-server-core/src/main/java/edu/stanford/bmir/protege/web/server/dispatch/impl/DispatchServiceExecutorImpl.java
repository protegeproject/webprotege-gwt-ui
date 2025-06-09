package edu.stanford.bmir.protege.web.server.dispatch.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.client.rpc.StatusCodeException;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.rpc.*;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import org.slf4j.*;

import javax.inject.Inject;
import java.io.IOException;
import java.net.ConnectException;
import java.net.http.*;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final ObjectMapper objectMapper;

    @Inject
    public DispatchServiceExecutorImpl(HttpClient httpClient,
                                       JsonRpcHttpRequestBuilder requestBuilder,
                                       JsonRpcHttpResponseHandler responseHandler) {
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.responseHandler = responseHandler;
        this.objectMapper = new ObjectMapperProvider().get();
    }

    @Override
    public <A extends Action<R>, R extends Result> DispatchServiceResultContainer execute(A action, ExecutionContext executionContext) throws ActionExecutionException, PermissionDeniedException {
        try {
            if (action instanceof TranslateEventListAction) {
                var translateEventsAction = (TranslateEventListAction) action;
                try {
                    GetProjectEventsResult<?> result = objectMapper.readValue(translateEventsAction.getEventList(), GetProjectEventsResult.class);
                    return DispatchServiceResultContainer.create(result);
                } catch (JsonProcessingException e) {
                    logger.error("Error when translating event list", e);
                    logger.error(translateEventsAction.getEventList());
                    throw new ActionExecutionException("An error occurred when translating the given event list.  See logs for more information.");
                }
            }
            if (action instanceof GetUserInfoAction) {
                GetUserInfoResult result = GetUserInfoResult.create(executionContext.getToken());
                return DispatchServiceResultContainer.create(result);
            }
            if (action instanceof FetchAppEnvVariables) {
                var websocketUrl = getEnvVariable("webprotege.websocketUrl").orElse("ws://webprotege-local.edu/wsapps");
                var logoutUrl = getEnvVariable("webprotege.logoutUrl").orElse("http://webprotege-local.edu/webprotege/logout");
                var redirectUrl = getEnvVariable("webprotege.logoutRedirectUrl").orElse("http://webprotege-local.edu/webprotege");
                var fileUploadUrl = getEnvVariable("webprotege.fileUploadurl").orElse("http://webprotege-local.edu");
                var historyFormat = getEnvVariable("webprotege.entityHistoryUrlFormat").orElse("https://icat-history.azurewebsites.net/changes/{0}.html");
                var notesFormat   = getEnvVariable("webprotege.entityNotesUrlFormat").orElse("https://icat-history.azurewebsites.net/notes/{0}.html");
                var oldHistoryAndNotesDate = getEnvVariable("webprotege.entityOldHistoryAndNotesDate").orElse("May 1st, 2025");
                AppEnvVariables result = AppEnvVariables.create(logoutUrl, websocketUrl, redirectUrl, fileUploadUrl, historyFormat, notesFormat, oldHistoryAndNotesDate);
                return DispatchServiceResultContainer.create(result);
            }
            var result = sendRequest(action, executionContext);
            return DispatchServiceResultContainer.create(result);
        }
        // Rethrow directly
        catch (PermissionDeniedException e) {
            logger.error("An exception was thrown when executing {} {}", action.getClass().getName(), e.getMessage(), e);
            throw e;
        } catch (ActionExecutionException e) {
            throw new ActionExecutionException(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occurred whilst executing an action", e);
            throw new ActionExecutionException(e.getMessage());
        }
    }

    private <A extends Action<R>, R extends Result> R sendRequest(A action,
                                                                  ExecutionContext executionContext) throws IOException, InterruptedException {
        try {
            var httpRequest = requestBuilder.getHttpRequestForAction(action, executionContext);
            var httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            var userId = executionContext.getUserId();
            logger.info("DispatchServiceExecutorImpl: Action: {} Response.StatusCode: {} Response: {}", action, httpResponse.statusCode(), httpResponse.body());

            // Special handling for FORBIDDEN 403.  This is handled by a PermissionDeniedException.
            // We should consider whether we want to change this
            if(httpResponse.statusCode() == HTTP_403_FORBIDDEN) {
                var headers = httpResponse.headers()
                        .map()
                        .entrySet()
                        .stream()
                        .map(e -> e.getKey() + ": " + e.getValue())
                        .collect(Collectors.joining("  &&  "));
                var reason = httpResponse.headers().firstValue("www-authenticate");
                logger.info("Permission denied for {} , reason {},  when executing {}.  User: {}, Headers: {}, Token: {}", executionContext.getUserId(),
                        reason,
                        action.getClass().getSimpleName(),
                        executionContext.getUserId(),
                        headers,
                        executionContext.getToken());
                throw new PermissionDeniedException("Permission denied (" + httpResponse.statusCode() + ")", executionContext.getUserId());
            } else if (httpResponse.statusCode() == 504) {
                logger.error("Gateway timeout when executing action: {} {}", action.getClass().getSimpleName(), httpResponse.body());
                throw new ActionExecutionException("Gateway Timeout (504)");
            }
            else if(httpResponse.statusCode() >= 400) {
                logger.error("Error response code returned when executing action.  Check the API gateway service for potential logs.  Details: {} {}", action.getClass().getSimpleName(), httpResponse.body());
                if(action instanceof BatchAction) {
                    ((BatchAction) action).getActions()
                            .stream()
                            .map(a -> a.getClass().getSimpleName())
                            .forEach(a -> logger.error("    Nested action: {}", a.getClass().getSimpleName()));
                }
                throw new StatusCodeException(httpResponse.statusCode(), httpResponse.body());
            }
            return responseHandler.getResultForResponse(action, httpResponse, userId);
        } catch (ConnectException e) {
            logger.error("Could not connect to API Gateway at {}", requestBuilder.getJsonRpcEndPoint().getUri(), e);
            throw new ActionExecutionException("Internal Server Error");
        }
    }

    private Optional<String> getEnvVariable(String path) {
        String env = System.getenv(path);
        return Optional.ofNullable(env);
    }
}
