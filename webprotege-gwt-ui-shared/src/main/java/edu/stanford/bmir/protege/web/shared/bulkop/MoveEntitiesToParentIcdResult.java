package edu.stanford.bmir.protege.web.shared.bulkop;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.MoveEntitiesToParent")
public abstract class MoveEntitiesToParentIcdResult implements Result {

    @JsonCreator
    public static MoveEntitiesToParentIcdResult create(@JsonProperty("isDestinationRetiredClass") @Nonnull boolean isDestinationRetiredClass,
                                                       @JsonProperty("linearizationPathParents") @Nullable ImmutableSet<OWLEntityData> entitiesForWhichParentIsLinPathParent) {
        return new AutoValue_MoveEntitiesToParentIcdResult(isDestinationRetiredClass, entitiesForWhichParentIsLinPathParent);
    }

    @JsonProperty("isDestinationRetiredClass")
    public abstract boolean isDestinationRetiredClass();

    @JsonProperty("isDestinationRetiredClass")
    @Nonnull
    public abstract ImmutableSet<OWLEntityData> getEntitiesForWhichParentIsLinPathParent();

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
