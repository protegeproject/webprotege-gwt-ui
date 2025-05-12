package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.*;
import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 6 Dec 2017
 */
public class DeleteEntitiesPresenter {

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UuidV4Provider uuidV4Provider;


    @Inject
    public DeleteEntitiesPresenter(@Nonnull Messages messages,
                                   @Nonnull MessageBox messageBox,
                                   @Nonnull DispatchServiceManager dispatchServiceManager,
                                   @Nonnull ProjectId projectId,
                                   @Nonnull UuidV4Provider uuidV4Provider) {
        this.messages = checkNotNull(messages);
        this.messageBox = checkNotNull(messageBox);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
        this.uuidV4Provider = uuidV4Provider;
    }

    /**
     * Start the deletion of the entities represented by the selected nodes in the specified tree.
     *
     * @param treeWidget The tree.
     */
    public void start(@Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget) {
        final Set<TreeNode<EntityNode>> currentSelection = treeWidget.getSelectedNodes();
        if (currentSelection.isEmpty()) {
            return;
        }
        Set<OWLEntityData> entities = currentSelection.stream()
                .map(TreeNode::getUserObject)
                .map(EntityNode::getEntityData)
                .collect(toImmutableSet());
        messageBox.showConfirmBox(getDeleteConfirmationTitle(entities),
                getDeleteConfirmationMessage(entities),
                CANCEL, DELETE,
                () -> deleteEntity(entities, treeWidget),
                CANCEL);
    }

    private String getDeleteConfirmationTitle(@Nonnull Set<OWLEntityData> entities) {
        if (entities.size() == 1) {
            return entities.stream()
                    .findFirst()
                    .map(this::getDeleteConfirmationTitle)
                    .orElse("");
        } else {
            return getMultiDeleteConfirmationTitle(entities);
        }

    }

    @Nonnull
    private String getDeleteConfirmationMessage(@Nonnull Set<OWLEntityData> entities) {
        String typeName = entities.stream()
                .findFirst()
                .map(OWLEntityData::getEntity)
                .map(OWLEntity::getEntityType)
                .map(type -> entities.size() > 1 ? type.getPluralPrintName() : type.getPrintName())
                .map(String::toLowerCase)
                .orElse("");
        String browserText = entities.stream()
                .map(OWLEntityData::getBrowserText)
                .limit(30)
                .collect(joining(", "));
        return messages.delete_entity_msg(typeName,
                browserText);
    }

    @Nonnull
    private String getDeleteConfirmationTitle(@Nonnull OWLEntityData entity) {
        return messages.delete_entity_title(entity.getBrowserText());
    }

    @Nonnull
    private String getMultiDeleteConfirmationTitle(@Nonnull Set<OWLEntityData> entities) {
        String type = entities.stream()
                .findFirst()
                .map(e -> e.getEntity().getEntityType())
                .map(EntityType::getPluralPrintName)
                .orElse("Entities");
        return messages.delete_entity_title(type);
    }

    private void deleteEntity(@Nonnull Set<OWLEntityData> entities,
                              @Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget) {
        treeWidget.moveSelectionDown();
        ImmutableSet<OWLEntityData> entitiesToDelete = entities.stream().collect(toImmutableSet());
        dispatchServiceManager.execute(
                new DeleteEntitiesAction(
                        ChangeRequestId.get(uuidV4Provider.get()),
                        projectId,
                        entitiesToDelete),
                deleteEntityResult -> {
                });
    }

}
