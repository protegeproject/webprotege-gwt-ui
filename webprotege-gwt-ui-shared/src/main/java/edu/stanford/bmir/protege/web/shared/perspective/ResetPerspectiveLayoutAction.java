package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2017
 */
@JsonTypeName("webprotege.perspectives.ResetPerspectiveLayout")
@GwtCompatible(serializable = true)
@AutoValue
public abstract class ResetPerspectiveLayoutAction implements ProjectAction<ResetPerspectiveLayoutResult> {

    public static ResetPerspectiveLayoutAction resetPerspective(@Nonnull ProjectId projectId,
                                                                @Nonnull PerspectiveId perspectiveId,
                                                                @Nonnull ChangeRequestId changeRequestId) {
        return create(projectId, perspectiveId, changeRequestId);
    }

    @JsonCreator
    public static ResetPerspectiveLayoutAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                      @JsonProperty("perspectiveId") @Nonnull PerspectiveId perspectiveId,
                                                      @JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId) {
        return new AutoValue_ResetPerspectiveLayoutAction(projectId, perspectiveId, changeRequestId);
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();


    @Nonnull
    @JsonProperty("perspectiveId")
    public abstract PerspectiveId getPerspectiveId();

    @Nonnull
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();
}
