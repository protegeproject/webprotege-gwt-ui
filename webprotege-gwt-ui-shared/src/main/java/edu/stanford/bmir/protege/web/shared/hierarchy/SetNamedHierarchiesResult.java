package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.SetNamedHierarchies")
public abstract class SetNamedHierarchiesResult implements Result {

    @JsonCreator
    public static SetNamedHierarchiesResult get() {
        return new AutoValue_SetNamedHierarchiesResult();
    }
}
