package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("FormData")
public abstract class FormData implements FormControlData {

    @JsonCreator
    public static FormData get(@JsonProperty("subject") @Nonnull Optional<FormEntitySubject> subject,
                               @JsonProperty("formDescriptor") @Nonnull FormDescriptor formDescriptor,
                               @JsonProperty("formFieldData") @Nonnull ImmutableList<FormFieldData> formFieldData) {
        return new AutoValue_FormData(subject.orElse(null), formDescriptor, formFieldData);
    }

    public static FormData empty(@Nonnull OWLEntity entity,
                                 @Nonnull FormId formId) {
        return get(Optional.of(FormSubject.get(entity)),
                   FormDescriptor.empty(formId),
                   ImmutableList.of());
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
    public Optional<FormEntitySubject> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @JsonIgnore
    @Nullable
    protected abstract FormEntitySubject getSubjectInternal();

    public abstract FormDescriptor getFormDescriptor();

    public abstract ImmutableList<FormFieldData> getFormFieldData();

    @Nonnull
    public FormId getFormId() {
        return getFormDescriptor().getFormId();
    }
}
