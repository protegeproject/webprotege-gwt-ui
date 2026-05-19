package edu.stanford.bmir.protege.web.shared.revision;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    @Nullable
    private PageRequest pageRequest;

    /**
     * For serialization purposes only
     */
    private GetRevisionSummariesAction() {
    }

    private GetRevisionSummariesAction(ProjectId projectId, @Nullable PageRequest pageRequest) {
        this.projectId = checkNotNull(projectId);
        this.pageRequest = pageRequest;
    }

    public static GetRevisionSummariesAction create(ProjectId projectId) {
        return new GetRevisionSummariesAction(projectId, null);
    }

    @JsonCreator
    public static GetRevisionSummariesAction create(@JsonProperty("projectId") ProjectId projectId,
                                                    @JsonProperty("pageRequest") @Nullable PageRequest pageRequest) {
        return new GetRevisionSummariesAction(projectId, pageRequest);
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nullable
    @JsonProperty("pageRequest")
    public PageRequest getPageRequest() {
        return pageRequest;
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
