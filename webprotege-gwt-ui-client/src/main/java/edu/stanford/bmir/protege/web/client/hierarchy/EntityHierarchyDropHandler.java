package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.*;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeNodeDropHandler;
import edu.stanford.protege.gwt.graphtree.shared.*;
import org.semanticweb.owlapi.model.OWLObject;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class EntityHierarchyDropHandler implements TreeNodeDropHandler<EntityNode> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final MessageBox messageBox;

    private final Messages messages;

    @Inject
    public EntityHierarchyDropHandler(@Nonnull ProjectId projectId,
                                      @Nonnull DispatchServiceManager dispatchServiceManager,
                                      @Nonnull MessageBox messageBox,
                                      @Nonnull Messages messages) {
        this.projectId = checkNotNull(projectId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.messageBox = checkNotNull(messageBox);
        this.messages = checkNotNull(messages);
    }

    @Nonnull
    private Optional<HierarchyId> hierarchyId = Optional.empty();


    public void start(@Nonnull HierarchyId hierarchyId) {
        this.hierarchyId = Optional.of(hierarchyId);
    }

    @Override
    public boolean isDropPossible(@Nonnull Path<EntityNode> nodePath,
                                  @Nonnull Path<EntityNode> targetPath,
                                  @Nonnull DropType dropType) {
        if (!hierarchyId.isPresent()) {
            return false;
        }
        if (nodePath.isEmpty()) {
            return false;
        }
        // Don't drop on self
        return !targetPath.getLast().equals(nodePath.getLast());
    }

    @Override
    public boolean isTextDropPossible(@Nonnull Path<EntityNode> path,
                                      @Nonnull DropType dropType) {
        return false;
    }

    @Override
    public void handleDrop(@Nonnull Path<EntityNode> nodePath,
                           @Nonnull Path<EntityNode> targetPath,
                           @Nonnull DropType dropType,
                           @Nonnull DropEndHandler dropEndHandler) {
        GWT.log("[EntityHierarchyDropHandler] handleDrop. From: " + nodePath + " To: " + nodePath);
        if (!hierarchyId.isPresent()) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        if (nodePath.isEmpty()) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        if (nodePath.getLast().map(EntityNode::getEntity).map(OWLObject::isTopEntity).orElse(false)) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        // Don't drop on self
        if (targetPath.getLast().equals(nodePath.getLast())) {
            dropEndHandler.handleDropCancelled();
            return;
        }

        YesNoHandler yesNoHandler = new YesNoHandler() {
            @Override
            public void handleYes() {
                dispatchServiceManager.execute(MoveHierarchyNodeAction.create(projectId,
                                hierarchyId.get(),
                                nodePath,
                                targetPath,
                                dropType),
                        moveResult -> {
                            if (moveResult.isMoved()) {
                                dropEndHandler.handleDropComplete();
                            } else {
                                if (moveResult.isDestinationRetiredClass()) {
                                    messageBox.showMessage(messages.classHierarchy_cannotMoveReleasedClassToRetiredParent());
                                }
                                dropEndHandler.handleDropCancelled();
                            }
                        }
                );
            }

            @Override
            public void handleNo() {
                dropEndHandler.handleDropCancelled();
            }
        };

        messageBox.showYesNoConfirmBox("Move entities?",
                "You are about to move selected entities to new parent. Are you sure?",
                yesNoHandler
        );
    }

    @Override
    public void handleTextDrop(@Nonnull String s,
                               @Nonnull Path<EntityNode> path,
                               @Nonnull DropType dropType,
                               @Nonnull DropEndHandler dropEndHandler) {

    }
}
