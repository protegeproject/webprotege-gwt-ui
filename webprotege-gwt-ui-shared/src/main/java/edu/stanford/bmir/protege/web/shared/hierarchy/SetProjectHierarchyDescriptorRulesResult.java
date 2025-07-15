package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.SetProjectHierarchyDescriptorRules")
public abstract class SetProjectHierarchyDescriptorRulesResult implements Result {

    @JsonCreator
    public static SetProjectHierarchyDescriptorRulesResult get() {
        return new AutoValue_SetProjectHierarchyDescriptorRulesResult();
    }
}
