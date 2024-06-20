package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.MoveHierarchyNode")
public abstract class MoveHierarchyNodeResult implements Result {


    @JsonCreator
    public static MoveHierarchyNodeResult create(@JsonProperty("moved") boolean moved,
                                                 @JsonProperty("isDestinationRetiredClass") @Nonnull boolean isDestinationRetiredClass) {
        return new AutoValue_MoveHierarchyNodeResult(moved, isDestinationRetiredClass);
    }

    @JsonProperty("moved")
    public abstract boolean isMoved();

    @JsonProperty("isDestinationRetiredClass")
    public abstract boolean isDestinationRetiredClass();

}
