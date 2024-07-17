package edu.stanford.bmir.protege.web.client.project;

import edu.stanford.bmir.protege.web.client.linearization.UploadAndProcessLinearizationWorkflow;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class UploadAndProcessLinearizationHandlerImpl implements UploadAndProcessLinearizationHandler {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Provider<UploadAndProcessLinearizationWorkflow> workflowProvider;

    @Inject
    public UploadAndProcessLinearizationHandlerImpl(@Nonnull ProjectId projectId,
                                                    @Nonnull Provider<UploadAndProcessLinearizationWorkflow> workflowProvider) {

        this.projectId = checkNotNull(projectId);
        this.workflowProvider = checkNotNull(workflowProvider);
    }

    @Override
    public void handleUploadLinearizationChanges() {
        UploadAndProcessLinearizationWorkflow uploadAndProcessLinearizationWorkflow = workflowProvider.get();
        uploadAndProcessLinearizationWorkflow.start(this.projectId);
    }
}
