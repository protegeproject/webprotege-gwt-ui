package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CopyFormDescriptorsFromProjectAction.CHANNEL)
public abstract class CopyFormDescriptorsFromProjectAction implements ProjectAction<CopyFormDescriptorsFromProjectResult> {

    public static final String CHANNEL = "webprotege.forms.CopyFormDescriptors";

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ProjectId getFromProjectId();

    @Nonnull
    public abstract ImmutableList<FormId> getFormIds();

    @JsonCreator
    public static CopyFormDescriptorsFromProjectAction create(@JsonProperty("projectId") ProjectId projectId,
                                                              @JsonProperty("fromProjectId") ProjectId fromProjectId,
                                                              @JsonProperty("formIds") ImmutableList<FormId> newFormIdsToCopy) {
        return new AutoValue_CopyFormDescriptorsFromProjectAction(projectId,
                                                                  fromProjectId,
                                                                  newFormIdsToCopy);
    }


}
