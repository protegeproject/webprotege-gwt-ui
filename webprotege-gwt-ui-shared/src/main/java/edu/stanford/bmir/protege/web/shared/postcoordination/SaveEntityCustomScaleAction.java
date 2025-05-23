package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.*;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(SaveEntityCustomScaleAction.CHANNEL)
public abstract class SaveEntityCustomScaleAction extends AbstractHasProjectAction<SaveEntityCustomScaleResult> {
    public final static String CHANNEL = "webprotege.postcoordination.AddEntityCustomScalesRevision";


    @JsonCreator
    public static SaveEntityCustomScaleAction create(@JsonProperty("projectId")
                                                     ProjectId projectId,
                                                     @JsonProperty("entityCustomScaleValues")
                                                     WhoficCustomScalesValues entityCustomScaleValues,
                                                     @JsonProperty("commitMessage") @Nullable
                                                     String commitMessage) {
        return new AutoValue_SaveEntityCustomScaleAction(projectId, entityCustomScaleValues, commitMessage);
    }


    @Nonnull
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("entityCustomScaleValues")
    public abstract WhoficCustomScalesValues getEntityCustomScaleValues();

    @JsonProperty("commitMessage")
    public abstract String getCommitMessage();
}