package edu.stanford.bmir.protege.web.client.project;

import edu.stanford.bmir.protege.web.client.hierarchy.UploadAndProcessSiblingsOrderingWorkflow;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class UploadAndProcessSiblingsOrderingHandlerImpl implements UploadAndProcessSiblingsOrderingHandler {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Provider<UploadAndProcessSiblingsOrderingWorkflow> workflowProvider;

    @Inject
    public UploadAndProcessSiblingsOrderingHandlerImpl(@Nonnull ProjectId projectId,
                                                       @Nonnull Provider<UploadAndProcessSiblingsOrderingWorkflow> workflowProvider) {

        this.projectId = checkNotNull(projectId);
        this.workflowProvider = checkNotNull(workflowProvider);
    }

    @Override
    public void handleUploadSiblingsOrdering() {
        UploadAndProcessSiblingsOrderingWorkflow uploadAndProcessSiblingsOrderingWorkflow = workflowProvider.get();
        uploadAndProcessSiblingsOrderingWorkflow.start(this.projectId);
    }

}
