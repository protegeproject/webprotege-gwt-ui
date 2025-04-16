package edu.stanford.bmir.protege.web.client.permissions;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import java.util.List;
import java.util.Optional;

public interface ProjectRoleSelectorView extends IsWidget, ValueEditor<RoleId> {

    void setAvailableRoles(List<RoleId> availableRoles);

    Optional<RoleId> getSelectedRole();

}
