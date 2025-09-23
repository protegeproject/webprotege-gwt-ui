package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.MoveHierarchyNodeIcd")
public abstract class MoveHierarchyNodeIcdResult implements Result {


    @JsonCreator
    public static MoveHierarchyNodeIcdResult create(@JsonProperty("moved") boolean moved,
                                                    @JsonProperty("isDestinationRetiredClass") boolean isDestinationRetiredClass,
                                                    @JsonProperty("isInitialParentLinPathParent") boolean isInitialParentLinPathParent,
                                                    @JsonProperty("releasedChildrenValidationMessage") @Nullable String releasedChildrenValidationMessage) {
        return new AutoValue_MoveHierarchyNodeIcdResult(moved, isDestinationRetiredClass, isInitialParentLinPathParent, releasedChildrenValidationMessage);
    }

    @JsonProperty("moved")
    public abstract boolean isMoved();

    @JsonProperty("isDestinationRetiredClass")
    public abstract boolean isDestinationRetiredClass();

    @JsonProperty("isInitialParentLinPathParent")
    public abstract boolean isInitialParentLinPathParent();

    @JsonProperty("releasedChildrenValidationMessage")
    @Nullable
    public abstract String getReleasedChildrenValidationMessage();


}
