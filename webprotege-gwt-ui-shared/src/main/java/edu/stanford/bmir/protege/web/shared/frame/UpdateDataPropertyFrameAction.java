package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateDataPropertyFrameAction extends UpdateFrameAction implements HasProjectId {

    private UpdateDataPropertyFrameAction() {
    }

    private UpdateDataPropertyFrameAction(ProjectId projectId, PlainDataPropertyFrame from, PlainDataPropertyFrame to) {
        super(projectId, from, to);
    }

    public static UpdateDataPropertyFrameAction create(ProjectId projectId,
                                                       PlainDataPropertyFrame from,
                                                       PlainDataPropertyFrame to) {
        return new UpdateDataPropertyFrameAction(projectId, from, to);
    }
}
