package edu.stanford.bmir.protege.web.shared.perspective;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import jsinterop.annotations.JsIgnore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.perspectives.SetPerspectives")
public abstract class SetPerspectivesAction implements ProjectAction<SetPerspectivesResult>, HasProjectId {


    @GwtIncompatible
    public static SetPerspectivesAction create(@Nonnull ProjectId projectId,
                                               @Nonnull ImmutableList<PerspectiveDescriptor> perspectives) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, null, perspectives);
    }

    @GwtIncompatible
    public static SetPerspectivesAction create(@Nonnull ProjectId projectId,
                                               @Nonnull Optional<UserId> userId,
                                               @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, userId.orElse(null), perspectiveIds);
    }

    @JsonCreator
    public static SetPerspectivesAction create(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                               @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                               @JsonProperty("userId") @Nullable UserId userId,
                                               @JsonProperty("perspectives") @Nonnull ImmutableList<PerspectiveDescriptor> perspectiveIds) {
        return new AutoValue_SetPerspectivesAction(changeRequestId, projectId, userId, perspectiveIds);
    }

    @JsonProperty("changeRequestId")
    @Nonnull
    public abstract ChangeRequestId getChangeRequestId();

    @JsonProperty("projectId")
    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @JsonProperty("userId")
    public abstract UserId getUserIdInternal();

    @JsonIgnore
    public Optional<UserId> getUserId() {
        return Optional.ofNullable(getUserIdInternal());
    }

    @JsonProperty("perspectives")
    @Nonnull
    public abstract ImmutableList<PerspectiveDescriptor> getPerspectiveDescriptors();

}

