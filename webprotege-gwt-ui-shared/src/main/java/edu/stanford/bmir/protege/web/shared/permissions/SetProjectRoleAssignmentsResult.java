package edu.stanford.bmir.protege.web.shared.permissions;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.authorization.SetProjectRoleAssignments")
public abstract class SetProjectRoleAssignmentsResult implements Result {

    @JsonCreator
    public static SetProjectRoleAssignmentsResult create(@JsonProperty("assignments") ProjectRoleAssignments roleAssignments) {
        return new AutoValue_SetProjectRoleAssignmentsResult(roleAssignments);
    }

    @JsonProperty("assignments")
    public abstract ProjectRoleAssignments getAssignments();

}