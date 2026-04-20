package edu.stanford.bmir.protege.web.client.chgpwd;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import javax.inject.Inject;
import javax.inject.Provider;

public final class ChangePasswordPresenterFactory {
  private final Provider<ChangePasswordView> changePasswordViewProvider;

  private final Provider<DispatchServiceManager> dispatchServiceManagerProvider;

  private final Provider<MessageBox> messageBoxProvider;

  private final Provider<DispatchErrorMessageDisplay> errorDisplayProvider;

  private final Provider<ProgressDisplay> progressDisplayProvider;

  private final Provider<ModalManager> modalManagerProvider;

  private final Provider<Messages> messagesProvider;

  @Inject
  public ChangePasswordPresenterFactory(
      Provider<ChangePasswordView> changePasswordViewProvider,
      Provider<DispatchServiceManager> dispatchServiceManagerProvider,
      Provider<MessageBox> messageBoxProvider,
      Provider<DispatchErrorMessageDisplay> errorDisplayProvider,
      Provider<ProgressDisplay> progressDisplayProvider,
      Provider<ModalManager> modalManagerProvider,
      Provider<Messages> messagesProvider) {
    this.changePasswordViewProvider = checkNotNull(changePasswordViewProvider, 1);
    this.dispatchServiceManagerProvider = checkNotNull(dispatchServiceManagerProvider, 2);
    this.messageBoxProvider = checkNotNull(messageBoxProvider, 3);
    this.errorDisplayProvider = checkNotNull(errorDisplayProvider, 4);
    this.progressDisplayProvider = checkNotNull(progressDisplayProvider, 5);
    this.modalManagerProvider = checkNotNull(modalManagerProvider, 6);
    this.messagesProvider = checkNotNull(messagesProvider, 7);
  }

  public ChangePasswordPresenter create(UserId userId) {
    return new ChangePasswordPresenter(
        checkNotNull(changePasswordViewProvider.get(), 1),
        checkNotNull(userId, 2),
        checkNotNull(dispatchServiceManagerProvider.get(), 3),
        checkNotNull(messageBoxProvider.get(), 4),
        checkNotNull(errorDisplayProvider.get(), 5),
        checkNotNull(progressDisplayProvider.get(), 6),
        checkNotNull(modalManagerProvider.get(), 7),
        checkNotNull(messagesProvider.get(), 8));
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
