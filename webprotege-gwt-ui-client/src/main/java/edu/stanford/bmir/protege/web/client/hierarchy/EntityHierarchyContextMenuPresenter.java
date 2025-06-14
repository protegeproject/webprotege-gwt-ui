package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.bulkop.EditAnnotationsUiAction;
import edu.stanford.bmir.protege.web.client.bulkop.MoveToParentUiAction;
import edu.stanford.bmir.protege.web.client.bulkop.SetAnnotationValueUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.ChangeChildrenOrderingUIAction;
import edu.stanford.bmir.protege.web.client.entity.MergeEntitiesUiAction;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.tag.EditEntityTagsUiAction;
import edu.stanford.bmir.protege.web.client.watches.WatchUiAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import edu.stanford.protege.gwt.graphtree.shared.tree.TreeNode;
import edu.stanford.protege.gwt.graphtree.shared.tree.impl.GraphTreeNodeModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.*;
import static edu.stanford.protege.gwt.graphtree.shared.tree.RevealMode.REVEAL_FIRST;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 3 Dec 2017
 */
@AutoFactory
public class EntityHierarchyContextMenuPresenter {

    @Nonnull
    private final EditAnnotationsUiAction editAnnotationsUiAction;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final TreeWidget<EntityNode, OWLEntity> treeWidget;

    @Nonnull
    private final EntityHierarchyModel model;

    @Nonnull
    private final UIAction createEntityAction;

    @Nonnull
    private final UIAction deleteEntityAction;

    @Nonnull
    private final SetAnnotationValueUiAction setAnnotationValueUiAction;

    @Nonnull
    private final MoveToParentUiAction moveToParentUiAction;

    @Nonnull
    private final ChangeChildrenOrderingUIAction changeChildrenOrderingUIAction;

    @Nonnull
    private final MergeEntitiesUiAction mergeEntitiesAction;

    @Nonnull
    private final EditEntityTagsUiAction editEntityTagsAction;

    @Nonnull
    private final WatchUiAction watchUiAction;

    @Nonnull
    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    @Nullable
    private PopupMenu contextMenu;

    private UIAction pruneBranchToRootAction;

    private UIAction pruneAllBranchesToRootAction;

    private UIAction clearPruningAction;

    private UIAction showIriAction;

    private UIAction showDirectLinkAction;

    private final InputBox inputBox;

    private DispatchServiceManager dispatch;
    private ProjectId projectId;

    public EntityHierarchyContextMenuPresenter(@Nonnull EntityHierarchyModel model,
                                               @Nonnull TreeWidget<EntityNode, OWLEntity> treeWidget,
                                               @Nonnull UIAction createEntityAction,
                                               @Nonnull UIAction deleteEntityAction,
                                               @Nonnull ProjectId projectId,
                                               @Provided @Nonnull DispatchServiceManager dispatch,
                                               @Provided @Nonnull SetAnnotationValueUiAction setAnnotationValueUiAction,
                                               @Provided @Nonnull MoveToParentUiAction moveToParentUiAction,
                                               @Provided @Nonnull ChangeChildrenOrderingUIAction changeChildrenOrderingUIAction,
                                               @Provided @Nonnull MergeEntitiesUiAction mergeEntitiesAction,
                                               @Provided @Nonnull EditAnnotationsUiAction editAnnotationsUiAction,
                                               @Provided @Nonnull EditEntityTagsUiAction editEntityTagsAction,
                                               @Provided Messages messages,
                                               @Provided @Nonnull WatchUiAction watchUiAction,
                                               @Provided @Nonnull LoggedInUserProjectCapabilityChecker capabilityChecker,
                                               @Provided @Nonnull InputBox inputBox) {
        this.projectId = projectId;
        this.dispatch = dispatch;
        this.setAnnotationValueUiAction = checkNotNull(setAnnotationValueUiAction);
        this.moveToParentUiAction = checkNotNull(moveToParentUiAction);
        this.editAnnotationsUiAction = checkNotNull(editAnnotationsUiAction);
        this.messages = checkNotNull(messages);
        this.treeWidget = checkNotNull(treeWidget);
        this.model = checkNotNull(model);
        this.createEntityAction = checkNotNull(createEntityAction);
        this.deleteEntityAction = checkNotNull(deleteEntityAction);
        this.mergeEntitiesAction = checkNotNull(mergeEntitiesAction);
        this.editEntityTagsAction = checkNotNull(editEntityTagsAction);
        this.changeChildrenOrderingUIAction = changeChildrenOrderingUIAction;
        this.watchUiAction = checkNotNull(watchUiAction);
        this.capabilityChecker = checkNotNull(capabilityChecker);
        this.inputBox = checkNotNull(inputBox);
    }

    /**
     * Install the context menu on its tree
     */
    public void install() {
        treeWidget.addContextMenuHandler(this::showContextMenu);
    }

    private void showContextMenu(ContextMenuEvent event) {
        if (contextMenu == null) {
            createContextMenu();
        }
        updateActionStates();
        int x = event.getNativeEvent().getClientX();
        int y = event.getNativeEvent().getClientY();
        contextMenu.show(x, y);
    }

    private void createContextMenu() {
        contextMenu = new PopupMenu();
        contextMenu.addItem(createEntityAction);
        contextMenu.addItem(deleteEntityAction);
        contextMenu.addSeparator();
        contextMenu.addItem(editEntityTagsAction);
        contextMenu.addSeparator();
        contextMenu.addItem(moveToParentUiAction);
        contextMenu.addItem(changeChildrenOrderingUIAction);
        contextMenu.addItem(mergeEntitiesAction);
        contextMenu.addItem(setAnnotationValueUiAction);
        contextMenu.addItem(editAnnotationsUiAction);
        contextMenu.addSeparator();
        contextMenu.addItem(watchUiAction);
        contextMenu.addSeparator();
        pruneBranchToRootAction = contextMenu.addItem(messages.tree_pruneBranchToRoot(), this::pruneSelectedNodesToRoot);
        pruneAllBranchesToRootAction = contextMenu.addItem(messages.tree_pruneAllBranchesToRoot(), this::pruneToKey);
        clearPruningAction = contextMenu.addItem(messages.tree_clearPruning(), this::clearPruning);
        contextMenu.addSeparator();
        showIriAction = contextMenu.addItem(messages.showIri(), this::showIriForSelection);
        showDirectLinkAction = contextMenu.addItem(messages.showDirectLink(), this::showUrlForSelection);
        contextMenu.addSeparator();
        contextMenu.addItem(messages.refreshTree(), this::handleRefresh);

        // This needs tidying somehow.  We don't do this for other actions.
        moveToParentUiAction.setHierarchyDescriptor(model.getHierarchyDescriptor());
        mergeEntitiesAction.setHierarchyDescriptor(model.getHierarchyDescriptor());
        Supplier<ImmutableSet<OWLEntityData>> selectionSupplier = () ->
                treeWidget.getSelectedNodes().stream()
                        .map(TreeNode::getUserObject)
                        .map(EntityNode::getEntityData)
                        .collect(toImmutableSet());
        setAnnotationValueUiAction.setSelectionSupplier(selectionSupplier);
        moveToParentUiAction.setSelectionSupplier(selectionSupplier);
        changeChildrenOrderingUIAction.setSelectionSupplier(selectionSupplier);
        changeChildrenOrderingUIAction.setHandleAfterSave(v -> this.handleRefresh());
        mergeEntitiesAction.setSelectionSupplier(selectionSupplier);
        editAnnotationsUiAction.setSelectionSupplier(selectionSupplier);

        updateActionStates();
    }

    private void updateActionStates() {
        mergeEntitiesAction.setEnabled(false);
        editEntityTagsAction.setEnabled(false);
        setAnnotationValueUiAction.setEnabled(false);
        editAnnotationsUiAction.setEnabled(false);
        moveToParentUiAction.setEnabled(false);
        watchUiAction.setEnabled(false);
        changeChildrenOrderingUIAction.setEnabled(true);
        int selSize = treeWidget.getSelectedKeys().size();
        boolean selIsNonEmpty = selSize > 0;
        boolean selIsSingleton = selSize == 1;
        pruneBranchToRootAction.setEnabled(selIsSingleton);
        pruneAllBranchesToRootAction.setEnabled(selIsSingleton);
        clearPruningAction.setEnabled(selIsSingleton);
        showIriAction.setEnabled(selIsSingleton);
        showDirectLinkAction.setEnabled(selIsSingleton);


        if (selIsNonEmpty) {
            capabilityChecker.hasCapability(WATCH_CHANGES, watchUiAction::setEnabled);
            capabilityChecker.hasCapability(MERGE_ENTITIES, mergeEntitiesAction::setEnabled);
            capabilityChecker.hasCapability(EDIT_ENTITY_TAGS, enabled -> editEntityTagsAction.setEnabled(selIsSingleton && enabled));
            capabilityChecker.hasCapability(EDIT_ONTOLOGY, setAnnotationValueUiAction::setEnabled);
            capabilityChecker.hasCapability(EDIT_ONTOLOGY, editAnnotationsUiAction::setEnabled);
            capabilityChecker.hasCapability(EDIT_ONTOLOGY, moveToParentUiAction::setEnabled);
        }
    }

    private void pruneSelectedNodesToRoot() {
        treeWidget.pruneToSelectedNodes();
    }

    private void pruneToKey() {
        treeWidget.getFirstSelectedKey().ifPresent(sel -> treeWidget.pruneToNodesContainingKey(sel, () -> {
        }));
    }

    private void clearPruning() {
        treeWidget.clearPruning();
    }


    private void showIriForSelection() {
        treeWidget.getFirstSelectedKey().ifPresent(sel -> {
            String iri = sel.getIRI().toString();
            inputBox.showOkDialog(messages.classIri(), true, iri, input -> {});
        });
    }

    private void showUrlForSelection() {
        String location = Window.Location.getHref();
        int fragmentIndex = location.indexOf("#");
        String fragment = location.substring(fragmentIndex + 1);
        String encodedFragment = URL.encodeQueryString(fragment);
        String rewritten = location.substring(0, fragmentIndex) + "?fragment=" + encodedFragment;
        inputBox.showOkDialog(messages.directLink(), true, rewritten, input -> {});
    }

    private void handleRefresh() {
        Optional<OWLEntity> firstSelectedKey = treeWidget.getFirstSelectedKey();
        treeWidget.setModel(GraphTreeNodeModel.create(model, EntityNode::getEntity));
        firstSelectedKey.ifPresent(sel -> treeWidget.revealTreeNodesForKey(sel, REVEAL_FIRST));
    }

}
