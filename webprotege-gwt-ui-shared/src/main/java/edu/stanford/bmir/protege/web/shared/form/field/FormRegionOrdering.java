package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormRegionOrdering {

    @JsonCreator
    @Nonnull
    public static FormRegionOrdering get(@JsonProperty(PropertyNames.REGION_ID) @Nonnull FormRegionId formRegionId,
                                         @JsonProperty(PropertyNames.DIRECTION) @Nonnull FormRegionOrderingDirection direction) {
        return new AutoValue_FormRegionOrdering(formRegionId, direction);
    }

    @JsonProperty(PropertyNames.REGION_ID)
    @Nonnull
    public abstract FormRegionId getRegionId();

    @JsonProperty(PropertyNames.DIRECTION)
    @Nonnull
    public abstract FormRegionOrderingDirection getDirection();

    @JsonIgnore
    public boolean isAscending() {
        return getDirection().equals(FormRegionOrderingDirection.ASC);
    }
}
