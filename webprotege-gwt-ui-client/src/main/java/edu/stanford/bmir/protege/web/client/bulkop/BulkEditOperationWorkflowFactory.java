package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import javax.inject.Inject;
import javax.inject.Provider;

public final class BulkEditOperationWorkflowFactory {
  private final Provider<DispatchServiceManager> dispatchProvider;

  private final Provider<BulkEditOperationViewContainer> viewContainerProvider;

  private final Provider<CommitMessageInputView> commitMessageInputViewProvider;

  private final Provider<ModalManager> modalManagerProvider;

  @Inject
  public BulkEditOperationWorkflowFactory(
      Provider<DispatchServiceManager> dispatchProvider,
      Provider<BulkEditOperationViewContainer> viewContainerProvider,
      Provider<CommitMessageInputView> commitMessageInputViewProvider,
      Provider<ModalManager> modalManagerProvider) {
    this.dispatchProvider = checkNotNull(dispatchProvider, 1);
    this.viewContainerProvider = checkNotNull(viewContainerProvider, 2);
    this.commitMessageInputViewProvider = checkNotNull(commitMessageInputViewProvider, 3);
    this.modalManagerProvider = checkNotNull(modalManagerProvider, 4);
  }

  public BulkEditOperationWorkflow create(
      BulkEditOperationPresenter presenter, ImmutableSet<OWLEntityData> entities) {
    return new BulkEditOperationWorkflow(
        checkNotNull(dispatchProvider.get(), 1),
        checkNotNull(viewContainerProvider.get(), 2),
        checkNotNull(presenter, 3),
        checkNotNull(entities, 4),
        checkNotNull(commitMessageInputViewProvider.get(), 5),
        checkNotNull(modalManagerProvider.get(), 6));
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
