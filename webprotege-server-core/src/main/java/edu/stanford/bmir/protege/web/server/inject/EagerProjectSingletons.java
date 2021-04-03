package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-02
 *
 * This isn't used for anything other than to force the eager instantiation of certain objects
 * in the project component object graph.
 */
@ProjectSingleton
public class EagerProjectSingletons {

    private final ProjectId projectId;

    @Inject
    public EagerProjectSingletons(ProjectId projectId) {
        this.projectId = projectId;
    }
}
