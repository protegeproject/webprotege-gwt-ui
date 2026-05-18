package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
@JsonTypeName("webprotege.discussions.SetDiscussionThreadStatus")
public class SetDiscussionThreadStatusAction implements ProjectAction<SetDiscussionThreadStatusResult> {

    private ChangeRequestId changeRequestId;

    private ProjectId projectId;

    private ThreadId threadId;

    private Status status;

    @JsonCreator
    public SetDiscussionThreadStatusAction(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                           @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                           @JsonProperty("threadId") @Nonnull ThreadId threadId,
                                           @JsonProperty("status") @Nonnull Status status) {
        this.changeRequestId = checkNotNull(changeRequestId);
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.status = checkNotNull(status);
    }

    public SetDiscussionThreadStatusAction(@Nonnull ProjectId projectId,
                                           @Nonnull ThreadId threadId,
                                           @Nonnull Status status) {
        this(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, threadId, status);
    }

    @GwtSerializationConstructor
    private SetDiscussionThreadStatusAction() {
    }

    public static SetDiscussionThreadStatusAction setDiscussionThreadStatus(@Nonnull ProjectId projectId,
                                                                            @Nonnull ThreadId threadId,
                                                                            @Nonnull Status status) {
        return new SetDiscussionThreadStatusAction(projectId, threadId, status);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ThreadId getThreadId() {
        return threadId;
    }

    @Nonnull
    public Status getStatus() {
        return status;
    }
}
