package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.authorization.SetProjectRoleDefinitions")
public abstract class SetProjectRoleDefinitionsAction implements ProjectAction<SetProjectRoleDefinitionsResult> {

    @JsonCreator
    public static SetProjectRoleDefinitionsAction get(@JsonProperty("projectId") ProjectId projectId,
                                                      @JsonProperty("roleDefinitions") List<RoleDefinition> roleDefinitions) {
        return new AutoValue_SetProjectRoleDefinitionsAction(projectId, roleDefinitions);
    }

    @JsonProperty("roleDefinitions")
    public abstract List<RoleDefinition> getRoleDefinitions();

}
