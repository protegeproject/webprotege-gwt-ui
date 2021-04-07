package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class RemoveProjectFromTrashAction implements Action<RemoveProjectFromTrashResult> {

    private ProjectId projectId;

    private RemoveProjectFromTrashAction() {
    }

    private RemoveProjectFromTrashAction(ProjectId projectId) {
        this.projectId = projectId;
    }

    public static RemoveProjectFromTrashAction create(ProjectId projectId) {
        return new RemoveProjectFromTrashAction(projectId);
    }

    public ProjectId getProjectId() {
        return projectId;
    }
}
