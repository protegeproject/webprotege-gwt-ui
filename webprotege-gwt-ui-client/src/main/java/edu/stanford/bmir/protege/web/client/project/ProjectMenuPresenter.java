package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsDownloader;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsImporter;
import edu.stanford.bmir.protege.web.client.role.EditProjectRolesHandler;
import edu.stanford.bmir.protege.web.client.tag.EditProjectTagsUIActionHandler;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class ProjectMenuPresenter implements HasDispose, Presenter {

    private static final Messages MESSAGES = GWT.create(Messages.class);

    private final ProjectMenuView view;

    private final ShowProjectDetailsHandler showProjectDetailsHandler;

    private final UploadAndMergeHandler uploadAndMergeHandler;

    private final UploadAndProcessSiblingsOrderingHandler uploadAndProcessSiblingsOrderingHandler;

    private final UploadAndMergeAdditionsHandler uploadAndMergeAdditionsHandler;

    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    private final EditProjectPrefixDeclarationsHandler editProjectPrefixDeclarationsHandler;

    private final EditProjectTagsUIActionHandler editProjectTagsUIActionHandler;

    private final EditProjectFormsUiHandler editProjectFormsUiHandler;

    private final ProjectSettingsDownloader projectSettingsDownloader;

    private final ProjectSettingsImporter projectSettingsImporter;

    private final ManageHierarchiesHandler manageHierarchiesHandler;

    private final EditProjectRolesHandler editProjectRolesHandler;

    private final AbstractUiAction editProjectSettings = new AbstractUiAction(MESSAGES.projectSettings()) {
        @Override
        public void execute() {
            showProjectDetailsHandler.handleShowProjectDetails();
        }
    };

    private final AbstractUiAction uploadAndMerge = new AbstractUiAction(MESSAGES.uploadAndMerge()) {
        @Override
        public void execute() {
            uploadAndMergeHandler.handleUploadAndMerge();
        }
    };

    private final AbstractUiAction uploadSiblingsOrdering = new AbstractUiAction(MESSAGES.siblingsOrdering()) {
        @Override
        public void execute() {
            uploadAndProcessSiblingsOrderingHandler.handleUploadSiblingsOrdering();
        }
    };

    private final AbstractUiAction uploadAndMergeAdditions = new AbstractUiAction(MESSAGES.uploadAndMergeAdditions()) {
        @Override
        public void execute() {
            uploadAndMergeAdditionsHandler.handleUploadAndMergeAdditions();
        }
    };

    private final AbstractUiAction editProjectPrefixes = new AbstractUiAction(MESSAGES.prefixes_edit()) {
        @Override
        public void execute() {
            editProjectPrefixDeclarationsHandler.handleEditProjectPrefixes();
        }
    };

    private final AbstractUiAction editProjectTags = new AbstractUiAction(MESSAGES.tags_EditProjectTags()) {
        @Override
        public void execute() {
            editProjectTagsUIActionHandler.handleEditProjectTags();
        }
    };

    private final AbstractUiAction editProjectForms = new AbstractUiAction(MESSAGES.forms_EditProjectForms()) {
        @Override
        public void execute() {
            editProjectFormsUiHandler.handleEditProjectForms();
        }
    };

    private final AbstractUiAction exportSettings = new AbstractUiAction(MESSAGES.settings_export()) {
        @Override
        public void execute() {
            projectSettingsDownloader.download();
        }
    };

    private final AbstractUiAction importSettings = new AbstractUiAction(MESSAGES.settings_import()) {
        @Override
        public void execute() {
            projectSettingsImporter.importSettings();
        }
    };

    private final AbstractUiAction manageHierarchies = new AbstractUiAction(MESSAGES.setting_manageHierarchies()) {
        @Override
        public void execute() {
            manageHierarchiesHandler.handleManageHierarchies();
        }
    };

    private final AbstractUiAction editProjectRoles = new AbstractUiAction(MESSAGES.settings_editProjectRoles()) {
        @Override
        public void execute() {
            editProjectRolesHandler.handleEditProjectRoles();
        }
    };

    @Inject
    public ProjectMenuPresenter(LoggedInUserProjectCapabilityChecker capabilityChecker,
                                ProjectMenuView view,
                                ShowProjectDetailsHandler showProjectDetailsHandler,
                                UploadAndMergeHandler uploadAndMergeHandler,
                                UploadAndProcessSiblingsOrderingHandler uploadAndProcessSiblingsOrderingHandler,
                                UploadAndMergeAdditionsHandler uploadAndMergeAdditionsHandler,
                                EditProjectPrefixDeclarationsHandler editProjectPrefixDeclarationsHandler,
                                EditProjectTagsUIActionHandler editProjectTagsUIActionHandler,
                                EditProjectFormsUiHandler editProjectFormsUiHandler,
                                ProjectSettingsDownloader projectSettingsDownloader,
                                ProjectSettingsImporter projectSettingsImporter,
                                ManageHierarchiesHandler manageHierarchiesHandler,
                                EditProjectRolesHandler editProjectRolesHandler) {
        this.capabilityChecker = capabilityChecker;
        this.view = view;
        this.showProjectDetailsHandler = showProjectDetailsHandler;
        this.uploadAndMergeHandler = uploadAndMergeHandler;
        this.uploadAndProcessSiblingsOrderingHandler = uploadAndProcessSiblingsOrderingHandler;
        this.uploadAndMergeAdditionsHandler = uploadAndMergeAdditionsHandler;
        this.editProjectPrefixDeclarationsHandler = editProjectPrefixDeclarationsHandler;
        this.editProjectTagsUIActionHandler = editProjectTagsUIActionHandler;
        this.editProjectFormsUiHandler = editProjectFormsUiHandler;
        this.projectSettingsDownloader = projectSettingsDownloader;
        this.projectSettingsImporter = projectSettingsImporter;
        this.editProjectRolesHandler = editProjectRolesHandler;
        setupActions();
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        editProjectSettings.setEnabled(false);
        uploadAndMerge.setEnabled(false);
        uploadAndMergeAdditions.setEnabled(false);
        uploadSiblingsOrdering.setEnabled(false);
        manageHierarchies.setEnabled(false);
        editProjectRoles.setEnabled(false);
        displayButton(container);
        capabilityChecker.hasCapability(EDIT_PROJECT_SETTINGS, editProjectSettings::setEnabled);
        capabilityChecker.hasCapability(UPLOAD_AND_MERGE, uploadAndMerge::setEnabled);
        capabilityChecker.hasCapability(EDIT_PROJECT_PREFIXES, editProjectPrefixes::setEnabled);
        capabilityChecker.hasCapability(EDIT_PROJECT_TAGS, editProjectTags::setEnabled);
        capabilityChecker.hasCapability(EDIT_FORMS, editProjectForms::setEnabled);
        capabilityChecker.hasCapability(EDIT_PROJECT_SETTINGS, exportSettings::setEnabled);
        capabilityChecker.hasCapability(EDIT_PROJECT_SETTINGS, importSettings::setEnabled);
        capabilityChecker.hasCapability(UPLOAD_AND_MERGE_ADDITIONS, uploadAndMergeAdditions::setEnabled);
        capabilityChecker.hasCapability(EDIT_ONTOLOGY, uploadSiblingsOrdering::setEnabled);
        capabilityChecker.hasCapability(EDIT_PROJECT_SETTINGS, manageHierarchies::setEnabled);
        capabilityChecker.hasCapability(EDIT_PROJECT_SETTINGS, editProjectRoles::setEnabled);
    }

    public void dispose() {

    }

    private void displayButton(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    private void setupActions() {
        view.addMenuAction(editProjectSettings);
        view.addMenuAction(editProjectTags);
        view.addMenuAction(editProjectForms);
        view.addMenuAction(editProjectPrefixes);
        view.addMenuAction(manageHierarchies);
        view.addMenuAction(editProjectRoles);
        view.addSeparator();
        view.addMenuAction(uploadSiblingsOrdering);
        view.addSeparator();
        view.addMenuAction(uploadAndMerge);

        view.addSeparator();
        view.addMenuAction(exportSettings);
        view.addMenuAction(importSettings);

        view.addMenuAction(uploadAndMergeAdditions);

    }


}
