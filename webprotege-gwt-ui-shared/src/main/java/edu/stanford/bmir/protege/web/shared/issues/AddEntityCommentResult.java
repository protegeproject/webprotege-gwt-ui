package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.discussions.AddComment")
public abstract class AddEntityCommentResult implements Result, HasProjectId {

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract ThreadId getThreadId();

    public abstract Comment getComment();

    public abstract String getCommentRendering();

    @JsonCreator
    public static AddEntityCommentResult create(@JsonProperty("projectId") ProjectId newProjectId,
                                                @JsonProperty("threadId") ThreadId newThreadId,
                                                @JsonProperty("comment") Comment newComment,
                                                @JsonProperty("commentRendering") String newCommentRendering) {
        return new AutoValue_AddEntityCommentResult(newProjectId,
                                                    newThreadId,
                                                    newComment,
                                                    newCommentRendering);
    }
}
