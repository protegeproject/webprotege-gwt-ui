package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import java.util.Optional;

public interface ParentRoleSelectorView extends IsWidget, ValueEditor<RoleId> {

    void setParentRoleIdSupplier(ParentRoleIdSupplier parentRoleIdSupplier);

    Optional<RoleId> getSelectedRole();

}
