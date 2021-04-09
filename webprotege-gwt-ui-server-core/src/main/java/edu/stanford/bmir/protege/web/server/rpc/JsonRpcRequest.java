package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
@AutoValue
public abstract class JsonRpcRequest {

    public static JsonRpcRequest create(Action action) {
        return new AutoValue_JsonRpcRequest(JsonRpcParams.create(action), UUID.randomUUID().toString());
    }

    @JsonProperty("jsonrpc")
    public final String getJsonRpc() {
        return "2.0";
    }

    @JsonProperty("method")
    public String getMethod() {
        return "executeAction";
    }

    @JsonProperty("params")
    public abstract JsonRpcParams getParams();

    @JsonProperty("id")
    public abstract String getId();
}
