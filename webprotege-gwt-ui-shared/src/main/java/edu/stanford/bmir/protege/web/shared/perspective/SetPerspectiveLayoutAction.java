package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.validation.constraints.NotNull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.perspectives.SetPerspectiveLayout")
public abstract class SetPerspectiveLayoutAction implements ProjectAction<SetPerspectiveLayoutResult>, ChangeRequest, HasProjectId {
    @JsonCreator
    public static SetPerspectiveLayoutAction create(@JsonProperty("changeRequestId") @NotNull ChangeRequestId id,
                                                    @JsonProperty("projectId") @NotNull ProjectId projectId,
                                                    @JsonProperty("userId") @NotNull UserId userId,
                                                    @JsonProperty("layout") @NotNull PerspectiveLayout layout) {
        return new AutoValue_SetPerspectiveLayoutAction(projectId,id, userId, layout);
    }

    public abstract UserId userId();

    public abstract PerspectiveLayout layout();


    @Override
    public String toString() {
        return toStringHelper("SetPerspectiveLayoutAction")
                .addValue(changeRequestId())
                .addValue(getProjectId())
                .addValue(userId())
                .addValue(layout())
                .toString();
    }
}
