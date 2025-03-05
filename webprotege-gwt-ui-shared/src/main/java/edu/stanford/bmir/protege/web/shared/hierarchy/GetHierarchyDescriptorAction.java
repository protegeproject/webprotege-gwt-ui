package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.DisplayContext;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetHierarchyDescriptor")
public abstract class GetHierarchyDescriptorAction implements ProjectAction<GetHierarchyDescriptorResult> {

    @JsonCreator
    public static GetHierarchyDescriptorAction create(@JsonProperty("projectId") ProjectId projectId,
                                                      @JsonProperty("displayContext") DisplayContext displayContext) {
        return new AutoValue_GetHierarchyDescriptorAction(projectId, displayContext);
    }

    @JsonProperty("displayContext")
    public abstract DisplayContext getDisplayContext();
}
