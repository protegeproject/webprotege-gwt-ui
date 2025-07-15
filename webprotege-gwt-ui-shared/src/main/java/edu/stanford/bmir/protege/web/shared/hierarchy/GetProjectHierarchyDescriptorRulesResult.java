package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptorRule;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetProjectHierarchyDescriptorRules")
public abstract class GetProjectHierarchyDescriptorRulesResult implements Result {

    @JsonCreator
    public static GetProjectHierarchyDescriptorRulesResult create(@JsonProperty("rules") List<HierarchyDescriptorRule> rules) {
        return new AutoValue_GetProjectHierarchyDescriptorRulesResult(rules);
    }

    @JsonProperty("rules")
    public abstract List<HierarchyDescriptorRule> getRules();
}
