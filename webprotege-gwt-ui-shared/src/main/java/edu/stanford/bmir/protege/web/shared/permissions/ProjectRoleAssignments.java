package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class ProjectRoleAssignments {

    @JsonCreator
    public static ProjectRoleAssignments get(@JsonProperty("userAssignments") List<ProjectUserRoleAssignment> userRoleAssignments) {
        return new AutoValue_ProjectRoleAssignments(userRoleAssignments);
    }

    @JsonProperty("userAssignments")
    public abstract List<ProjectUserRoleAssignment> getUserAssignments();
}
