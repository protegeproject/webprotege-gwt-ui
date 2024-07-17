package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.dispatch.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProcessUploadedLinerizationWorkflow {


    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    @Inject
    public ProcessUploadedLinerizationWorkflow(@Nonnull DispatchServiceManager dispatchServiceManager,
                                               @Nonnull MessageBox messageBox,
                                               @Nonnull DispatchErrorMessageDisplay errorDisplay,
                                               @Nonnull ProgressDisplay progressDisplay) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.messageBox = checkNotNull(messageBox);
        this.errorDisplay = checkNotNull(errorDisplay);
        this.progressDisplay = checkNotNull(progressDisplay);
    }


    public void start(ProjectId projectId, DocumentId documentId) {
        processLinearization(projectId, documentId);
    }


    private void processLinearization(final ProjectId projectId, final DocumentId uploadedProjectDocumentId) {
        dispatchServiceManager.execute(
                ProcessUploadedLinearizationAction.create(projectId, uploadedProjectDocumentId),
                new DispatchServiceCallbackWithProgressDisplay<ProcessUploadedLinearizationResult>(errorDisplay, progressDisplay) {

                    @Override
                    public String getProgressDisplayTitle() {
                        return "Process linearization";
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return "Applying linearization changes to ontologies.  Please wait.";
                    }

                    @Override
                    public void handleSuccess(ProcessUploadedLinearizationResult processUploadedLinearizationResult) {
                        messageBox.showMessage("The linearization where succesfully processes and added to the project");
                    }
                }
        );
    }
}
