package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class RoleDefinition {

    @JsonCreator
    public static RoleDefinition get(@JsonProperty("roleId") RoleId roleId,
                                     @JsonProperty("description") String description) {
        return new AutoValue_RoleDefinition(roleId, description);
    }

    @JsonProperty("roleId")
    public abstract RoleId getRoleId();

    @JsonProperty("description")
    public abstract String getDescription();
}
