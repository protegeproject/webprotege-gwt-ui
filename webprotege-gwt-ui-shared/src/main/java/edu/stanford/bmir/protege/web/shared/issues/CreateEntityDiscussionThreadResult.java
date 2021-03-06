package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
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
 * 6 Oct 2016
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.discussions.CreateEntityDiscussionThread")
public abstract class CreateEntityDiscussionThreadResult implements Result {

    @JsonCreator
    public static CreateEntityDiscussionThreadResult create(@JsonProperty("threads") @Nonnull ImmutableList<EntityDiscussionThread> threads) {
        return new AutoValue_CreateEntityDiscussionThreadResult(threads);
    }

    public abstract ImmutableList<EntityDiscussionThread> getThreads();
}
