package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.MoveHierarchyNode")
public abstract class MoveHierarchyNodeResult implements Result {


    @JsonCreator
    public static MoveHierarchyNodeResult create(@JsonProperty("moved") boolean moved) {
        return new AutoValue_MoveHierarchyNodeResult(moved);
    }

    @JsonProperty("moved")
    public abstract boolean isMoved();

}
