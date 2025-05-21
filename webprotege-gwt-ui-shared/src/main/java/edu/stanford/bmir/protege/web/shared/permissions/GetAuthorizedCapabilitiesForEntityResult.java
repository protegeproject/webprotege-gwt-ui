package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static edu.stanford.bmir.protege.web.shared.permissions.GetAuthorizedCapabilitiesForEntityAction.CHANNEL;

@JsonTypeName(CHANNEL)
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetAuthorizedCapabilitiesForEntityResult implements Result {

    @JsonCreator
    public static GetAuthorizedCapabilitiesForEntityResult create(@JsonProperty("capabilities") ImmutableSet<Capability> capabilities) {
        return new AutoValue_GetAuthorizedCapabilitiesForEntityResult(capabilities);
    }

    @JsonProperty("capabilities")
    public abstract ImmutableSet<Capability> getCapabilities();
}
