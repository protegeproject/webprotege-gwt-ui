package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.client.form.ObjectListPresenter;
import edu.stanford.bmir.protege.web.client.form.ObjectListView;
import edu.stanford.bmir.protege.web.client.form.ObjectListViewHolder;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.permissions.RoleType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleDefinitionsObjectListPresenter extends ObjectListPresenter<RoleDefinition> {

    private ParentRoleIdSupplier parentRoleIdSupplier = () -> Collections.emptySet();

    private RoleIdCycleFinder roleIdCycleFinder = roleId -> Optional.empty();

    @Inject
    public RoleDefinitionsObjectListPresenter(@Nonnull ObjectListView view,
                                              @Nonnull Provider<RoleDefinitionPresenter> objectListPresenter,
                                              @Nonnull Provider<ObjectListViewHolder> objectViewHolderProvider) {
        super(view, objectListPresenter::get, objectViewHolderProvider, () -> blankRoleDefinition());
        setAddObjectText("Add role");
    }

    private static RoleDefinition blankRoleDefinition() {
        return RoleDefinition.get(new RoleId("" ), RoleType.PROJECT_ROLE, "","", Collections.emptyList() , new ArrayList<>());
    }

    @Override
    protected void customizePresenter(ObjectPresenter<RoleDefinition> presenter) {
        RoleDefinitionPresenter defPresenter = (RoleDefinitionPresenter) presenter;
        defPresenter.setParentRoleIdSupplier(parentRoleIdSupplier);
        defPresenter.setRoleIdCycleChecker(roleIdCycleFinder);
    }

    public List<RoleDefinitionPresenter> getRoleDefinitionPresenters() {
        return super.getObjectPresenters()
                .stream()
                .map(p -> (RoleDefinitionPresenter) p)
                .collect(Collectors.toList());
    }

    public void setParentRoleIdSupplier(ParentRoleIdSupplier parentRoleIdSupplier) {
        this.parentRoleIdSupplier = parentRoleIdSupplier;
        getRoleDefinitionPresenters()
                .forEach(p -> p.setParentRoleIdSupplier(parentRoleIdSupplier));
    }

    public void setRoleIdCycleChecker(RoleIdCycleFinder roleIdCycleFinder) {
        this.roleIdCycleFinder = roleIdCycleFinder;
        getRoleDefinitionPresenters().forEach(p -> p.setRoleIdCycleChecker(roleIdCycleFinder));
    }
}
