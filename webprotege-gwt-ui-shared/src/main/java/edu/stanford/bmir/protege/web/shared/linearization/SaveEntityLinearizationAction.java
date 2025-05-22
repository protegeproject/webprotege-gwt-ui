package edu.stanford.bmir.protege.web.shared.linearization;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nullable;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.SaveEntityLinearization")
public abstract class SaveEntityLinearizationAction implements Action<SaveEntityLinearizationResult> {

    @JsonCreator
    public static SaveEntityLinearizationAction create(@JsonProperty("projectId") ProjectId projectId,
                                                       @JsonProperty("entityLinearization") WhoficEntityLinearizationSpecification entityLinearization,
                                                       @JsonProperty("commitsMessage") @Nullable String commitMessage) {
        return new AutoValue_SaveEntityLinearizationAction(projectId, entityLinearization, commitMessage);
    }

    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("entityLinearization")
    public abstract WhoficEntityLinearizationSpecification getEntityLinearization();

    @JsonProperty("commitsMessage")
    public abstract String getCommitsMessage();

}
