package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
@AutoValue
public abstract class JsonRpcError {

    @JsonCreator
    public static JsonRpcError create(@JsonProperty("code") @JsonAlias("status") int code,
                                      @JsonProperty("message") @JsonAlias("error") String message) {
        return new AutoValue_JsonRpcError(code, message);
    }

    public abstract int getCode();

    public abstract String getMessage();
}
