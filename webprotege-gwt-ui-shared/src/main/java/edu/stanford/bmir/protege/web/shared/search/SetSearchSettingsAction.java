package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableList;
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
 * 2020-08-17
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.search.SetSearchSettings")
public abstract class SetSearchSettingsAction implements ProjectAction<SetSearchSettingsResult> {

    @GwtIncompatible
    public static SetSearchSettingsAction create(@Nonnull ProjectId projectId,
                                                 @Nonnull ImmutableList<EntitySearchFilter> from,
                                                 @Nonnull ImmutableList<EntitySearchFilter> to) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, from, to);
    }

    @JsonCreator
    public static SetSearchSettingsAction create(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                                 @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                 @JsonProperty("from") @Nonnull ImmutableList<EntitySearchFilter> from,
                                                 @JsonProperty("to") @Nonnull ImmutableList<EntitySearchFilter> to) {
        return new AutoValue_SetSearchSettingsAction(changeRequestId, projectId, from, to);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ImmutableList<EntitySearchFilter> getFrom();

    @Nonnull
    public abstract ImmutableList<EntitySearchFilter> getTo();
}
