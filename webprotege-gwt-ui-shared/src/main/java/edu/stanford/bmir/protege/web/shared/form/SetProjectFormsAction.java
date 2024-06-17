package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-15
 */
@JsonTypeName("webprotege.forms.SetProjectForms")
@AutoValue
@GwtCompatible(serializable = true)
public abstract class SetProjectFormsAction implements ProjectAction<SetProjectFormsResult> {

    public SetProjectFormsAction get(@Nonnull ProjectId projectId,
                                     @Nonnull ImmutableList<FormDescriptor> formDescriptors,
                                     @Nonnull ImmutableList<EntityFormSelector> formSelectors) {
        return new AutoValue_SetProjectFormsAction(projectId, formDescriptors, formSelectors);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    public abstract ImmutableList<FormDescriptor> getFormDescriptors();

    public abstract ImmutableList<EntityFormSelector> getFormSelectors();
}
