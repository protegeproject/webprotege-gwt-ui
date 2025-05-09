package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2017
 */
@JsonTypeName("webprotege.entities.DeleteEntities")
public class DeleteEntitiesAction implements ProjectAction<DeleteEntitiesResult> {

    private ChangeRequestId changeRequestId;

    private ProjectId projectId;

    private ImmutableSet<OWLEntity> entities;

    @GwtSerializationConstructor
    private DeleteEntitiesAction() {
    }

    @JsonCreator
    public DeleteEntitiesAction(
            @JsonProperty("projectId") @Nonnull ChangeRequestId changeRequestId,
            @JsonProperty("projectId") @Nonnull ProjectId projectId,
            @JsonProperty("entities") @Nonnull ImmutableSet<OWLEntity> entities) {
        this.changeRequestId = changeRequestId;
        this.entities = entities;
        this.projectId = checkNotNull(projectId);
    }

    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public ImmutableSet<OWLEntity> getEntities() {
        return entities;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, entities);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DeleteEntitiesAction)) {
            return false;
        }
        DeleteEntitiesAction other = (DeleteEntitiesAction) obj;
        return this.projectId.equals(other.projectId)
                && this.entities.equals(other.entities);
    }


    @Override
    public String toString() {
        return toStringHelper("DeleteEntitiesAction")
                .addValue(projectId)
                .addValue(entities)
                .toString();
    }
}
