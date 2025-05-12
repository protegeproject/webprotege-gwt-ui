package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("FormRegionCapability")
public abstract class FormRegionCapability implements ContextSensitiveCapability {

    @JsonProperty("formRegionId")
    public abstract FormRegionId getFormRegionId();

    @JsonCreator
    public static FormRegionCapability get(@JsonProperty("id") CapabilityId id,
                                           @JsonProperty("formRegionId") FormRegionId formRegionId,
                                           @JsonProperty("contextCriteria") CompositeRootCriteria contextCriteria) {
        return new AutoValue_FormRegionCapability(id,
                (contextCriteria != null ? contextCriteria : CompositeRootCriteria.any()),
                formRegionId);
    }

}
