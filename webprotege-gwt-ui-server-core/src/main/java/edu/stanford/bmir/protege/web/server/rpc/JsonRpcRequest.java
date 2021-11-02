package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
@AutoValue
@JsonPropertyOrder({"id", "jsonrpc", "method", "params"})
public abstract class JsonRpcRequest {

    public static JsonRpcRequest create(Action action) {
        return new AutoValue_JsonRpcRequest(action, UUID.randomUUID().toString());
    }

    @JsonProperty("jsonrpc")
    public final String getJsonRpc() {
        return "2.0";
    }

    @JsonProperty("params")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "method")
    public abstract Action getParams();

    @JsonProperty("id")
    public abstract String getId();
}
