package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SingleChoiceControlData")
public abstract class SingleChoiceControlData implements FormControlData {

    @JsonCreator
    public static SingleChoiceControlData get(@JsonProperty(PropertyNames.CONTROL) @Nonnull SingleChoiceControlDescriptor descriptor,
                                              @JsonProperty(PropertyNames.CHOICE) @Nullable PrimitiveFormControlData choice) {

        return new AutoValue_SingleChoiceControlData(descriptor, choice);
    }

    @Override
    public <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormControlDataVisitor visitor) {
        visitor.visit(this);
    }

    @Nonnull
    public abstract SingleChoiceControlDescriptor getDescriptor();

    @JsonProperty("choice")
    @Nullable
    protected abstract PrimitiveFormControlData getChoiceInternal();

    @Nonnull
    public Optional<PrimitiveFormControlData> getChoice() {
        return Optional.ofNullable(getChoiceInternal());
    }
}
