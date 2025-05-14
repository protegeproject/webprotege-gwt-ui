package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.forms.GetFormRegionAccessRestrictions")
public abstract class GetFormRegionAccessRestrictionsResult implements Result, HasProjectId {

    @JsonCreator
    public static GetFormRegionAccessRestrictionsResult get(@JsonProperty("projectId") ProjectId projectId,
                                                            @JsonProperty("accessRestrictions") List<FormRegionAccessRestriction> accessRestrictions) {
        return new AutoValue_GetFormRegionAccessRestrictionsResult(projectId, accessRestrictions);
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("accessRestrictions")
    public abstract List<FormRegionAccessRestriction> getAccessRestrictions();
}
