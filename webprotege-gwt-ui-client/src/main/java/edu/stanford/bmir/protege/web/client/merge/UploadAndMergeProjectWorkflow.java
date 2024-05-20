package edu.stanford.bmir.protege.web.client.merge;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogController;
import edu.stanford.bmir.protege.web.client.upload.UploadFileDialogControllerFactory;
import edu.stanford.bmir.protege.web.client.upload.UploadFileResultHandler;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class UploadAndMergeProjectWorkflow {

    @Nonnull
    private final MergeUploadedProjectWorkflow mergeWorkflow;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final UploadFileDialogControllerFactory uploadFileDialogControllerFactory;

    @Inject
    public UploadAndMergeProjectWorkflow(@Nonnull MergeUploadedProjectWorkflow mergeWorkflow,
                                         @Nonnull DispatchServiceManager dispatchServiceManager,
                                         @Nonnull UploadFileDialogControllerFactory uploadFileDialogControllerFactory) {
        this.mergeWorkflow = checkNotNull(mergeWorkflow);
        this.dispatchServiceManager = dispatchServiceManager;
        this.uploadFileDialogControllerFactory = uploadFileDialogControllerFactory;
    }

    public void start(ProjectId projectId) {
        uploadProject(projectId);
    }

    private void uploadProject(final ProjectId projectId) {
        UploadFileDialogController uploadFileDialogController = uploadFileDialogControllerFactory.create(
                "Upload ontologies", new UploadFileResultHandler() {
                    @Override
                    public void handleFileUploaded(DocumentId fileDocumentId) {
                        startMergeWorkflow(projectId, fileDocumentId);
                    }

                    @Override
                    public void handleFileUploadFailed(String errorMessage) {
                        GWT.log("Upload failed");
                    }
                }
        );
        WebProtegeDialog.showDialog(uploadFileDialogController);
    }

    private void startMergeWorkflow(ProjectId projectId, DocumentId documentId) {
        mergeWorkflow.start(projectId, documentId);
    }

}
