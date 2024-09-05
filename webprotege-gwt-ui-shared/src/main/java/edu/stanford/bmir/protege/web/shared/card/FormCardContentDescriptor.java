package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("FormCardContentDescriptor")
public abstract class FormCardContentDescriptor implements CardContentDescriptor {

    @JsonCreator
    public static FormCardContentDescriptor create(@JsonProperty("formId") FormId formId) {
        return new AutoValue_FormCardContentDescriptor(formId);
    }

    @JsonProperty("formId")
    @Nonnull
    public abstract FormId getFormId();

    @Override
    public <R> R accept(CardContentDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
