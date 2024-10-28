package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.AddNamedHierarchy")
public abstract class AddNamedHierarchyResult implements Result {

    @JsonCreator
    public static AddNamedHierarchyResult get() {
        return new AutoValue_AddNamedHierarchyResult();
    }
}
