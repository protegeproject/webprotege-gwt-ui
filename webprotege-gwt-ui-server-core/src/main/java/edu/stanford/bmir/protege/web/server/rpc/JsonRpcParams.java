package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
@AutoValue
public abstract class JsonRpcParams {

    public static JsonRpcParams create(Action action) {
        return new AutoValue_JsonRpcParams(action);
    }

    public abstract Action getAction();
}
