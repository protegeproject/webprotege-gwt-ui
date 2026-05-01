package edu.stanford.bmir.protege.web.client.hierarchy;

import javax.inject.Inject;
import javax.inject.Provider;

public final class HierarchyPopupPresenterFactory {
  private final Provider<HierarchyPopupView> viewProvider;

  private final Provider<EntityHierarchyModel> modelProvider;

  @Inject
  public HierarchyPopupPresenterFactory(
      Provider<HierarchyPopupView> viewProvider, Provider<EntityHierarchyModel> modelProvider) {
    this.viewProvider = checkNotNull(viewProvider, 1);
    this.modelProvider = checkNotNull(modelProvider, 2);
  }

  public HierarchyPopupPresenter create(HierarchyDescriptor hierarchyDescriptor) {
    return new HierarchyPopupPresenter(
        checkNotNull(hierarchyDescriptor, 1),
        checkNotNull(viewProvider.get(), 2),
        checkNotNull(modelProvider.get(), 3));
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
