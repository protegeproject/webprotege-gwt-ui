package edu.stanford.bmir.protege.web.shared.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.discussions.GetEntityDiscussionThreads")
public abstract class GetEntityDiscussionThreadsResult implements Result {

    @JsonCreator
    public static GetEntityDiscussionThreadsResult create(@JsonProperty("entity") @Nonnull OWLEntityData entityData,
                                                          @JsonProperty("threads") @Nonnull ImmutableList<EntityDiscussionThread> threads) {
        return new AutoValue_GetEntityDiscussionThreadsResult(entityData, threads);
    }

    @Nonnull
    public abstract OWLEntityData getEntity();

    @Nonnull
    public abstract ImmutableList<EntityDiscussionThread> getThreads();
}
