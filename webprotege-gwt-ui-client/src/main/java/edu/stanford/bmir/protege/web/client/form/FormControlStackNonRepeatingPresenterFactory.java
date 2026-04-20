package edu.stanford.bmir.protege.web.client.form;

import javax.inject.Inject;

public final class FormControlStackNonRepeatingPresenterFactory {
  @Inject
  public FormControlStackNonRepeatingPresenterFactory() {}

  public FormControlStackNonRepeatingPresenter create(
      FormControl formControl, FormRegionPosition position) {
    return new FormControlStackNonRepeatingPresenter(
        checkNotNull(formControl, 1), checkNotNull(position, 2));
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
