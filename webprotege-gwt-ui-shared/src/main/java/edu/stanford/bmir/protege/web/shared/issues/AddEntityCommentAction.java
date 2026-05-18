package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.discussions.AddComment")
public abstract class AddEntityCommentAction implements ProjectAction<AddEntityCommentResult>, HasProjectId {

    public static AddEntityCommentAction addComment(@Nonnull ProjectId projectId,
                                                    @Nonnull ThreadId threadId,
                                                    @Nonnull String comment) {
        return addComment(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, threadId, comment);
    }

    @JsonCreator
    public static AddEntityCommentAction addComment(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                                    @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("threadId") @Nonnull ThreadId threadId,
                                                    @JsonProperty("comment") @Nonnull String comment) {
        return new AutoValue_AddEntityCommentAction(changeRequestId, projectId, threadId, comment);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();

    @Nonnull
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("threadId")
    public abstract ThreadId getThreadId();

    @JsonProperty("comment")
    public abstract String getComment();
}
