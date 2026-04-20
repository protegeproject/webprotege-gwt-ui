package edu.stanford.bmir.protege.web.client.hierarchy;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.bulkop.EditAnnotationsUiAction;
import edu.stanford.bmir.protege.web.client.bulkop.MoveToParentUiAction;
import edu.stanford.bmir.protege.web.client.bulkop.SetAnnotationValueUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.ChangeChildrenOrderingUIAction;
import edu.stanford.bmir.protege.web.client.entity.MergeEntitiesUiAction;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.tag.EditEntityTagsUiAction;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.watches.WatchUiAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLEntity;

public final class EntityHierarchyContextMenuPresenterFactory {
  private final Provider<DispatchServiceManager> dispatchProvider;

  private final Provider<SetAnnotationValueUiAction> setAnnotationValueUiActionProvider;

  private final Provider<MoveToParentUiAction> moveToParentUiActionProvider;

  private final Provider<ChangeChildrenOrderingUIAction> changeChildrenOrderingUIActionProvider;

  private final Provider<MergeEntitiesUiAction> mergeEntitiesActionProvider;

  private final Provider<EditAnnotationsUiAction> editAnnotationsUiActionProvider;

  private final Provider<EditEntityTagsUiAction> editEntityTagsActionProvider;

  private final Provider<Messages> messagesProvider;

  private final Provider<WatchUiAction> watchUiActionProvider;

  private final Provider<LoggedInUserProjectCapabilityChecker> capabilityCheckerProvider;

  private final Provider<InputBox> inputBoxProvider;

  private final Provider<LoggedInUserProvider> loggedInUserProviderProvider;

  @Inject
  public EntityHierarchyContextMenuPresenterFactory(
      Provider<DispatchServiceManager> dispatchProvider,
      Provider<SetAnnotationValueUiAction> setAnnotationValueUiActionProvider,
      Provider<MoveToParentUiAction> moveToParentUiActionProvider,
      Provider<ChangeChildrenOrderingUIAction> changeChildrenOrderingUIActionProvider,
      Provider<MergeEntitiesUiAction> mergeEntitiesActionProvider,
      Provider<EditAnnotationsUiAction> editAnnotationsUiActionProvider,
      Provider<EditEntityTagsUiAction> editEntityTagsActionProvider,
      Provider<Messages> messagesProvider,
      Provider<WatchUiAction> watchUiActionProvider,
      Provider<LoggedInUserProjectCapabilityChecker> capabilityCheckerProvider,
      Provider<InputBox> inputBoxProvider,
      Provider<LoggedInUserProvider> loggedInUserProviderProvider) {
    this.dispatchProvider = checkNotNull(dispatchProvider, 1);
    this.setAnnotationValueUiActionProvider = checkNotNull(setAnnotationValueUiActionProvider, 2);
    this.moveToParentUiActionProvider = checkNotNull(moveToParentUiActionProvider, 3);
    this.changeChildrenOrderingUIActionProvider =
        checkNotNull(changeChildrenOrderingUIActionProvider, 4);
    this.mergeEntitiesActionProvider = checkNotNull(mergeEntitiesActionProvider, 5);
    this.editAnnotationsUiActionProvider = checkNotNull(editAnnotationsUiActionProvider, 6);
    this.editEntityTagsActionProvider = checkNotNull(editEntityTagsActionProvider, 7);
    this.messagesProvider = checkNotNull(messagesProvider, 8);
    this.watchUiActionProvider = checkNotNull(watchUiActionProvider, 9);
    this.capabilityCheckerProvider = checkNotNull(capabilityCheckerProvider, 10);
    this.inputBoxProvider = checkNotNull(inputBoxProvider, 11);
    this.loggedInUserProviderProvider = checkNotNull(loggedInUserProviderProvider, 12);
  }

  public EntityHierarchyContextMenuPresenter create(
      EntityHierarchyModel model,
      TreeWidget<EntityNode, OWLEntity> treeWidget,
      UIAction createEntityAction,
      UIAction deleteEntityAction,
      ProjectId projectId) {
    return new EntityHierarchyContextMenuPresenter(
        checkNotNull(model, 1),
        checkNotNull(treeWidget, 2),
        checkNotNull(createEntityAction, 3),
        checkNotNull(deleteEntityAction, 4),
        checkNotNull(projectId, 5),
        checkNotNull(dispatchProvider.get(), 6),
        checkNotNull(setAnnotationValueUiActionProvider.get(), 7),
        checkNotNull(moveToParentUiActionProvider.get(), 8),
        checkNotNull(changeChildrenOrderingUIActionProvider.get(), 9),
        checkNotNull(mergeEntitiesActionProvider.get(), 10),
        checkNotNull(editAnnotationsUiActionProvider.get(), 11),
        checkNotNull(editEntityTagsActionProvider.get(), 12),
        checkNotNull(messagesProvider.get(), 13),
        checkNotNull(watchUiActionProvider.get(), 14),
        checkNotNull(capabilityCheckerProvider.get(), 15),
        checkNotNull(inputBoxProvider.get(), 16),
        checkNotNull(loggedInUserProviderProvider.get(), 17));
  }

  private static <T> T checkNotNull(T reference, int argumentIndex) {
    if (reference == null) {
      throw new NullPointerException(
          "@AutoFactory method argument is null but is not marked @Nullable. Argument index: "
              + argumentIndex);
    }
    return reference;
  }
}
