package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SingleChoiceControlDataDto")
public abstract class SingleChoiceControlDataDto implements FormControlDataDto {

    @JsonCreator
    @Nonnull
    public static SingleChoiceControlDataDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull SingleChoiceControlDescriptor descriptor,
                                                 @JsonProperty(PropertyNames.CHOICE) @Nullable PrimitiveFormControlDataDto choice,
                                                 @JsonProperty(PropertyNames.DEPTH) int depth) {
        return new AutoValue_SingleChoiceControlDataDto(depth, descriptor, choice);
    }

    @Nonnull
    @JsonProperty(PropertyNames.CONTROL)
    public abstract SingleChoiceControlDescriptor getDescriptor();

    @JsonProperty(PropertyNames.CHOICE)
    @Nullable
    protected abstract PrimitiveFormControlDataDto getChoiceInternal();

    @JsonIgnore
    @Nonnull
    public Optional<PrimitiveFormControlDataDto> getChoice() {
        return Optional.ofNullable(getChoiceInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public SingleChoiceControlData toFormControlData() {
        return SingleChoiceControlData.get(getDescriptor(),
                getChoice().map(PrimitiveFormControlDataDto::toPrimitiveFormControlData).orElse(null));
    }
}
