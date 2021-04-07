package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UpdateNamedIndividualFrameAction extends UpdateFrameAction {

    /**
     * For serialization purposes only
     */
    private UpdateNamedIndividualFrameAction() {
    }

    private UpdateNamedIndividualFrameAction(ProjectId projectId,
                                             PlainNamedIndividualFrame from,
                                             PlainNamedIndividualFrame to) {
        super(projectId, from, to);
    }

    public static UpdateNamedIndividualFrameAction create(ProjectId projectId,
                                                          PlainNamedIndividualFrame from,
                                                          PlainNamedIndividualFrame to) {
        return new UpdateNamedIndividualFrameAction(projectId, from, to);
    }
}
