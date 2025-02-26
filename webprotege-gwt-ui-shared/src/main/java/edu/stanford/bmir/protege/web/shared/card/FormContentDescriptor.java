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
@JsonTypeName("FormContent")
public abstract class FormContentDescriptor implements EntityCardContentDescriptor {

    @JsonCreator
    public static FormContentDescriptor create(@JsonProperty("formId") FormId formId) {
        return new AutoValue_FormContentDescriptor(formId);
    }

    @JsonProperty("formId")
    @Nonnull
    public abstract FormId getFormId();

    @Override
    public <R> R accept(EntityCardContentDescriptorVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
