package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class ChoiceDescriptorDto {

    @JsonCreator
    @Nonnull
    public static ChoiceDescriptorDto get(@JsonProperty(PropertyNames.VALUE) @Nonnull PrimitiveFormControlDataDto value,
                                          @JsonProperty(PropertyNames.LABEL) @Nonnull LanguageMap label) {
        return new AutoValue_ChoiceDescriptorDto(label, value);
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract PrimitiveFormControlDataDto getValue();
}
