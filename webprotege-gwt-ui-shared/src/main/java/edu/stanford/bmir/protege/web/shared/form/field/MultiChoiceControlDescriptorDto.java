package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName("MultiChoiceControlDescriptorDto")
public abstract class MultiChoiceControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static MultiChoiceControlDescriptorDto get(@JsonProperty(PropertyNames.CHOICES_SOURCE) @Nonnull ChoiceListSourceDescriptor choiceListSourceDescriptor,
                                                      @JsonProperty(PropertyNames.CHOICES) @Nonnull ImmutableList<ChoiceDescriptorDto> choices) {
        return new AutoValue_MultiChoiceControlDescriptorDto(choiceListSourceDescriptor, choices);
    }

    @Nonnull
    @JsonProperty(PropertyNames.CHOICES_SOURCE)
    public abstract ChoiceListSourceDescriptor getChoiceListSourceDescriptor();

    @Nonnull
    @JsonProperty(PropertyNames.CHOICES)
    public abstract ImmutableList<ChoiceDescriptorDto> getAvailableChoices();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public MultiChoiceControlDescriptor toFormControlDescriptor() {
        return MultiChoiceControlDescriptor.get(
            getChoiceListSourceDescriptor(),
            ImmutableList.of()
        );
    }
}
