package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptorDto;
import javax.inject.Inject;
import javax.inject.Provider;

public final class GridCellPresenterFactory {
  private final Provider<GridCellView> viewProvider;

  @Inject
  public GridCellPresenterFactory(Provider<GridCellView> viewProvider) {
    this.viewProvider = checkNotNull(viewProvider, 1);
  }

  public GridCellPresenter create(
      GridColumnDescriptorDto columnDescriptor, FormControlStackPresenter stackPresenter) {
    return new GridCellPresenter(
        checkNotNull(viewProvider.get(), 1),
        checkNotNull(columnDescriptor, 2),
        checkNotNull(stackPresenter, 3));
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
