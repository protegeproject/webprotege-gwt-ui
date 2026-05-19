package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
@JsonTypeName("webprotege.discussions.DeleteComment")
public class DeleteEntityCommentAction implements ProjectAction<DeleteEntityCommentResult> {

    private ChangeRequestId changeRequestId;

    private ProjectId projectId;

    private CommentId commentId;

    @GwtIncompatible
    public static DeleteEntityCommentAction deleteComment(@Nonnull ProjectId projectId,
                                                          @Nonnull CommentId commentId) {
        return deleteComment(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, commentId);
    }

    @JsonCreator
    public static DeleteEntityCommentAction deleteComment(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                                          @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                          @JsonProperty("commentId") @Nonnull CommentId commentId) {
        return new DeleteEntityCommentAction(changeRequestId, projectId, commentId);
    }

    public DeleteEntityCommentAction(@Nonnull ChangeRequestId changeRequestId,
                                     @Nonnull ProjectId projectId,
                                     @Nonnull CommentId commentId) {
        this.changeRequestId = checkNotNull(changeRequestId);
        this.commentId = checkNotNull(commentId);
        this.projectId = checkNotNull(projectId);
    }

    @GwtIncompatible
    public DeleteEntityCommentAction(@Nonnull ProjectId projectId,
                                     @Nonnull CommentId commentId) {
        this(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, commentId);
    }

    @GwtSerializationConstructor
    private DeleteEntityCommentAction() {
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

    public CommentId getCommentId() {
        return commentId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(commentId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DeleteEntityCommentAction)) {
            return false;
        }
        DeleteEntityCommentAction other = (DeleteEntityCommentAction) obj;
        return this.commentId.equals(other.commentId);
    }
}
