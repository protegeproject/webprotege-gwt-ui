package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class UpdateFrameAction<R extends Result> extends AbstractHasProjectAction<R> {

    public abstract PlainEntityFrame getFrom();

    public abstract PlainEntityFrame getTo();

    /**
     * Identifies this change request. The backend requires it on every frame
     * update: without one, computing the hierarchy-changed events for edits
     * that touch parents fails server-side and the whole edit is rejected.
     */
    public abstract ChangeRequestId getChangeRequestId();
}
