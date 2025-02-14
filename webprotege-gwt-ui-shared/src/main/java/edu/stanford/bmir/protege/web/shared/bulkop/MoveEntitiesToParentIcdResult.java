package edu.stanford.bmir.protege.web.shared.bulkop;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.*;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.MoveEntitiesToParentIcd")
public abstract class MoveEntitiesToParentIcdResult implements Result {

    @JsonCreator
    public static MoveEntitiesToParentIcdResult create(@JsonProperty("isDestinationRetiredClass") boolean isDestinationRetiredClass,
                                                       @JsonProperty("entitiesForWhichParentIsLinPathParent") @Nonnull ImmutableMap<String, ImmutableSet<OWLEntityData>> entitiesForWhichParentIsLinPathParent) {
        return new AutoValue_MoveEntitiesToParentIcdResult(isDestinationRetiredClass, entitiesForWhichParentIsLinPathParent);
    }

    @JsonProperty("isDestinationRetiredClass")
    public abstract boolean isDestinationRetiredClass();

    @JsonProperty("entitiesForWhichParentIsLinPathParent")
    @Nonnull
    public abstract ImmutableMap<String, ImmutableSet<OWLEntityData>> getEntitiesForWhichParentIsLinPathParent();

    public boolean hasOldParentAsLinearizationPathParent() {
        return !getEntitiesForWhichParentIsLinPathParent().isEmpty();
    }

    public boolean isFailure() {
        return isDestinationRetiredClass() || hasOldParentAsLinearizationPathParent();
    }

    public boolean isSuccess() {
        return !isFailure();
    }

}
