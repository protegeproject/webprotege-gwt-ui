package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2017
 */
@JsonTypeName("webprotege.entities.DeleteEntities")
public class DeleteEntitiesResult implements Result {

    private Set<OWLEntity> deletedEntities;

    @GwtSerializationConstructor
    private DeleteEntitiesResult() {
    }


    @JsonCreator
    public DeleteEntitiesResult(@JsonProperty("deletedEntities") @Nonnull Set<OWLEntity> deletedEntities) {
        this.deletedEntities = new HashSet<>(deletedEntities);
    }



    public Set<OWLEntity> getDeletedEntities() {
        return new HashSet<>(deletedEntities);
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
