package edu.stanford.bmir.protege.web.client.list;

import edu.stanford.bmir.protege.web.client.individualslist.EntityNodeListCellRenderer;
import javax.inject.Inject;
import javax.inject.Provider;

public final class EntityNodeListPopupPresenterFactory {
  private final Provider<EntityNodeListCellRenderer> rendererProvider;

  private final Provider<EntityNodeListPopupView> viewProvider;

  @Inject
  public EntityNodeListPopupPresenterFactory(
      Provider<EntityNodeListCellRenderer> rendererProvider,
      Provider<EntityNodeListPopupView> viewProvider) {
    this.rendererProvider = checkNotNull(rendererProvider, 1);
    this.viewProvider = checkNotNull(viewProvider, 2);
  }

  public EntityNodeListPopupPresenter create(
      EntityNodeListPopupPresenter.ListDataSupplier listDataSupplier) {
    return new EntityNodeListPopupPresenter(
        checkNotNull(rendererProvider.get(), 1),
        checkNotNull(viewProvider.get(), 2),
        checkNotNull(listDataSupplier, 3));
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
