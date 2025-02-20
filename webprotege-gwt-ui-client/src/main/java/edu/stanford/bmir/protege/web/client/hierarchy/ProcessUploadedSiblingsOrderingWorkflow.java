package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProcessUploadedSiblingsOrderingWorkflow {

    private static final Messages MESSAGES = GWT.create(Messages.class);

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    @Inject
    public ProcessUploadedSiblingsOrderingWorkflow(@Nonnull DispatchServiceManager dispatchServiceManager,
                                                   @Nonnull MessageBox messageBox,
                                                   @Nonnull DispatchErrorMessageDisplay errorDisplay,
                                                   @Nonnull ProgressDisplay progressDisplay) {
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.messageBox = checkNotNull(messageBox);
        this.errorDisplay = checkNotNull(errorDisplay);
        this.progressDisplay = checkNotNull(progressDisplay);
    }


    public void start(ProjectId projectId, DocumentId documentId) {
        processSiblingsOrdering(projectId, documentId);
    }


    private void processSiblingsOrdering(final ProjectId projectId, final DocumentId uploadedProjectDocumentId) {
        dispatchServiceManager.execute(
                ProcessUploadedSiblingsOrderingAction.create(projectId, uploadedProjectDocumentId),
                new DispatchServiceCallbackWithProgressDisplay<ProcessUploadedSiblingsOrderingResult>(errorDisplay, progressDisplay) {

                    @Override
                    public String getProgressDisplayTitle() {
                        return MESSAGES.processingSiblingsOrdering();
                    }

                    @Override
                    public String getProgressDisplayMessage() {
                        return MESSAGES.applyingSiblingsOrderingToEntities();
                    }

                    @Override
                    public void handleSuccess(ProcessUploadedSiblingsOrderingResult processUploadedSiblingsOrderingResult) {
                        messageBox.showMessage(MESSAGES.siblingsOrderingSuccessfullyProcessed());
                    }
                }
        );
    }
}
