package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.forms.GetFormRegionAccessRestrictions")
public abstract class GetFormRegionAccessRestrictionsAction implements ProjectAction<GetFormRegionAccessRestrictionsResult> {

    @JsonCreator
    public static GetFormRegionAccessRestrictionsAction get(@JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetFormRegionAccessRestrictionsAction(projectId);
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();
}
