package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Nonnull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, defaultImpl = GenericParameterizedCapability.class)
@JsonSubTypes({
        @JsonSubTypes.Type(BasicCapability.class),
        @JsonSubTypes.Type(GenericParameterizedCapability.class),
        @JsonSubTypes.Type(FormRegionCapability.class),
        @JsonSubTypes.Type(LinearizationRowsCapability.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Capability {

    /**
     * Gets the identifier of this capability
     * @return A string that represents the identifier of this capability
     */
    @JsonProperty("id")
    @Nonnull
    CapabilityId getId();
}
