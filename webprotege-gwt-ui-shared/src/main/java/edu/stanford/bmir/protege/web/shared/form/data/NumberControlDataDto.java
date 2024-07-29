package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlDescriptor;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("NumberControlDataDto")
public abstract class NumberControlDataDto implements FormControlDataDto, Comparable<NumberControlDataDto> {

    private Double numericValue;

    @JsonCreator
    @Nonnull
    public static NumberControlDataDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull NumberControlDescriptor descriptor,
                                           @JsonProperty(PropertyNames.LABEL) @Nonnull OWLLiteral value,
                                           @JsonProperty(PropertyNames.DEPTH) int depth) {
        return new AutoValue_NumberControlDataDto(depth, descriptor, value);
    }

    @Nonnull
    public abstract NumberControlDescriptor getDescriptor();

    @JsonProperty(PropertyNames.LABEL)
    @Nullable
    protected abstract OWLLiteral getValueInternal();

    @Nonnull
    @JsonIgnore
    public Optional<OWLLiteral> getValue() {
        return Optional.ofNullable(getValueInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public NumberControlData toFormControlData() {
        return NumberControlData.get(getDescriptor(),
                                     getValueInternal());
    }

    private Double getNumericValue() {
        if (numericValue == null) {
            OWLLiteral literal = getValueInternal();
            if (literal == null) {
                numericValue = Double.MIN_VALUE;
            } else {
                try {
                    numericValue = Double.parseDouble(literal.getLiteral().trim());
                } catch (NumberFormatException e) {
                    numericValue = Double.MIN_VALUE;
                }
            }
        }
        return numericValue;
    }

    @Override
    public int compareTo(@Nonnull NumberControlDataDto o) {
        return getNumericValue().compareTo(o.getNumericValue());
    }
}
