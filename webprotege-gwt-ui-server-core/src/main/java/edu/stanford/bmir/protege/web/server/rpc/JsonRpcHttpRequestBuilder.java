package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceExecutorImpl;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.net.http.HttpRequest;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-09
 */
public class JsonRpcHttpRequestBuilder {
    private static final Logger logger = LoggerFactory.getLogger(JsonRpcHttpRequestBuilder.class.getName());

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPLICATION_JSON = "application/json";

    private final ObjectMapper objectMapper;

    private final JsonRpcEndPoint jsonRpcEndPoint;

    @Inject
    public JsonRpcHttpRequestBuilder(@Nonnull ObjectMapper objectMapper,
                                     @Nonnull JsonRpcEndPoint jsonRpcEndPoint) {
        this.objectMapper = checkNotNull(objectMapper);
        this.jsonRpcEndPoint = checkNotNull(jsonRpcEndPoint);
    }

    public  <A extends Action<R>, R extends Result> HttpRequest getHttpRequestForAction(A action,
                                                                                        ExecutionContext executionContext) throws com.fasterxml.jackson.core.JsonProcessingException {
        var jsonRpcRequest = JsonRpcRequest.create(action);
        var requestBody = objectMapper.writeValueAsString(jsonRpcRequest);
        var builder = HttpRequest.newBuilder()
                          .uri(jsonRpcEndPoint.getUri())
                          .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                          .setHeader(CONTENT_TYPE, APPLICATION_JSON);
        var jwt = executionContext.getToken();
        builder.setHeader("Authorization", "Bearer " + jwt);
        logger.info("ALEX calling with request " + requestBody + " " + executionContext.getToken());

        return builder.build();
    }

    public JsonRpcEndPoint getJsonRpcEndPoint() {
        return jsonRpcEndPoint;
    }
}
