package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import javax.inject.Inject;
import javax.inject.Provider;

public final class FormPresenterFactory {
  private final Provider<FormView> formViewProvider;

  private final Provider<NoFormView> noFormViewProvider;

  private final Provider<DispatchServiceManager> dispatchServiceManagerProvider;

  @Inject
  public FormPresenterFactory(
      Provider<FormView> formViewProvider,
      Provider<NoFormView> noFormViewProvider,
      Provider<DispatchServiceManager> dispatchServiceManagerProvider) {
    this.formViewProvider = checkNotNull(formViewProvider, 1);
    this.noFormViewProvider = checkNotNull(noFormViewProvider, 2);
    this.dispatchServiceManagerProvider = checkNotNull(dispatchServiceManagerProvider, 3);
  }

  public FormPresenter create(FormFieldPresenterFactory formFieldPresenterFactory) {
    return new FormPresenter(
        checkNotNull(formViewProvider.get(), 1),
        checkNotNull(noFormViewProvider.get(), 2),
        checkNotNull(dispatchServiceManagerProvider.get(), 3),
        checkNotNull(formFieldPresenterFactory, 4));
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
