package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import javax.inject.Inject;
import javax.inject.Provider;

public final class FormControlStackRepeatingPresenterFactory {
  private final Provider<FormControlStackRepeatingView> viewProvider;

  private final Provider<PaginatorPresenter> paginatorPresenterProvider;

  @Inject
  public FormControlStackRepeatingPresenterFactory(
      Provider<FormControlStackRepeatingView> viewProvider,
      Provider<PaginatorPresenter> paginatorPresenterProvider) {
    this.viewProvider = checkNotNull(viewProvider, 1);
    this.paginatorPresenterProvider = checkNotNull(paginatorPresenterProvider, 2);
  }

  public FormControlStackRepeatingPresenter create(
      FormRegionPosition position, FormControlDataEditorFactory formControlFactory) {
    return new FormControlStackRepeatingPresenter(
        checkNotNull(viewProvider.get(), 1),
        checkNotNull(paginatorPresenterProvider.get(), 2),
        checkNotNull(position, 3),
        checkNotNull(formControlFactory, 4));
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
