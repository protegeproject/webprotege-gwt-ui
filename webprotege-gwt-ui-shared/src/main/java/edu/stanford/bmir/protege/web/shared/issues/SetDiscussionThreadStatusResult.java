package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
@JsonTypeName("webprotege.discussions.SetDiscussionThreadStatus")
public class SetDiscussionThreadStatusResult implements Result {

    private ThreadId threadId;

    private Status result;

    private EventList<ProjectEvent<?>> eventList;

    public SetDiscussionThreadStatusResult(@Nonnull ThreadId threadId,
                                           @Nonnull Status result) {
        this.threadId = checkNotNull(threadId);
        this.result = checkNotNull(result);
        this.eventList = checkNotNull(eventList);
    }

    @GwtSerializationConstructor
    private SetDiscussionThreadStatusResult() {
    }

    @Nonnull
    public ThreadId getThreadId() {
        return threadId;
    }

    @Nonnull
    public Status getResult() {
        return result;
    }
}
