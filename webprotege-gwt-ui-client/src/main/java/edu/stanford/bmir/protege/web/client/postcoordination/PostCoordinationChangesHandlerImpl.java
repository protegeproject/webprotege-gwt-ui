package edu.stanford.bmir.protege.web.client.postcoordination;

import edu.stanford.bmir.protege.web.shared.postcoordination.ProcessUploadedPostCoordinationAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

public class PostCoordinationChangesHandlerImpl implements PostCoordinationChangesHandler {


    @Nonnull
    private final Provider<UploadPostCoordinationWorkflow> workflowProvider;
    @Nonnull
    private final ProjectId projectId;

    @Inject
    public PostCoordinationChangesHandlerImpl(@Nonnull Provider<UploadPostCoordinationWorkflow> workflowProvider, @Nonnull ProjectId projectId) {
        this.workflowProvider = workflowProvider;
        this.projectId = projectId;
    }

    @Override
    public void handleUploadPostCoordinationChanges() {
        UploadPostCoordinationWorkflow workflow = workflowProvider.get();

        UploadPostCoordinationWorkflowConfiguration configuration = new UploadPostCoordinationWorkflowConfiguration("Upload PostCoordination changes",
                "Process postcoordination",
                "Applying postcoordination changes to ontologies.  Please wait.",
                "The postcoordination where successfully processes and added to the project",
                ProcessUploadedPostCoordinationAction::create);

        workflow.start(projectId, configuration);

    }
}
