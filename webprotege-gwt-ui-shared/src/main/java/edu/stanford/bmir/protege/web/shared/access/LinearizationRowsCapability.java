package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("LinearizationRowsCapability")
public abstract class LinearizationRowsCapability implements ContextSensitiveCapability {


    @JsonProperty("linearizationIds")
    public abstract List<String> getLinearizationIds();

    @JsonCreator
    public static LinearizationRowsCapability get(@JsonProperty("id") CapabilityId id,
                                                  @JsonProperty("linearizationIds") List<String> linearizationIds,
                                                  @JsonProperty("contextCriteria") CompositeRootCriteria contextCriteria) {
        return new AutoValue_LinearizationRowsCapability(id,
                (contextCriteria != null ? contextCriteria : CompositeRootCriteria.any()),
                linearizationIds);
    }
}
