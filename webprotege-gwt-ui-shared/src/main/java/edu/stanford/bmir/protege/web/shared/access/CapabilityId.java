package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class CapabilityId implements Comparable<CapabilityId> {

    @JsonCreator
    public static CapabilityId valueOf(String capabilityId) {
        return new AutoValue_CapabilityId(capabilityId);
    }

    @JsonValue
    public abstract String getId();

    @Override
    public int compareTo(CapabilityId o) {
        return this.getId().compareTo(o.getId());
    }
}
