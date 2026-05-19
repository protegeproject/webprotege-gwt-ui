package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
@JsonTypeName("webprotege.history.RevertRevision")
public class RevertRevisionAction implements ProjectAction<RevertRevisionResult> {

    private ChangeRequestId changeRequestId;

    private RevisionNumber revisionNumber;

    private ProjectId projectId;

    /**
     * For serialization
     */
    private RevertRevisionAction() {
    }

    private RevertRevisionAction(ChangeRequestId changeRequestId, ProjectId projectId, RevisionNumber revisionNumber) {
        this.changeRequestId = checkNotNull(changeRequestId);
        this.projectId = checkNotNull(projectId);
        this.revisionNumber = checkNotNull(revisionNumber);
    }

    @GwtIncompatible
    public static RevertRevisionAction create(ProjectId projectId,
                                              RevisionNumber revisionNumber) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, revisionNumber);
    }

    @JsonCreator
    public static RevertRevisionAction create(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                              @JsonProperty("projectId") ProjectId projectId,
                                              @JsonProperty("revisionNumber") RevisionNumber revisionNumber) {
        return new RevertRevisionAction(changeRequestId, projectId, revisionNumber);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public RevisionNumber getRevisionNumber() {
        return revisionNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, revisionNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RevertRevisionAction)) {
            return false;
        }
        RevertRevisionAction other = (RevertRevisionAction) obj;
        return this.projectId.equals(other.projectId) && this.revisionNumber.equals(other.revisionNumber);
    }


    @Override
    public String toString() {
        return toStringHelper("RevertRevisionAction")
                .addValue(projectId)
                .addValue(revisionNumber)
                .toString();
    }
}
