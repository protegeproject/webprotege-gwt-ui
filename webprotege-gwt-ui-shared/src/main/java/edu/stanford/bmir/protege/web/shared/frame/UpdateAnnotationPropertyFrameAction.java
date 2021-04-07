package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class UpdateAnnotationPropertyFrameAction extends UpdateFrameAction {

    @GwtSerializationConstructor
    private UpdateAnnotationPropertyFrameAction() {
    }

    private UpdateAnnotationPropertyFrameAction(ProjectId projectId,
                                                PlainAnnotationPropertyFrame from,
                                                PlainAnnotationPropertyFrame to) {
        super(projectId, from, to);
    }

    public static UpdateAnnotationPropertyFrameAction create(ProjectId projectId,
                                                             PlainAnnotationPropertyFrame from,
                                                             PlainAnnotationPropertyFrame to) {
        return new UpdateAnnotationPropertyFrameAction(projectId, from, to);
    }
}
