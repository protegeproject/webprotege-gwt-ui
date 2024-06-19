package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class ChoiceDescriptor implements IsSerializable {

    @JsonCreator
    public static ChoiceDescriptor choice(@Nonnull @JsonProperty(PropertyNames.LABEL) LanguageMap label,
                                          @Nonnull @JsonProperty(PropertyNames.VALUE) PrimitiveFormControlData value) {
        return new AutoValue_ChoiceDescriptor(label, value);
    }

    @Nonnull
    @JsonProperty(PropertyNames.LABEL)
    public abstract LanguageMap getLabel();

    @Nonnull
    @JsonProperty(PropertyNames.VALUE)
    public abstract PrimitiveFormControlData getValue();
}
