package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedFromTrashEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class RemoveProjectFromTrashResult extends AbstractHasEventListResult<ProjectMovedFromTrashEvent> {

    private RemoveProjectFromTrashResult() {
    }

    private RemoveProjectFromTrashResult(EventList<ProjectMovedFromTrashEvent> eventList) {
        super(eventList);
    }

    public static RemoveProjectFromTrashResult create(EventList<ProjectMovedFromTrashEvent> eventList) {
        return new RemoveProjectFromTrashResult(eventList);
    }
}
