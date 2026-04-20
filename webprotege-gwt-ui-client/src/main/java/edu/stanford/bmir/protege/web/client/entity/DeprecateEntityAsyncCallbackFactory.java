package edu.stanford.bmir.protege.web.client.entity;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import javax.inject.Inject;
import javax.inject.Provider;

public final class DeprecateEntityAsyncCallbackFactory {
  private final Provider<DispatchErrorMessageDisplay> errorMessageDisplayProvider;

  private final Provider<ProgressDisplay> progressDisplayProvider;

  @Inject
  public DeprecateEntityAsyncCallbackFactory(
      Provider<DispatchErrorMessageDisplay> errorMessageDisplayProvider,
      Provider<ProgressDisplay> progressDisplayProvider) {
    this.errorMessageDisplayProvider = checkNotNull(errorMessageDisplayProvider, 1);
    this.progressDisplayProvider = checkNotNull(progressDisplayProvider, 2);
  }

  public DeprecateEntityAsyncCallback create(String entityRendering) {
    return new DeprecateEntityAsyncCallback(
        checkNotNull(errorMessageDisplayProvider.get(), 1),
        checkNotNull(progressDisplayProvider.get(), 2),
        checkNotNull(entityRendering, 3));
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
