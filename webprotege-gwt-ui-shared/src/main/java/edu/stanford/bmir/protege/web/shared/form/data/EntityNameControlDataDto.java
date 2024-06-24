package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.field.EntityNameControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EntityNameControlDataDto")
public abstract class EntityNameControlDataDto implements FormControlDataDto {

    public EntityNameControlDataDto(){

    }

    @JsonCreator
    public static EntityNameControlDataDto get(@JsonProperty("descriptor") @Nonnull EntityNameControlDescriptor descriptor,
                                               @JsonProperty("entity") @Nonnull OWLEntityData entityData,
                                               @JsonProperty("depth") int depth) {
        return new AutoValue_EntityNameControlDataDto(depth, descriptor, entityData);
    }

    @JsonProperty("descriptor")
    @Nonnull
    public abstract EntityNameControlDescriptor getDescriptor();

    @Nullable
    @JsonProperty("entity")
    protected abstract OWLEntityData getEntityInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLEntityData> getEntity() {
        return Optional.ofNullable(getEntityInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public FormControlData toFormControlData() {
        return EntityNameControlData.get(getDescriptor(), getEntity().map(OWLEntityData::getEntity).orElse(null));
    }
}
