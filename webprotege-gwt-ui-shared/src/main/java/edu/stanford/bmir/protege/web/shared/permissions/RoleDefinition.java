package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class RoleDefinition {

    @JsonCreator
    public static RoleDefinition get(@JsonProperty("roleId") RoleId roleId,
                                     @JsonProperty("description") String description,
                                     @JsonProperty("parentRoles") List<RoleId> parentRoles,
                                     @JsonProperty("roleCapabilities") List<Capability> roleCapabilities) {
        return new AutoValue_RoleDefinition(roleId, description, parentRoles, roleCapabilities);
    }

    @JsonProperty("roleId")
    public abstract RoleId getRoleId();

    @JsonProperty("description")
    public abstract String getDescription();

    @JsonProperty("parentRoles")
    public abstract List<RoleId> getParentRoles();

    @JsonProperty("roleCapabilties")
    public abstract List<Capability> getRoleCapabilities();
}
