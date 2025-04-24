package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.client.form.ObjectListPresenter;
import edu.stanford.bmir.protege.web.client.form.ObjectListView;
import edu.stanford.bmir.protege.web.client.form.ObjectListViewHolder;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;

public class RoleDefinitionsObjectListPresenter extends ObjectListPresenter<RoleDefinition> {

    @Inject
    public RoleDefinitionsObjectListPresenter(@Nonnull ObjectListView view,
                                              @Nonnull Provider<ObjectPresenter<RoleDefinition>> objectListPresenter,
                                              @Nonnull Provider<ObjectListViewHolder> objectViewHolderProvider) {
        super(view, objectListPresenter, objectViewHolderProvider, () -> blankRoleDefinition());
    }

    private static RoleDefinition blankRoleDefinition() {
        return RoleDefinition.get(new RoleId(""), "" , new ArrayList<>());
    }
}
