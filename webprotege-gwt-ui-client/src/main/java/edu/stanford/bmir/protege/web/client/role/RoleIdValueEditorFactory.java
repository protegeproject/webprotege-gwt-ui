package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.permissions.ProjectRoleSelectorPresenter;
import edu.stanford.bmir.protege.web.shared.access.RoleId;

import javax.inject.Inject;
import javax.inject.Provider;

public class RoleIdValueEditorFactory implements ValueEditorFactory<RoleId> {

    private final Provider<ProjectRoleSelectorPresenter> presenterProvider;

    @Inject
    public RoleIdValueEditorFactory(Provider<ProjectRoleSelectorPresenter> presenterProvider) {
        this.presenterProvider = presenterProvider;
    }

    @Override
    public ValueEditor<RoleId> createEditor() {
        ProjectRoleSelectorPresenter presenter = presenterProvider.get();
        presenter.start();
        return presenter;
    }
}
