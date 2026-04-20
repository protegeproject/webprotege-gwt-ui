package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.match.NumericValueCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlDescriptorDto;
import javax.inject.Inject;
import javax.inject.Provider;

public final class NumberControlFilterPresenterFactory {
  private final Provider<NumberControlFilterView> viewProvider;

  private final Provider<NumericValueCriteriaPresenter> numericValueCriteriaPresenterProvider;

  @Inject
  public NumberControlFilterPresenterFactory(
      Provider<NumberControlFilterView> viewProvider,
      Provider<NumericValueCriteriaPresenter> numericValueCriteriaPresenterProvider) {
    this.viewProvider = checkNotNull(viewProvider, 1);
    this.numericValueCriteriaPresenterProvider =
        checkNotNull(numericValueCriteriaPresenterProvider, 2);
  }

  public NumberControlFilterPresenter create(NumberControlDescriptorDto descriptor) {
    return new NumberControlFilterPresenter(
        checkNotNull(viewProvider.get(), 1),
        checkNotNull(descriptor, 2),
        checkNotNull(numericValueCriteriaPresenterProvider.get(), 3));
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
