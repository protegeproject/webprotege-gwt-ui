package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface RoleDefinitionView extends IsWidget {

    void setRoleId(RoleId roleId);

    Optional<RoleId> getRoleId();

    void setParentRoles(List<RoleId> parentRoles);

    List<RoleId> getParentRoles();

    void setDescription(String description);

    String getLabel();

    void setLabel(String label);

    String getDescription();

    void setCapabilities(List<Capability> capabilities);

    List<Capability> getCapabilities();

    void setParentRoleIdSupplier(ParentRoleIdSupplier parentRoleIdSupplier);

    void setRoleIdChangedHandler(Consumer<String> o);

    void setParentRoleIdsChangedHandler(ParentRoleIdsChangedHandler handler);

    void clearCycle();

    void displayCycle(List<RoleId> roleIds);
}
