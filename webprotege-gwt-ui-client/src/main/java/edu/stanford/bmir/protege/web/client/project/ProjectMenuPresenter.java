package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.postcoordination.PostCoordinationChangesHandler;
import edu.stanford.bmir.protege.web.client.projectsettings.*;
import edu.stanford.bmir.protege.web.client.tag.EditProjectTagsUIActionHandler;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.*;

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

    private final UploadAndProcessLinearizationHandler linearizationChangesHandler;

    private final PostCoordinationChangesHandler postCoordinationChangesHandler;

    private final UploadAndMergeAdditionsHandler uploadAndMergeAdditionsHandler;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final EditProjectPrefixDeclarationsHandler editProjectPrefixDeclarationsHandler;

    private final EditProjectTagsUIActionHandler editProjectTagsUIActionHandler;

    private final EditProjectFormsUiHandler editProjectFormsUiHandler;

    private final ProjectSettingsDownloader projectSettingsDownloader;

    private final ProjectSettingsImporter projectSettingsImporter;


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

    private final AbstractUiAction uploadLinearizationChanges = new AbstractUiAction(MESSAGES.linearizationUpload()) {
        @Override
        public void execute() {
            linearizationChangesHandler.handleUploadLinearizationChanges();
        }
    };

    private final AbstractUiAction uploadPostCoordinationChanges = new AbstractUiAction(MESSAGES.postCoordinationUpload()) {
        @Override
        public void execute() {
            postCoordinationChangesHandler.handleUploadPostCoordinationChanges();
        }
    };

    private final AbstractUiAction uploadPostCoordinationCustomScales = new AbstractUiAction(MESSAGES.postCoordinationCustomScales()) {
        @Override
        public void execute() {
            postCoordinationChangesHandler.handlePostCoordinationCustomScales();
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

    @Inject
    public ProjectMenuPresenter(LoggedInUserProjectPermissionChecker permissionChecker,
                                ProjectMenuView view,
                                ShowProjectDetailsHandler showProjectDetailsHandler,
                                UploadAndMergeHandler uploadAndMergeHandler,
                                UploadAndProcessLinearizationHandler linearizationChangesHandler,
                                PostCoordinationChangesHandler postCoordinationChangesHandler, UploadAndMergeAdditionsHandler uploadAndMergeAdditionsHandler,
                                EditProjectPrefixDeclarationsHandler editProjectPrefixDeclarationsHandler,
                                EditProjectTagsUIActionHandler editProjectTagsUIActionHandler,
                                EditProjectFormsUiHandler editProjectFormsUiHandler,
                                ProjectSettingsDownloader projectSettingsDownloader,
                                ProjectSettingsImporter projectSettingsImporter) {
        this.permissionChecker = permissionChecker;
        this.view = view;
        this.showProjectDetailsHandler = showProjectDetailsHandler;
        this.uploadAndMergeHandler = uploadAndMergeHandler;
        this.linearizationChangesHandler = linearizationChangesHandler;
        this.postCoordinationChangesHandler = postCoordinationChangesHandler;
        this.uploadAndMergeAdditionsHandler = uploadAndMergeAdditionsHandler;
        this.editProjectPrefixDeclarationsHandler = editProjectPrefixDeclarationsHandler;
        this.editProjectTagsUIActionHandler = editProjectTagsUIActionHandler;
        this.editProjectFormsUiHandler = editProjectFormsUiHandler;
        this.projectSettingsDownloader = projectSettingsDownloader;
        this.projectSettingsImporter = projectSettingsImporter;
        setupActions();
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        editProjectSettings.setEnabled(false);
        uploadAndMerge.setEnabled(false);
        uploadAndMergeAdditions.setEnabled(false);
        uploadLinearizationChanges.setEnabled(false);
        uploadPostCoordinationChanges.setEnabled(false);
        uploadPostCoordinationCustomScales.setEnabled(false);
        displayButton(container);
        permissionChecker.hasPermission(EDIT_PROJECT_SETTINGS, editProjectSettings::setEnabled);
        permissionChecker.hasPermission(UPLOAD_AND_MERGE, uploadAndMerge::setEnabled);
        permissionChecker.hasPermission(EDIT_PROJECT_PREFIXES, editProjectPrefixes::setEnabled);
        permissionChecker.hasPermission(EDIT_PROJECT_TAGS, editProjectTags::setEnabled);
        permissionChecker.hasPermission(EDIT_FORMS, editProjectForms::setEnabled);
        permissionChecker.hasPermission(EDIT_PROJECT_SETTINGS, exportSettings::setEnabled);
        permissionChecker.hasPermission(EDIT_PROJECT_SETTINGS, importSettings::setEnabled);
        permissionChecker.hasPermission(UPLOAD_AND_MERGE_ADDITIONS, uploadAndMergeAdditions::setEnabled);
        permissionChecker.hasPermission(EDIT_ONTOLOGY, uploadLinearizationChanges::setEnabled);
        permissionChecker.hasPermission(EDIT_ONTOLOGY, uploadPostCoordinationChanges::setEnabled);
        permissionChecker.hasPermission(EDIT_ONTOLOGY, uploadPostCoordinationCustomScales::setEnabled);

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
        view.addSeparator();
        view.addMenuAction(uploadLinearizationChanges);
        view.addMenuAction(uploadPostCoordinationChanges);
        view.addMenuAction(uploadPostCoordinationCustomScales);
        view.addSeparator();
        view.addMenuAction(uploadAndMerge);

        view.addSeparator();
        view.addMenuAction(exportSettings);
        view.addMenuAction(importSettings);

        view.addMenuAction(uploadAndMergeAdditions);

    }


}
