package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2017
 */
@JsonTypeName("webprotege.entities.DeleteEntities")
public class DeleteEntitiesResult implements Result {

    private ImmutableSet<OWLEntityData> deletedEntities;

    @GwtSerializationConstructor
    private DeleteEntitiesResult() {
    }


    @JsonCreator
    public DeleteEntitiesResult(@JsonProperty("deletedEntities") @Nonnull ImmutableSet<OWLEntityData> deletedEntities) {
        this.deletedEntities = deletedEntities;
    }


    public ImmutableSet<OWLEntityData> getDeletedEntities() {
        return deletedEntities;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deletedEntities);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DeleteEntitiesResult)) {
            return false;
        }
        DeleteEntitiesResult other = (DeleteEntitiesResult) obj;
        return this.deletedEntities.equals(other.deletedEntities);
    }


    @Override
    public String toString() {
        return toStringHelper("DeleteEntitiesResult")
                .addValue(deletedEntities)
                .toString();
    }
}
