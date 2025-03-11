package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.annotation.Nonnull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(BasicCapability.class)
})
public interface Capability {

    /**
     * Gets the identifier of this capability
     * @return A string that represents the identifier of this capability
     */
    @JsonProperty
    @Nonnull
    String getId();
}
