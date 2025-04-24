package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;

import java.util.List;
import java.util.Optional;

public interface RoleDefinitionView extends IsWidget {

    void setRoleId(RoleId roleId);

    Optional<RoleId> getRoleId();

    void setAvailableRoles(List<RoleDefinition> availableRoles);

    void setParentRoles(List<RoleId> parentRoles);

    List<RoleId> getParentRoles();

    void setDescription(String description);

    String getDescription();

    void setCapabilities(List<Capability> capabilities);

    List<Capability> getCapabilities();
}
