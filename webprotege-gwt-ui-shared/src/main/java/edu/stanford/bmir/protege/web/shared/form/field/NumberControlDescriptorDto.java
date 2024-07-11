package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName("NumberControlDescriptorDto")
public abstract class NumberControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static NumberControlDescriptorDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull NumberControlDescriptor descriptor) {
        return new AutoValue_NumberControlDescriptorDto(descriptor);
    }

    @Nonnull
    @JsonProperty(PropertyNames.CONTROL)
    public abstract NumberControlDescriptor getDescriptor();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public FormControlDescriptor toFormControlDescriptor() {
        return getDescriptor();
    }


    @JsonIgnore
    public String getFormat() {
        return getDescriptor().getFormat();
    }

    @JsonIgnore
    public NumberControlRange getRange() {
        return getDescriptor().getRange();
    }

    @JsonIgnore
    public LanguageMap getPlaceholder() {
        return getDescriptor().getPlaceholder();
    }

    @JsonIgnore
    public int getLength() {
        return getDescriptor().getLength();
    }
}
