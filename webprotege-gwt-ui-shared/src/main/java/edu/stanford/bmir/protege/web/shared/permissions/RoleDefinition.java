package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class RoleDefinition {

    @JsonCreator
    public static RoleDefinition get(@JsonProperty("roleId") RoleId roleId,
                                     @Nonnull @JsonProperty("roleType") RoleType roleType,
                                     @Nullable @JsonProperty("label") String label,
                                     @JsonProperty("description") String description,
                                     @JsonProperty("parentRoles") List<RoleId> parentRoles,
                                     @JsonProperty("roleCapabilities") List<Capability> roleCapabilities) {
        return new AutoValue_RoleDefinition(roleId, roleType, label, description, parentRoles, roleCapabilities);
    }

    @JsonProperty("roleId")
    public abstract RoleId getRoleId();

    @JsonProperty("roleType")
    public abstract RoleType getRoleType();

    @JsonProperty("label")
    @Nullable
    public abstract String getLabel();

    @JsonProperty("description")
    public abstract String getDescription();

    @JsonProperty("parentRoles")
    public abstract List<RoleId> getParentRoles();

    @JsonProperty("roleCapabilities")
    public abstract List<Capability> getRoleCapabilities();
}
