package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.forms.SetFormRegionAccessRestrictions")
public abstract class SetFormRegionAccessRestrictionsAction implements ProjectAction<SetFormRegionAccessRestrictionsResult> {

    @JsonCreator
    public static SetFormRegionAccessRestrictionsAction get(@JsonProperty("projectId") ProjectId projectId,
                                                            @JsonProperty("accessRestrictions") List<FormRegionAccessRestrictions> accessRestrictions) {
        return new AutoValue_SetFormRegionAccessRestrictionsAction(projectId, accessRestrictions);
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("accessRestrictions")
    public abstract List<FormRegionAccessRestrictions> getAccessRestrictions();
}