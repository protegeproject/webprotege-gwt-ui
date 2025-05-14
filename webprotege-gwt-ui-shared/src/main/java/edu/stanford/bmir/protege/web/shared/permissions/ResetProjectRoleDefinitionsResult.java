package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.authorization.ResetProjectRoleDefinitions")
public abstract class ResetProjectRoleDefinitionsResult implements Result {

    @JsonCreator
    public static ResetProjectRoleDefinitionsResult get(@JsonProperty("roleDefinitions") List<RoleDefinition> roleDefinitions) {
        return new AutoValue_ResetProjectRoleDefinitionsResult(roleDefinitions);
    }

    @JsonProperty("roleDefinitions")
    public abstract List<RoleDefinition> getRoleDefinitions();
}
