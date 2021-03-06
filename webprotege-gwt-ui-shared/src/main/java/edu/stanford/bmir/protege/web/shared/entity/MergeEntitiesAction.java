package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.bulkop.HasCommitMessage;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Mar 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.MergeEntities")
public abstract class MergeEntitiesAction extends AbstractHasProjectAction<MergeEntitiesResult> implements HasCommitMessage {


    /**
     * Creates a {@link MergeEntitiesAction}.  An entity merge is directional – one entity is merged into
     * another entity.
     * @param projectId The project to perform the merge in.
     * @param sourceEntities The entities that will be merged into another entity.
     * @param targetEntity The entity that will have the source entity merged into it.
     * @param treatment The treatment for the merged entity that specifies whether the merged entity
     *                  will be deleted or deprecated.
     */
    public static MergeEntitiesAction mergeEntities(@Nonnull ProjectId projectId,
                                                    @Nonnull ImmutableSet<OWLEntity> sourceEntities,
                                                    @Nonnull OWLEntity targetEntity,
                                                    @Nonnull MergedEntityTreatment treatment,
                                                    @Nonnull String commitMessage) {
        return create(projectId, sourceEntities, targetEntity, treatment, commitMessage);
    }

    @JsonCreator
    public static MergeEntitiesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                             @JsonProperty("sourceEntities") @Nonnull ImmutableSet<OWLEntity> sourceEntities,
                                             @JsonProperty("targetEntity") @Nonnull OWLEntity targetEntity,
                                             @JsonProperty("treatment") @Nonnull MergedEntityTreatment treatment,
                                             @JsonProperty("commitMessage") @Nonnull String commitMessage) {
        return new AutoValue_MergeEntitiesAction(projectId, sourceEntities, targetEntity, treatment, commitMessage);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    /**
     * Gets the class that is being merged into another class
     */
    @Nonnull
    public abstract ImmutableSet<OWLEntity> getSourceEntities();

    /**
     * Gets the class that the other class is being merged into.
     */
    @Nonnull
    public abstract OWLEntity getTargetEntity();

    /**
     * Get the treatment for the entity that is being merged into another entity.
     */
    @Nonnull
    public abstract MergedEntityTreatment getTreatment();

    @Nonnull
    @Override
    public abstract String getCommitMessage();
}
