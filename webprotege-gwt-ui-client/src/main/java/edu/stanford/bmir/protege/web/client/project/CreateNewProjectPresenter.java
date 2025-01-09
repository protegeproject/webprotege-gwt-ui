package edu.stanford.bmir.protege.web.client.project;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.ApplicationEnvironmentManager;
import edu.stanford.bmir.protege.web.client.dispatch.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.projectmanager.ProjectCreatedEvent;
import edu.stanford.bmir.protege.web.client.upload.FileUploader;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.icd.*;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.UPLOAD_PROJECT;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Nov 2017
 */
public class CreateNewProjectPresenter {

    private final DispatchErrorMessageDisplay errorDisplay;

    private final ProgressDisplay progressDisplay;

    public interface ProjectCreatedHandler {

        void handleProjectCreated();
    }

    @Nonnull
    private final CreateNewProjectView view;

    @Nonnull
    private final LoggedInUserManager loggedInUserManager;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private final MessageBox messageBox;

    private final ApplicationEnvironmentManager applicationEnvironmentManager;

    @Inject
    public CreateNewProjectPresenter(DispatchErrorMessageDisplay errorDisplay,
                                     ProgressDisplay progressDisplay,
                                     @Nonnull CreateNewProjectView view,
                                     @Nonnull LoggedInUserManager loggedInUserManager,
                                     @Nonnull DispatchServiceManager dispatchServiceManager,
                                     @Nonnull EventBus eventBus,
                                     @Nonnull MessageBox messageBox, ApplicationEnvironmentManager applicationEnvironmentManager) {
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
        this.view = checkNotNull(view);
        this.loggedInUserManager = checkNotNull(loggedInUserManager);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.eventBus = checkNotNull(eventBus);
        this.messageBox = messageBox;
        this.applicationEnvironmentManager = applicationEnvironmentManager;
    }

    @Nonnull
    public CreateNewProjectView getView() {
        return view;
    }

    public void start() {
        view.clear();
        view.setFileUploadEnabled(loggedInUserManager.isAllowedApplicationAction(UPLOAD_PROJECT));
        view.setFileSourceTypeOptions();
    }

    private boolean validate() {
        if (view.getProjectName().isEmpty()) {
            view.showProjectNameMissingMessage();
            return false;
        }else if ( view.isBackupFileSourceType() && !view.isBackupFilesBranchSet()){
            view.showBranchNameMissingMessage();
            return false;
        }
        return true;
    }

    public void validateAndCreateProject(ProjectCreatedHandler handler) {
        if (validate()) {
            submitCreateProjectRequest(handler);
        }
    }


    private void submitCreateProjectRequest(ProjectCreatedHandler handler) {
        String fileSourceType = view.getFileSourceType();
        if (fileSourceType.equals("owl") && view.isFileUploadSpecified()) {
            uploadSourcesAndCreateProject(handler);
        } else if (fileSourceType.equals("backup") && view.isFileUploadSpecified()) {
            uploadProjectBackupAndCreateProjectFromBackup(handler);
        } else {
            createEmptyProject(handler);
        }
    }

    private void createEmptyProject(ProjectCreatedHandler projectCreatedHandler) {
        NewProjectSettings newProjectSettings = NewProjectSettings.get(loggedInUserManager.getLoggedInUserId(),
                view.getProjectName(),
                view.getProjectLanguage(),
                view.getProjectDescription());
        submitCreateNewProjectRequest(newProjectSettings, projectCreatedHandler);
    }

    private void uploadProjectBackupAndCreateProjectFromBackup(@Nonnull ProjectCreatedHandler projectCreatedHandler) {
        checkNotNull(projectCreatedHandler);
        progressDisplay.displayProgress("Uploading file", "Uploading file.  Please wait.");
        dispatchServiceManager.execute(new GetUserInfoAction(),
                info -> createProjectFromProjectBackupForUser(info, projectCreatedHandler));
    }

    private void createProjectFromProjectBackupForUser(GetUserInfoResult userInfo,
                                                       ProjectCreatedHandler projectCreatedHandler) {
        String fileUploadElementId = view.getFileUploadElementId();
        FileUploader fileUploader = new FileUploader(applicationEnvironmentManager.getAppEnvVariables().getFileUploadUrl());
        fileUploader.uploadFile(fileUploadElementId,
                userInfo.getToken(),
                fileSubmissionId -> handleProjectBackupFilesSubmissionSuccess(projectCreatedHandler, fileSubmissionId),
                this::handleFileSubmissionError);
    }

    private void handleProjectBackupFilesSubmissionSuccess(ProjectCreatedHandler projectCreatedHandler, String fileSubmissionId) {
        progressDisplay.hideProgress();
        handleCreateNewProjectFromProjectBackup(projectCreatedHandler,
                new DocumentId(fileSubmissionId));
    }

    private void handleCreateNewProjectFromProjectBackup(ProjectCreatedHandler projectCreatedHandler, DocumentId documentId) {
        NewProjectSettings newProjectSettings = NewProjectSettings.get(loggedInUserManager.getLoggedInUserId(),
                view.getProjectName(),
                view.getProjectLanguage(),
                view.getProjectDescription(),
                documentId);
        submitCreateNewProjectFromProjectBackupRequest(newProjectSettings, projectCreatedHandler);
    }

    private void submitCreateNewProjectFromProjectBackupRequest(NewProjectSettings newProjectSettings, ProjectCreatedHandler projectCreatedHandler) {
        String uuid = UuidV4.uuidv4();
        ProjectId newProjectId = ProjectId.get(uuid);
        dispatchServiceManager.execute(new CreateNewProjectFromProjectBackupAction(newProjectId, newProjectSettings, view.getProjectVersioningBranch()),
                new DispatchServiceCallbackWithProgressDisplay<CreateNewProjectFromProjectBackupResult>(
                        errorDisplay,
                        progressDisplay) {
                    @Override
                    public String getProgressDisplayTitle() {
                        return "Creating project";
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return "Please wait.";
                    }

                    @Override
                    public void handleSuccess(CreateNewProjectFromProjectBackupResult result) {
                        handleProjectCreationSuccess(projectCreatedHandler, result.getProjectDetails());
                    }

                    @Override
                    public void handleExecutionException(Throwable cause) {
                        handleProjectCreationException(cause);
                    }
                });
    }


    private void uploadSourcesAndCreateProject(@Nonnull ProjectCreatedHandler projectCreatedHandler) {
        checkNotNull(projectCreatedHandler);
        progressDisplay.displayProgress("Uploading file", "Uploading file.  Please wait.");
        dispatchServiceManager.execute(new GetUserInfoAction(),
                info -> createProjectForUser(info, projectCreatedHandler));
    }

    private void createProjectForUser(GetUserInfoResult userInfo,
                                      ProjectCreatedHandler projectCreatedHandler) {

        String fileUploadElementId = view.getFileUploadElementId();
        FileUploader fileUploader = new FileUploader(applicationEnvironmentManager.getAppEnvVariables().getFileUploadUrl());
        fileUploader.uploadFile(fileUploadElementId,
                userInfo.getToken(),
                fileSubmissionId -> handleFileSubmissionSuccess(projectCreatedHandler, fileSubmissionId),
                this::handleFileSubmissionError);
    }

    private void handleFileSubmissionError(int errorCode) {
        messageBox.showMessage("Could not upload file", "Error code: " + errorCode);
        progressDisplay.hideProgress();
    }

    private void handleFileSubmissionSuccess(CreateNewProjectPresenter.ProjectCreatedHandler projectCreatedHandler,
                                             String fileSubmissionId) {
        progressDisplay.hideProgress();
        handleCreateNewProjectFromUploadedSources(projectCreatedHandler,
                new DocumentId(fileSubmissionId));
    }

    private void handleCreateNewProjectFromUploadedSources(ProjectCreatedHandler projectCreatedHandler, DocumentId documentId) {
        NewProjectSettings newProjectSettings = NewProjectSettings.get(loggedInUserManager.getLoggedInUserId(),
                view.getProjectName(),
                view.getProjectLanguage(),
                view.getProjectDescription(),
                documentId);
        submitCreateNewProjectRequest(newProjectSettings, projectCreatedHandler);
    }

    private void submitCreateNewProjectRequest(@Nonnull NewProjectSettings newProjectSettings,
                                               @Nonnull ProjectCreatedHandler projectCreatedHandler) {
        String uuid = UuidV4.uuidv4();
        ProjectId newProjectId = ProjectId.get(uuid);
        dispatchServiceManager.execute(new CreateNewProjectAction(newProjectId, newProjectSettings),
                new DispatchServiceCallbackWithProgressDisplay<CreateNewProjectResult>(
                        errorDisplay,
                        progressDisplay) {
                    @Override
                    public String getProgressDisplayTitle() {
                        return "Creating project";
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return "Please wait.";
                    }

                    @Override
                    public void handleSuccess(CreateNewProjectResult result) {
                        handleProjectCreationSuccess(projectCreatedHandler, result.getProjectDetails());
                    }

                    @Override
                    public void handleExecutionException(Throwable cause) {
                        handleProjectCreationException(cause);
                    }
                });
    }

    private void handleProjectCreationSuccess(ProjectCreatedHandler projectCreatedHandler, ProjectDetails projectDetails) {
        projectCreatedHandler.handleProjectCreated();
        eventBus.fireEvent(new ProjectCreatedEvent(projectDetails));
    }

    private void handleProjectCreationException(Throwable cause) {
        if (cause instanceof PermissionDeniedException) {
            messageBox.showMessage(
                    "You do not have permission to create new projects");
        } else if (cause instanceof ProjectAlreadyRegisteredException) {
            ProjectAlreadyRegisteredException ex = (ProjectAlreadyRegisteredException) cause;
            String projectName = ex.getProjectId().getId();
            messageBox.showMessage("The project name " + projectName + " is already registered.  Please try a different name.");
        } else if (cause instanceof ProjectDocumentExistsException) {
            ProjectDocumentExistsException ex = (ProjectDocumentExistsException) cause;
            String projectName = ex.getProjectId().getId();
            messageBox.showMessage(
                    "There is already a non-empty project on the server with the id " + projectName + ".  This project has NOT been overwritten.  Please contact the administrator to resolve this issue.");
        } else {
            messageBox.showMessage(cause.getMessage());
        }
    }
}