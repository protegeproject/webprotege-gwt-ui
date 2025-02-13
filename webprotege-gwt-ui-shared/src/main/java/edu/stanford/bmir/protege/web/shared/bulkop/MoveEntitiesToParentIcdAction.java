
package edu.stanford.bmir.protege.web.shared.bulkop;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.MoveEntitiesToParentIcd")
public abstract class MoveEntitiesToParentIcdAction implements ProjectAction<MoveEntitiesToParentIcdResult>, HasCommitMessage {

    @JsonCreator
    public static MoveEntitiesToParentIcdAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                       @JsonProperty("entities") @Nonnull ImmutableSet<OWLClass> entities,
                                                       @JsonProperty("parentEntity") @Nonnull OWLClass entity,
                                                       @JsonProperty("commitMessage") @Nonnull String commitMessage) {
        return new AutoValue_MoveEntitiesToParentIcdAction(projectId, entities, entity, commitMessage);
    }

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
