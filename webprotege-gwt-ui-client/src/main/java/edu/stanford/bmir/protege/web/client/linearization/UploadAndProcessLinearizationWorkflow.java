package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.upload.*;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;


public class UploadAndProcessLinearizationWorkflow {

    private static final Messages MESSAGES = GWT.create(Messages.class);


    @Nonnull
    private final UploadFileDialogControllerFactory uploadFileDialogControllerFactory;

    @Nonnull
    private final ProcessUploadedLinerizationWorkflow processLinerizationWorkflow;

    @Inject
    public UploadAndProcessLinearizationWorkflow(@Nonnull UploadFileDialogControllerFactory uploadFileDialogControllerFactory,
                                                 @Nonnull ProcessUploadedLinerizationWorkflow processLinerizationWorkflow) {
        this.uploadFileDialogControllerFactory = uploadFileDialogControllerFactory;
        this.processLinerizationWorkflow = checkNotNull(processLinerizationWorkflow);
    }


    public void start(ProjectId projectId) {
        uploadLiniarization(projectId);
    }


    private void uploadLiniarization(final ProjectId projectId) {
        UploadFileDialogController uploadFileDialogController = uploadFileDialogControllerFactory.create(
                MESSAGES.linearizationUpload(), new UploadFileResultHandler() {
                    @Override
                    public void handleFileUploaded(DocumentId fileDocumentId, boolean overrideExisting) {
                        startLinearizationWorkflow(projectId, fileDocumentId);
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

    private void startLinearizationWorkflow(ProjectId projectId, DocumentId documentId) {
        processLinerizationWorkflow.start(projectId, documentId);
    }
}
