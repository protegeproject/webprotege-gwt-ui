package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.field.MultiChoiceControlDescriptor;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableList.toImmutableList;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("MultiChoiceControlDataDto")
public abstract class MultiChoiceControlDataDto implements FormControlDataDto {

    @JsonCreator
    @Nonnull
    public static MultiChoiceControlDataDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull MultiChoiceControlDescriptor descriptor,
                                                @JsonProperty(PropertyNames.VALUES) @Nonnull ImmutableList<PrimitiveFormControlDataDto> values,
                                                @JsonProperty(PropertyNames.DEPTH) int depth) {
        return new AutoValue_MultiChoiceControlDataDto(depth, descriptor, values);
    }

    @JsonProperty(PropertyNames.CONTROL)
    @Nonnull
    public abstract MultiChoiceControlDescriptor getDescriptor();

    @JsonProperty(PropertyNames.VALUES)
    @Nonnull
    public abstract ImmutableList<PrimitiveFormControlDataDto> getValues();

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public MultiChoiceControlData toFormControlData() {
        return MultiChoiceControlData.get(getDescriptor(),
                getValues().stream().map(PrimitiveFormControlDataDto::toPrimitiveFormControlData).collect(toImmutableList()));
    }
}
