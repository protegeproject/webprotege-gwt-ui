package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.upload.*;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class UploadAndProcessSiblingsOrderingWorkflow {

    private static final Messages MESSAGES = GWT.create(Messages.class);

    @Nonnull
    private final UploadFileDialogControllerFactory uploadFileDialogControllerFactory;

    @Nonnull
    private final ProcessUploadedSiblingsOrderingWorkflow processSiblingsOrderingWorkflow;

    @Inject
    public UploadAndProcessSiblingsOrderingWorkflow(@Nonnull UploadFileDialogControllerFactory uploadFileDialogControllerFactory,
                                                 @Nonnull ProcessUploadedSiblingsOrderingWorkflow processSiblingsOrderingWorkflow) {
        this.uploadFileDialogControllerFactory = uploadFileDialogControllerFactory;
        this.processSiblingsOrderingWorkflow = checkNotNull(processSiblingsOrderingWorkflow);
    }


    public void start(ProjectId projectId) {
        uploadSiblingsOrdering(projectId);
    }


    private void uploadSiblingsOrdering(final ProjectId projectId) {
        UploadFileDialogController uploadFileDialogController = uploadFileDialogControllerFactory.create(
                MESSAGES.siblingsOrdering(), new UploadFileResultHandler() {
                    @Override
                    public void handleFileUploaded(DocumentId fileDocumentId, boolean overrideExisting) {
                        startSiblingsOrderingWorkflow(projectId, fileDocumentId, overrideExisting);
                    }

                    @Override
                    public void handleFileUploadFailed(String errorMessage) {
                        GWT.log("Upload failed");
                    }
                },
                true
        );
        WebProtegeDialog.showDialog(uploadFileDialogController);
    }

    private void startSiblingsOrderingWorkflow(ProjectId projectId, DocumentId documentId, boolean overrideExisting) {
        processSiblingsOrderingWorkflow.start(projectId, documentId, overrideExisting);
    }
}
