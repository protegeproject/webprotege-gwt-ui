package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("LinearizationResidualsCapability")
public abstract class LinearizationResidualsCapability implements ContextSensitiveCapability {

    @JsonCreator
    public static LinearizationResidualsCapability get(@JsonProperty("id") CapabilityId id,
                                                       @JsonProperty("contextCriteria") CompositeRootCriteria contextCriteria) {
        return new AutoValue_LinearizationResidualsCapability(id,
                (contextCriteria != null ? contextCriteria : CompositeRootCriteria.any()));
    }
}
