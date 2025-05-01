package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.permissions.ProjectRoleAssignments;
import edu.stanford.bmir.protege.web.shared.permissions.ProjectUserRoleAssignment;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;

import java.util.List;

public interface ProjectRoleAssignmentsView extends IsWidget {

    void setAvailableRoles(List<RoleDefinition> roleDefinitions);

    void setRoleAssignments(List<ProjectUserRoleAssignment> assignments);

    List<ProjectUserRoleAssignment> getRoleAssignments();
}
