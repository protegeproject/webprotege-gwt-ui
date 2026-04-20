package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptorDto;
import javax.inject.Inject;
import javax.inject.Provider;

public final class SingleChoiceControlFilterPresenterFactory {
  private final Provider<SingleChoiceControlFilterView> viewProvider;

  private final Provider<FormControlFactory> formControlFactoryProvider;

  @Inject
  public SingleChoiceControlFilterPresenterFactory(
      Provider<SingleChoiceControlFilterView> viewProvider,
      Provider<FormControlFactory> formControlFactoryProvider) {
    this.viewProvider = checkNotNull(viewProvider, 1);
    this.formControlFactoryProvider = checkNotNull(formControlFactoryProvider, 2);
  }

  public SingleChoiceControlFilterPresenter create(SingleChoiceControlDescriptorDto descriptor) {
    return new SingleChoiceControlFilterPresenter(
        checkNotNull(viewProvider.get(), 1),
        checkNotNull(descriptor, 2),
        checkNotNull(formControlFactoryProvider.get(), 3));
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
