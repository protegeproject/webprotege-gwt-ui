package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptorRule;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.SetProjectHierarchyDescriptorRules")
public abstract class SetProjectHierarchyDescriptorRulesAction implements ProjectAction<SetProjectHierarchyDescriptorRulesResult> {

    @JsonProperty("rules")
    public abstract List<HierarchyDescriptorRule> getRules();

    @JsonCreator
    public static SetProjectHierarchyDescriptorRulesAction create(@JsonProperty("projectId") ProjectId projectId,
                                                                  @JsonProperty("rules") List<HierarchyDescriptorRule> rules) {
        return new AutoValue_SetProjectHierarchyDescriptorRulesAction(projectId, rules);
    }
}
