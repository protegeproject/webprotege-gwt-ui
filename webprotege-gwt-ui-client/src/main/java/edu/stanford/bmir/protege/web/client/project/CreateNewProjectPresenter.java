package edu.stanford.bmir.protege.web.client.project;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.projectmanager.ProjectCreatedEvent;
import edu.stanford.bmir.protege.web.client.upload.FileUploadFileReader;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.upload.SubmitFileAction;
import edu.stanford.bmir.protege.web.shared.upload.SubmitFileResult;

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

    @Inject
    public CreateNewProjectPresenter(DispatchErrorMessageDisplay errorDisplay,
                                     ProgressDisplay progressDisplay,
                                     @Nonnull CreateNewProjectView view,
                                     @Nonnull LoggedInUserManager loggedInUserManager,
                                     @Nonnull DispatchServiceManager dispatchServiceManager,
                                     @Nonnull EventBus eventBus,
                                     @Nonnull MessageBox messageBox) {
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
        this.view = checkNotNull(view);
        this.loggedInUserManager = checkNotNull(loggedInUserManager);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.eventBus = checkNotNull(eventBus);
        this.messageBox = messageBox;
    }

    @Nonnull
    public CreateNewProjectView getView() {
        return view;
    }

    public void start() {
        view.clear();
        view.setFileUploadEnabled(loggedInUserManager.isAllowedApplicationAction(UPLOAD_PROJECT));
    }

    private boolean validate() {
        if (view.getProjectName().isEmpty()) {
            view.showProjectNameMissingMessage();
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
        if (view.isFileUploadSpecified()) {
            uploadSourcesAndCreateProject(handler);
        }
        else {
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


    private void uploadSourcesAndCreateProject(@Nonnull ProjectCreatedHandler projectCreatedHandler) {
        checkNotNull(projectCreatedHandler);
        String fileUploadElementId = view.getFileUploadElementId();
        FileUploadFileReader fileUploadFileReader = new FileUploadFileReader();
        progressDisplay.displayProgress("Uploading file", "Uploading file.  Please wait.");
        fileUploadFileReader.readFiles(fileUploadElementId, fileContent -> {
            progressDisplay.hideProgress();
            handleFileReadComplete(fileContent, projectCreatedHandler);
        }, readError -> {
            progressDisplay.hideProgress();
            messageBox.showAlert("Upload Failed", "Your file could not be uploaded.");
        });
    }

    private void handleFileReadComplete(@Nonnull String base64EncodedContent,
                                        ProjectCreatedHandler projectCreatedHandler) {

            dispatchServiceManager.execute(SubmitFileAction.create(base64EncodedContent),
                                           busy -> {},
                                           result -> {
                                               handleCreateNewProjectFromUploadedSources(projectCreatedHandler, result);
                                           });


    }

    private void handleCreateNewProjectFromUploadedSources(ProjectCreatedHandler projectCreatedHandler, SubmitFileResult result) {
        DocumentId documentId = result.getFileSubmissionId();
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
                                               projectCreatedHandler.handleProjectCreated();
                                               eventBus.fireEvent(new ProjectCreatedEvent(result.getProjectDetails()));
                                           }

                                           @Override
                                           public void handleExecutionException(Throwable cause) {
                                               if (cause instanceof PermissionDeniedException) {
                                                   messageBox.showMessage(
                                                           "You do not have permission to create new projects");
                                               }
                                               else if (cause instanceof ProjectAlreadyRegisteredException) {
                                                   ProjectAlreadyRegisteredException ex = (ProjectAlreadyRegisteredException) cause;
                                                   String projectName = ex.getProjectId().getId();
                                                   messageBox.showMessage("The project name " + projectName + " is already registered.  Please try a different name.");
                                               }
                                               else if (cause instanceof ProjectDocumentExistsException) {
                                                   ProjectDocumentExistsException ex = (ProjectDocumentExistsException) cause;
                                                   String projectName = ex.getProjectId().getId();
                                                   messageBox.showMessage(
                                                           "There is already a non-empty project on the server with the id " + projectName + ".  This project has NOT been overwritten.  Please contact the administrator to resolve this issue.");
                                               }
                                               else {
                                                   messageBox.showMessage(cause.getMessage());
                                               }
                                           }
                                       });
    }

    public NewProjectInfo getNewProjectInfo() {
        return new NewProjectInfo(view.getProjectName(), view.getProjectDescription());
    }
}
