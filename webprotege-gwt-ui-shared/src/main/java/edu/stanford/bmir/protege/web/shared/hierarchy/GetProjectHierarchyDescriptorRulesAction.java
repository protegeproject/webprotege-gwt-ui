package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetProjectHierarchyDescriptorRules")
public abstract class GetProjectHierarchyDescriptorRulesAction implements ProjectAction<GetProjectHierarchyDescriptorRulesResult> {

    @JsonCreator
    public static GetProjectHierarchyDescriptorRulesAction create(ProjectId projectId) {
        return new AutoValue_GetProjectHierarchyDescriptorRulesAction(projectId);
    }
}
