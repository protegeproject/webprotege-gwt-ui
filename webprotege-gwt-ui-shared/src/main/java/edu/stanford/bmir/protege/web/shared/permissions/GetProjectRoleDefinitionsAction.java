package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.authorization.GetProjectRoleDefinitions")
public abstract class GetProjectRoleDefinitionsAction implements ProjectAction<GetProjectRoleDefinitionsResult> {

    @JsonCreator
    public static GetProjectRoleDefinitionsAction get(@JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetProjectRoleDefinitionsAction(projectId);
    }

}
