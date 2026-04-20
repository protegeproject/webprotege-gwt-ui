package edu.stanford.bmir.protege.web.client.upload;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import javax.inject.Inject;
import javax.inject.Provider;

public final class UploadFileDialogControllerFactory {
  private final Provider<DispatchServiceManager> dispatchProvider;

  private final Provider<ProgressDisplay> progressDisplayProvider;

  @Inject
  public UploadFileDialogControllerFactory(
      Provider<DispatchServiceManager> dispatchProvider,
      Provider<ProgressDisplay> progressDisplayProvider) {
    this.dispatchProvider = checkNotNull(dispatchProvider, 1);
    this.progressDisplayProvider = checkNotNull(progressDisplayProvider, 2);
  }

  public UploadFileDialogController create(
      String title, UploadFileResultHandler resultHandler, boolean showOverrideCheckbox) {
    return new UploadFileDialogController(
        checkNotNull(title, 1),
        checkNotNull(resultHandler, 2),
        showOverrideCheckbox,
        checkNotNull(dispatchProvider.get(), 4),
        checkNotNull(progressDisplayProvider.get(), 5));
  }

  private static <T> T checkNotNull(T reference, int argumentIndex) {
    if (reference == null) {
      throw new NullPointerException(
          "Constructor argument is null but is not marked @Nullable. Argument index: "
              + argumentIndex);
    }
    return reference;
  }
}
