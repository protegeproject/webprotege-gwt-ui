package edu.stanford.bmir.protege.web.client.projectmanager;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedFromTrashEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedToTrashEvent;
import edu.stanford.bmir.protege.web.shared.project.MoveProjectsToTrashAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.RemoveProjectFromTrashAction;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class TrashManagerRequestHandlerImpl implements TrashManagerRequestHandler {

    private final DispatchServiceManager dispatchServiceManager;

    private final EventBus eventBus;

    @Inject
    public TrashManagerRequestHandlerImpl(DispatchServiceManager dispatchServiceManager,
                                          EventBus eventBus) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.eventBus = eventBus;
    }

    @Override
    public void handleMoveProjectToTrash(final ProjectId projectId) {
        // Fire ProjectMovedToTrashEvent on the local bus when the dispatch
        // returns. Without this the originating client never sees the row
        // disappear from the default project list — there is no server-
        // side fan-out of this event in this repo, and the only listener
        // (ProjectManagerPresenter.handleProjectMovedToTrash) updates the
        // local AvailableProjectsCache from a bus event. Mirrors how
        // CreateNewProjectPresenter fires ProjectCreatedEvent on success.
        dispatchServiceManager.execute(MoveProjectsToTrashAction.create(projectId),
                                       result -> eventBus.fireEvent(new ProjectMovedToTrashEvent(projectId).asGWTEvent()));
    }

    @Override
    public void handleRemoveProjectFromTrash(final ProjectId projectId) {
        dispatchServiceManager.execute(RemoveProjectFromTrashAction.create(projectId),
                                       result -> eventBus.fireEvent(new ProjectMovedFromTrashEvent(projectId).asGWTEvent()));
    }
}
