package edu.stanford.bmir.protege.web.shared.icd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;


import java.util.List;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.GetClassAncestors")
public abstract class GetClassAncestorsResult implements Result {
    public abstract AncestorClassHierarchy getAncestorsTree();

    @JsonCreator
    public static GetClassAncestorsResult create(@JsonProperty("ancestorTree")  AncestorClassHierarchy ancestorTree ) {
        return new AutoValue_GetClassAncestorsResult(ancestorTree);
    }
}
