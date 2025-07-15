package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
@JsonTypeName(FixedChoiceListSourceDescriptor.TYPE)
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FixedChoiceListSourceDescriptor implements ChoiceListSourceDescriptor {

    public static final String TYPE = "Fixed";

    @JsonCreator
    public static FixedChoiceListSourceDescriptor get(@JsonProperty(PropertyNames.CHOICES) @Nullable ImmutableList<ChoiceDescriptor> choices) {
        return new AutoValue_FixedChoiceListSourceDescriptor(choices == null ? ImmutableList.of() : choices);
    }

    @JsonProperty(PropertyNames.CHOICES)
    @Nonnull
    public abstract ImmutableList<ChoiceDescriptor> getChoices();
}
