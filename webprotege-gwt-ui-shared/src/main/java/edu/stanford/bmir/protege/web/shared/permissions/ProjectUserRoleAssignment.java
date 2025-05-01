package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class ProjectUserRoleAssignment {

    @JsonCreator
    public static ProjectUserRoleAssignment create(@JsonProperty("userId") UserId userId,
                                                   @JsonProperty("roleId") RoleId roleId) {
        return new AutoValue_ProjectUserRoleAssignment(userId, roleId);
    }

    @JsonProperty("userId")
    public abstract UserId getUserId();

    @JsonProperty("roleId")
    public abstract RoleId getRoleId();
}
