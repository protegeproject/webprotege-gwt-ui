package edu.stanford.bmir.protege.web.shared.icd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.GetLogicalDefinitionsClassAncestors")
public abstract class GetLogicalDefinitionsClassAncestorsResult implements Result {
    public abstract AncestorClassHierarchy getAncestorsTree();

    @JsonCreator
    public static GetLogicalDefinitionsClassAncestorsResult create(@JsonProperty("ancestorTree")  AncestorClassHierarchy ancestorTree ) {
        return new AutoValue_GetLogicalDefinitionsClassAncestorsResult(ancestorTree);
    }
}
