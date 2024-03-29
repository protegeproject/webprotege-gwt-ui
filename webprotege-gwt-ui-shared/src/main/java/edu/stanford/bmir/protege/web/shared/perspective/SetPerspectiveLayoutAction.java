package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
@JsonTypeName("webprotege.perspectives.SetPerspectiveLayout")
public class SetPerspectiveLayoutAction implements ProjectAction<SetPerspectiveLayoutResult>, Serializable, IsSerializable {

    private ProjectId projectId;

    private UserId userId;

    private PerspectiveLayout layout;

    private ChangeRequestId changeRequestId;

    /**
     * For serialization only
     */
    private SetPerspectiveLayoutAction() {
    }

    private SetPerspectiveLayoutAction(ChangeRequestId changeRequestId, ProjectId projectId, UserId userId, PerspectiveLayout layout) {
        this.changeRequestId = checkNotNull(changeRequestId);
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.layout = checkNotNull(layout);
    }

    public static SetPerspectiveLayoutAction create(ProjectId projectId, UserId userId, PerspectiveLayout layout) {
        return new SetPerspectiveLayoutAction(ChangeRequestId.get("123"), projectId, userId, layout);
    }
    public static SetPerspectiveLayoutAction create(ChangeRequestId changeRequestId, ProjectId projectId, UserId userId, PerspectiveLayout layout) {
        return new SetPerspectiveLayoutAction(changeRequestId, projectId, userId, layout);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public UserId getUserId() {
        return userId;
    }

    public PerspectiveLayout getLayout() {
        return layout;
    }

    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    public void setChangeRequestId(ChangeRequestId changeRequestId) {
        this.changeRequestId = changeRequestId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, userId, layout);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetPerspectiveLayoutAction)) {
            return false;
        }
        SetPerspectiveLayoutAction other = (SetPerspectiveLayoutAction) obj;
        return this.projectId.equals(other.projectId)
                && this.userId.equals(other.userId)
                && this.changeRequestId.equals(other.changeRequestId)
                && this.layout.equals(other.layout);
    }


    @Override
    public String toString() {
        return toStringHelper("SetPerspectiveLayoutAction")
                .addValue(projectId)
                .addValue(userId)
                .addValue(layout)
                .addValue(changeRequestId)
                .toString();
    }
}
