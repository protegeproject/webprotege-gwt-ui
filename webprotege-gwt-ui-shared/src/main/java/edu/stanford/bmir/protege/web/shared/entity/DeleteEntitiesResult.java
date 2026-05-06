package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
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
public class DeleteEntitiesResult implements Result {

    private ImmutableSet<OWLEntity> deletedEntities;

    @GwtSerializationConstructor
    private DeleteEntitiesResult() {
    }


    @JsonCreator
    public DeleteEntitiesResult(@JsonProperty("deletedEntities") @Nonnull ImmutableSet<OWLEntity> deletedEntities) {
        this.deletedEntities = checkNotNull(deletedEntities);
    }



    public ImmutableSet<OWLEntity> getDeletedEntities() {
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
