package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetHierarchyDescriptor")
public abstract class GetHierarchyDescriptorResult implements Result {

    @JsonCreator
    public static GetHierarchyDescriptorResult create(@JsonProperty("descriptor") HierarchyDescriptor hierarchyDescriptor) {
        return new AutoValue_GetHierarchyDescriptorResult(hierarchyDescriptor);
    }

    @JsonProperty("descriptor")
    public abstract HierarchyDescriptor getHierarchyDescriptor();
}
