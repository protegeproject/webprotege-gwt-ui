package edu.stanford.bmir.protege.web.server.rpc;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;
import java.net.URI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-09
 */
@AutoValue
public abstract class JsonRpcEndPoint {

    public static JsonRpcEndPoint get(@Nonnull URI uri) {
        return new AutoValue_JsonRpcEndPoint(uri);
    }

    public abstract URI getUri();
}
