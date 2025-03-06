package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogController;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogControllerFactory;
import edu.stanford.bmir.protege.web.client.upload.UploadFileResultHandler;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.postcoordination.ProcessUploadedPostCoordinationResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;


public class UploadPostCoordinationWorkflow {


    @Nonnull
    private final UploadFileDialogControllerFactory uploadFileDialogControllerFactory;

    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;


    @Inject
    public UploadPostCoordinationWorkflow(@Nonnull UploadFileDialogControllerFactory uploadFileDialogControllerFactory,
                                          DispatchServiceManager dispatchServiceManager, @Nonnull MessageBox messageBox,
                                          @Nonnull DispatchErrorMessageDisplay errorDisplay, @Nonnull ProgressDisplay progressDisplay) {
        this.uploadFileDialogControllerFactory = uploadFileDialogControllerFactory;
        this.dispatchServiceManager = dispatchServiceManager;
        this.messageBox = messageBox;
        this.errorDisplay = errorDisplay;
        this.progressDisplay = progressDisplay;
    }


    public void start(ProjectId projectId, UploadPostCoordinationWorkflowConfiguration configuration) {
        uploadPostCoordination(projectId, configuration);
    }


    private void uploadPostCoordination(final ProjectId projectId,  UploadPostCoordinationWorkflowConfiguration configuration) {
        UploadFileDialogController uploadFileDialogController = uploadFileDialogControllerFactory.create(
                configuration.getMenuTitle(), new UploadFileResultHandler() {
                    @Override
                    public void handleFileUploaded(DocumentId fileDocumentId, boolean overrideExisting) {
                        handleAfterFileUpload(projectId, fileDocumentId, configuration);
                    }

                    @Override
                    public void handleFileUploadFailed(String errorMessage) {
                        GWT.log("Upload failed");
                    }
                },
                false
        );
        WebProtegeDialog.showDialog(uploadFileDialogController);
    }

    private void handleAfterFileUpload(ProjectId projectId, DocumentId documentId, UploadPostCoordinationWorkflowConfiguration configuration) {

        dispatchServiceManager.execute(
                configuration.getActionForDocumentUploadSupplier().apply(projectId, documentId),
                new DispatchServiceCallbackWithProgressDisplay<ProcessUploadedPostCoordinationResult>(errorDisplay, progressDisplay) {

                    @Override
                    public String getProgressDisplayTitle() {
                        return configuration.getProgressDisplayTitle();
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return configuration.getProgressDisplayMessages();
                    }

                    @Override
                    public void handleSuccess(ProcessUploadedPostCoordinationResult processUploadedLinearizationResult) {
                        messageBox.showMessage(configuration.getUploadSuccessMessage());
                    }
                }
        );
    }
}
