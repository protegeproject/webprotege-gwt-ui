package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBoxWithReasonForChange;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.MoveHierarchyNodeIcdAction;
import edu.stanford.bmir.protege.web.shared.issues.CreateEntityDiscussionThreadAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeNodeDropHandler;
import edu.stanford.protege.gwt.graphtree.shared.DropType;
import edu.stanford.protege.gwt.graphtree.shared.Path;
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

    private final MessageBoxWithReasonForChange messageBoxWithReasonForChange;

    @Nonnull
    private final MessageBox messageBox;

    private final Messages messages;

    private final UuidV4Provider uuidV4Provider;

    @Inject
    public EntityHierarchyDropHandler(@Nonnull ProjectId projectId,
                                      @Nonnull DispatchServiceManager dispatchServiceManager,
                                      @Nonnull MessageBoxWithReasonForChange messageBoxWithReasonForChange,
                                      @Nonnull MessageBox messageBox,
                                      UuidV4Provider uuidV4Provider,
                                      @Nonnull Messages messages) {
        this.projectId = checkNotNull(projectId);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.messageBoxWithReasonForChange = messageBoxWithReasonForChange;
        this.messageBox = messageBox;
        this.messages = messages;
        this.uuidV4Provider = uuidV4Provider;
    }

    @Nonnull
    private Optional<HierarchyDescriptor> hierarchyDescriptor = Optional.empty();


    public void start(@Nonnull HierarchyDescriptor hierarchyDescriptor) {
        this.hierarchyDescriptor = Optional.of(hierarchyDescriptor);
    }

    @Override
    public boolean isDropPossible(@Nonnull Path<EntityNode> nodePath,
                                  @Nonnull Path<EntityNode> targetPath,
                                  @Nonnull DropType dropType) {
        if (!hierarchyDescriptor.isPresent()) {
            return false;
        }
        if(nodePath.isEmpty()) {
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
        if (!hierarchyDescriptor.isPresent()) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        if(nodePath.isEmpty()) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        if(nodePath.getLast().map(EntityNode::getEntity).map(OWLObject::isTopEntity).orElse(false)) {
            dropEndHandler.handleDropCancelled();
            return;
        }
        // Don't drop on self
        if(targetPath.getLast().equals(nodePath.getLast())) {
            dropEndHandler.handleDropCancelled();
            return;
        }

        Optional<EntityNode> parentEntityOptional = targetPath.getLast();
        String parentEntityBrowserText = parentEntityOptional.map(EntityNode::getBrowserText).orElse("");
        String entityBrowserText = nodePath.getLast().get().getBrowserText();
        messageBoxWithReasonForChange.showConfirmBoxWithReasonForChange(messages.classHierarchy_dragAndDropEntityTitle(),
                messages.classHierarchy_dragAndDropEntityMessage(entityBrowserText, parentEntityBrowserText),
                DialogButton.CANCEL,
                dropEndHandler::handleDropCancelled,
                DialogButton.YES,
                (reasonForChangeText) -> dispatchServiceManager.execute(MoveHierarchyNodeIcdAction.create(projectId,
                                hierarchyDescriptor.get(),
                                nodePath,
                                targetPath,
                                dropType,
                                reasonForChangeText,
                                ChangeRequestId.get(uuidV4Provider.get())),
                        moveResult -> {
                            if (moveResult.isMoved()) {
                                dropEndHandler.handleDropComplete();
                                dispatchServiceManager.execute(
                                        CreateEntityDiscussionThreadAction.create(projectId, nodePath.getLast().get().getEntity(), reasonForChangeText),
                                        threadActionResult -> {
                                        });
                                return;
                            }
                            if (moveResult.isDestinationRetiredClass() || (moveResult.getReleasedChildrenValidationMessage() != null && !moveResult.getReleasedChildrenValidationMessage().isEmpty())) {
                                String errorMessage = messages.classHierarchy_cannotMoveReleasedClassToRetiredParent();
                                if(moveResult.getReleasedChildrenValidationMessage() != null && !moveResult.getReleasedChildrenValidationMessage().isEmpty()) {
                                    errorMessage = errorMessage + ". " + moveResult.getReleasedChildrenValidationMessage();
                                }
                                messageBox.showMessage(errorMessage);
                            } else if (moveResult.isInitialParentLinPathParent()) {
                                messageBox.showMessage(
                                        messages.classHierarchy_removeParentThatIsLinearizationPathParent(
                                                nodePath.getLastPredecessor().get().getBrowserText()
                                        ).asString()
                                );
                            }
                            dropEndHandler.handleDropCancelled();
                        })
        );
    }

    @Override
    public void handleTextDrop(@Nonnull String s,
                               @Nonnull Path<EntityNode> path,
                               @Nonnull DropType dropType,
                               @Nonnull DropEndHandler dropEndHandler) {

    }
}
