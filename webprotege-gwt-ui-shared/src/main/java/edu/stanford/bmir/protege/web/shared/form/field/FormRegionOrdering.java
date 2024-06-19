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
                                         @JsonProperty(PropertyNames.ORDERING) @Nonnull FormRegionOrderingDirection direction) {
        return new AutoValue_FormRegionOrdering(formRegionId, direction);
    }

    @JsonProperty(PropertyNames.REGION_ID)
    @Nonnull
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = FormFieldId.class),
            @JsonSubTypes.Type(value = GridColumnId.class)
    })
    public abstract FormRegionId getRegionId();

    @JsonProperty(PropertyNames.ORDERING)
    @Nonnull
    public abstract FormRegionOrderingDirection getDirection();

    @JsonIgnore
    public boolean isAscending() {
        return getDirection().equals(FormRegionOrderingDirection.ASC);
    }
}
