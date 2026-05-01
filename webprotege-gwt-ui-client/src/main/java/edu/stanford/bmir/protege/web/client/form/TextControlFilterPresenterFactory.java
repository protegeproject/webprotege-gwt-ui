package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.match.LiteralCriteriaListPresenter;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptorDto;
import javax.inject.Inject;
import javax.inject.Provider;

public final class TextControlFilterPresenterFactory {
  private final Provider<TextControlFilterView> viewProvider;

  private final Provider<LiteralCriteriaListPresenter> criteriaPresenterProvider;

  @Inject
  public TextControlFilterPresenterFactory(
      Provider<TextControlFilterView> viewProvider,
      Provider<LiteralCriteriaListPresenter> criteriaPresenterProvider) {
    this.viewProvider = checkNotNull(viewProvider, 1);
    this.criteriaPresenterProvider = checkNotNull(criteriaPresenterProvider, 2);
  }

  public TextControlFilterPresenter create(TextControlDescriptorDto descriptor) {
    return new TextControlFilterPresenter(
        checkNotNull(viewProvider.get(), 1),
        checkNotNull(descriptor, 2),
        checkNotNull(criteriaPresenterProvider.get(), 3));
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
