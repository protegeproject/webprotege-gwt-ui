package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("FormRegionCapability")
public abstract class FormRegionCapability implements Capability {

    @JsonProperty("id")
    public abstract String getId();

    @JsonProperty("formRegionId")
    public abstract FormRegionId getFormRegionId();

    @JsonCreator
    public static FormRegionCapability get(@JsonProperty("id") String id,
                                           @JsonProperty("formRegionId") FormRegionId formRegionId) {
        return new AutoValue_FormRegionCapability(id, formRegionId);
    }

}
