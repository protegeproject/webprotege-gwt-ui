package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.forms.DeleteForm")
public abstract class DeleteFormAction implements ProjectAction<DeleteFormResult> {

    @JsonCreator
    public static DeleteFormAction get(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                            @JsonProperty("formId") @Nonnull FormId formId) {
        return new AutoValue_DeleteFormAction(projectId, formId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();
}
