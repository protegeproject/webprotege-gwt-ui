package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.permissions.RoleType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RoleDefinitionPresenter implements ObjectPresenter<RoleDefinition> {

    private final Logger logger = Logger.getLogger(RoleDefinitionPresenter.class.getName());

    private final RoleDefinitionView view;

    private Optional<RoleId> currentRoleId = Optional.empty();

    private Consumer<String> headerLabelChangedHandler = (label) -> {};

    private RoleIdCycleFinder roleIdCycleFinder = roleId -> Optional.empty();

    @Inject
    public RoleDefinitionPresenter(RoleDefinitionView view) {
        this.view = view;
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void setValue(@Nonnull RoleDefinition value) {
        currentRoleId = Optional.of(value.getRoleId());
        view.setRoleId(value.getRoleId());
        view.setLabel(value.getLabel());
        view.setDescription(value.getDescription());
        view.setParentRoles(value.getParentRoles());
        view.setParentRoleIdsChangedHandler(this::handleParentRolesChanged);
        view.setCapabilities(value.getRoleCapabilities());
        view.setRoleIdChangedHandler(this::handleHeaderLabelChanged);
        headerLabelChangedHandler.accept(getHeaderLabel());
        checkForCycles();
    }

    private void handleParentRolesChanged() {
        checkForCycles();
    }

    private void checkForCycles() {
        view.clearCycle();
        currentRoleId.ifPresent(id -> {
            Optional<List<RoleId>> cycle = roleIdCycleFinder.findCycle(id);
            cycle.ifPresent(view::displayCycle);
        });
    }

    private void handleHeaderLabelChanged(String s) {
        this.currentRoleId = Optional.of(s.trim()).filter(trimmed -> !trimmed.isEmpty()).map(RoleId::new);
        headerLabelChangedHandler.accept(s.trim());
    }

    @Nonnull
    @Override
    public Optional<RoleDefinition> getValue() {
        return Optional.of(RoleDefinition.get(
                view.getRoleId().orElse(new RoleId("")),
                RoleType.PROJECT_ROLE,
                view.getLabel(),
                view.getDescription(),
                view.getParentRoles(),
                view.getCapabilities()
        ));
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return currentRoleId.map(RoleId::getId).orElse("");
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {
        this.headerLabelChangedHandler = headerLabelHandler;
    }

    public void setParentRoleIdSupplier(ParentRoleIdSupplier parentRoleIdSupplier) {
        view.setParentRoleIdSupplier(wrapParentRoleIdSupplier(parentRoleIdSupplier));
    }

    /**
     * Wraps the parent role id supplier to filter out the current role id.
     */
    private ParentRoleIdSupplier wrapParentRoleIdSupplier(ParentRoleIdSupplier parentRoleIdSupplier) {
        return () -> parentRoleIdSupplier.get()
                .stream()
                .filter(this::roleIdIsNotCurrentRoleId)
                .collect(Collectors.toSet());
    }

    /**
     * Determines if the specified role id is equal to the current role id
     * @param r The role id
     */
    private Boolean roleIdIsNotCurrentRoleId(RoleId r) {
        return currentRoleId.map(i -> !i.equals(r)).orElse(false);
    }

    public void setRoleIdCycleChecker(RoleIdCycleFinder roleIdCycleFinder) {
        this.roleIdCycleFinder = roleIdCycleFinder;
    }
}
