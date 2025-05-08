package edu.stanford.bmir.protege.web.shared.perspective;


import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.*;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.perspectives.SetPerspectives")
public abstract class SetPerspectivesAction implements ProjectAction<SetPerspectivesResult>, HasProjectId {


    public static SetPerspectivesAction create(@Nonnull ProjectId projectId,
                                               @Nonnull ImmutableList<PerspectiveDescriptor> perspectives) {
        return new AutoValue_SetPerspectivesAction(projectId, null, perspectives);
    }


    public static SetPerspectivesAction create(@Nonnull ProjectId projectId,
                                               @Nonnull Optional<UserId> userId,
                                               @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        return new AutoValue_SetPerspectivesAction(projectId, userId.orElse(null), perspectiveIds);
    }

    @JsonCreator
    public static SetPerspectivesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                               @JsonProperty("userId") @Nullable UserId userId,
                                               @JsonProperty("perspectives") @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        return new AutoValue_SetPerspectivesAction(projectId, userId, perspectiveIds);
    }

    @JsonProperty("projectId")
    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @JsonProperty("userId")
    @Nullable
    public abstract UserId getUserIdInternal();

    @JsonIgnore
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(getUserIdInternal());
    }

    @JsonProperty("perspectives")
    @Nonnull
    public abstract ImmutableList<PerspectiveDescriptor> getPerspectiveDescriptors();

}

