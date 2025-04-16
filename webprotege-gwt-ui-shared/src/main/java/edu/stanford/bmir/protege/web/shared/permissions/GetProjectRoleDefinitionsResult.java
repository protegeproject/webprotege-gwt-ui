package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.authorization.GetProjectRoleDefinitions")
public abstract class GetProjectRoleDefinitionsResult implements Result, HasProjectId {

    @JsonCreator
    public static GetProjectRoleDefinitionsResult get(@JsonProperty("projectId") ProjectId projectId,
                                                      @JsonProperty("roleDefinitions") List<RoleDefinition> roleDefinitions) {
        return new AutoValue_GetProjectRoleDefinitionsResult(projectId, roleDefinitions);
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("roleDefinitions")
    public abstract List<RoleDefinition> getRoleDefinitions();
}
