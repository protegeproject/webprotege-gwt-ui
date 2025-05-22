package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nullable;

import static edu.stanford.bmir.protege.web.shared.postcoordination.SaveEntityPostCoordinationAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class SaveEntityPostCoordinationAction extends AbstractHasProjectAction<SaveEntityPostCoordinationResult> {

    public final static String CHANNEL = "webprotege.postcoordination.AddEntitySpecificationRevision";

    @JsonCreator
    public static SaveEntityPostCoordinationAction create(@JsonProperty("projectId")
                                                          ProjectId projectId,
                                                          @JsonProperty("entitySpecification")
                                                          WhoficEntityPostCoordinationSpecification entitySpecification,
                                                          @JsonProperty("commitsMessage") @Nullable
                                                          String commitMessage) {
        return new AutoValue_SaveEntityPostCoordinationAction(projectId, entitySpecification, commitMessage);
    }


    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("entitySpecification")
    public abstract WhoficEntityPostCoordinationSpecification getEntitySpecification();

    @JsonProperty("commitMessage")
    public abstract String getCommitMessage();
}
