package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class RoleDefinitionPresenter implements ObjectPresenter<RoleDefinition> {

    private final Logger logger = Logger.getLogger(RoleDefinitionPresenter.class.getName());

    private final RoleDefinitionView view;

    private Optional<RoleId> currentRoleId = Optional.empty();

    private Consumer<String> headerLabelChangedHandler = (label) -> {};

    @Inject
    public RoleDefinitionPresenter(RoleDefinitionView view) {
        this.view = view;
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void setValue(@Nonnull RoleDefinition value) {

        logger.info("Setting role: " + value);
        currentRoleId = Optional.of(value.getRoleId());
        view.setRoleId(value.getRoleId());
        view.setDescription(value.getDescription());
        view.setParentRoles(value.getParentRoles());
        view.setCapabilities(value.getRoleCapabilities());

        headerLabelChangedHandler.accept(getHeaderLabel());
    }

    @Nonnull
    @Override
    public Optional<RoleDefinition> getValue() {
        return Optional.of(RoleDefinition.get(
                view.getRoleId().orElse(new RoleId("")),
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
}
