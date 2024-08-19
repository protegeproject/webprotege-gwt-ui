package edu.stanford.bmir.protege.web.shared.linearization;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.SaveEntityLinearization")
public abstract class SaveEntityLinearizationAction implements Action<SaveEntityLinearizationResult> {

    @JsonCreator
    public static SaveEntityLinearizationAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("entityLinearization") WhoficEntityLinearizationSpecification entityLinearization) {
        return new AutoValue_SaveEntityLinearizationAction(projectId, entityLinearization);
    }

    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("entityLinearization")
    public abstract WhoficEntityLinearizationSpecification getEntityLinearization();

}
