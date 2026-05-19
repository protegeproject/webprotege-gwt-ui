package edu.stanford.bmir.protege.web.shared.bulkop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import jsinterop.annotations.JsProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.MoveEntitiesToParent")
public abstract class MoveEntitiesToParentAction implements ProjectAction<MoveEntitiesToParentResult>, HasCommitMessage {

    @GwtIncompatible
    public static MoveEntitiesToParentAction create(@Nonnull ProjectId projectId,
                                                    @Nonnull ImmutableSet<OWLClass> entities,
                                                    @Nonnull OWLClass entity,
                                                    @Nonnull String commitMessage) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()), projectId, entities, entity, commitMessage);
    }

    @JsonCreator
    public static MoveEntitiesToParentAction create(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                                    @JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("entities") @Nonnull ImmutableSet<OWLClass> entities,
                                                    @JsonProperty("parentEntity") @Nonnull OWLClass entity,
                                                    @JsonProperty("commitMessage") @Nonnull String commitMessage) {
        return new AutoValue_MoveEntitiesToParentAction(changeRequestId, projectId, entities, entity, commitMessage);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ImmutableSet<? extends OWLEntity> getEntities();

    @Nonnull
    public abstract OWLEntity getParentEntity();

    @Nonnull
    @Override
    public abstract String getCommitMessage();
}
