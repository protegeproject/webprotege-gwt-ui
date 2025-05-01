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
@JsonTypeName("webprotege.authorization.SetProjectRoleAssignments")
public abstract class SetProjectRoleAssignmentsAction implements ProjectAction<SetProjectRoleAssignmentsResult> {

    @JsonCreator
    public static SetProjectRoleAssignmentsAction create(@JsonProperty("projectId") ProjectId projectId,
                                                         @JsonProperty("assignments") ProjectRoleAssignments assignments) {
        return new AutoValue_SetProjectRoleAssignmentsAction(projectId, assignments);
    }

    @JsonProperty("assignments")
    public abstract ProjectRoleAssignments getProjectRoleAssignments();
}