package edu.stanford.bmir.protege.web.shared.revision;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@JsonTypeName("webprotege.history.GetRevisionSummaries")
public class GetRevisionSummariesAction implements ProjectAction<GetRevisionSummariesResult> {

    private ProjectId projectId;

    /**
     * For serialization purposes only
     */
    private GetRevisionSummariesAction() {
    }

    private GetRevisionSummariesAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    public static GetRevisionSummariesAction create(ProjectId projectId) {
        return new GetRevisionSummariesAction(projectId);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetRevisionSummariesAction)) {
            return false;
        }
        GetRevisionSummariesAction other = (GetRevisionSummariesAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetRevisionSummariesAction")
                .addValue(projectId)
                .toString();
    }
}
