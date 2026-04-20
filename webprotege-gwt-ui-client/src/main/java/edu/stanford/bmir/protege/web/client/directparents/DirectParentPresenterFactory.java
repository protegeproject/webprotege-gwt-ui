package edu.stanford.bmir.protege.web.client.directparents;

import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import javax.inject.Inject;
import javax.inject.Provider;

public final class DirectParentPresenterFactory {
  private final Provider<DirectParentView> viewProvider;

  private final Provider<EntityNodeHtmlRenderer> nodeRendererProvider;

  @Inject
  public DirectParentPresenterFactory(
      Provider<DirectParentView> viewProvider,
      Provider<EntityNodeHtmlRenderer> nodeRendererProvider) {
    this.viewProvider = checkNotNull(viewProvider, 1);
    this.nodeRendererProvider = checkNotNull(nodeRendererProvider, 2);
  }

  public DirectParentPresenter create(
      EntityNode entityNode, ParentSelectionHandler selectionHandler) {
    return new DirectParentPresenter(
        checkNotNull(entityNode, 1),
        checkNotNull(selectionHandler, 2),
        checkNotNull(viewProvider.get(), 3),
        checkNotNull(nodeRendererProvider.get(), 4));
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
