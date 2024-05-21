package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-11
 */
@GwtCompatible(serializable = true)
public enum GitHubState implements IsSerializable {

    @JsonProperty("open")
    OPEN,

    @JsonProperty("closed")
    CLOSED
}
