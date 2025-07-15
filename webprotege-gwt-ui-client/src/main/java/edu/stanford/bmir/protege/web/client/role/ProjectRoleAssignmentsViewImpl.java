package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitGeneratedAnnotationsSettingsPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectRoleDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.ProjectUserRoleAssignment;
import edu.stanford.bmir.protege.web.shared.permissions.RoleDefinition;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectRoleAssignmentsViewImpl extends Composite implements ProjectRoleAssignmentsView {

    private final List<RoleDefinition> roleDefinitions = new ArrayList<>();

    interface ProjectRoleAssignmentsViewImplUiBinder extends UiBinder<HTMLPanel, ProjectRoleAssignmentsViewImpl> {

    }

    private static ProjectRoleAssignmentsViewImplUiBinder ourUiBinder = GWT.create(ProjectRoleAssignmentsViewImplUiBinder.class);

    @UiField(provided = true)
    ValueListEditor<ProjectUserRoleAssignment> listEditor;

    @Inject
    public ProjectRoleAssignmentsViewImpl(DispatchServiceManager dispatchServiceManager) {
        listEditor = new ValueListFlexEditorImpl<>(() -> {
            ProjectRoleAssignmentsValueEditor editor = new ProjectRoleAssignmentsValueEditor(dispatchServiceManager);
            editor.setAvailableRoles(roleDefinitions);
            return editor;
        });
        listEditor.setEnabled(true);
        listEditor.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setAvailableRoles(List<RoleDefinition> roleDefinitions) {
        this.roleDefinitions.clear();
        this.roleDefinitions.addAll(roleDefinitions);
        listEditor.forEachEditor(editor -> {
            ((ProjectRoleAssignmentsValueEditor) editor).setAvailableRoles(roleDefinitions);
        });
    }

    @Override
    public void setRoleAssignments(List<ProjectUserRoleAssignment> assignments) {
        listEditor.setValue(assignments);
    }

    @Override
    public List<ProjectUserRoleAssignment> getRoleAssignments() {
        return listEditor.getValue().orElse(Collections.emptyList());
    }
}