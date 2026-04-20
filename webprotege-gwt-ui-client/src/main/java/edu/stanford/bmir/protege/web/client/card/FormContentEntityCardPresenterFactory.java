package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormPresenter;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import javax.inject.Inject;
import javax.inject.Provider;

public final class FormContentEntityCardPresenterFactory {
  private final Provider<DispatchServiceManager> dispatchProvider;

  private final Provider<ProjectId> projectIdProvider;

  private final Provider<FormPresenter> formPresenterProvider;

  private final Provider<SelectionModel> selectionModelProvider;

  @Inject
  public FormContentEntityCardPresenterFactory(
      Provider<DispatchServiceManager> dispatchProvider,
      Provider<ProjectId> projectIdProvider,
      Provider<FormPresenter> formPresenterProvider,
      Provider<SelectionModel> selectionModelProvider) {
    this.dispatchProvider = checkNotNull(dispatchProvider, 1);
    this.projectIdProvider = checkNotNull(projectIdProvider, 2);
    this.formPresenterProvider = checkNotNull(formPresenterProvider, 3);
    this.selectionModelProvider = checkNotNull(selectionModelProvider, 4);
  }

  public FormContentEntityCardPresenter create(FormId formId) {
    return new FormContentEntityCardPresenter(
        checkNotNull(formId, 1),
        checkNotNull(dispatchProvider.get(), 2),
        checkNotNull(projectIdProvider.get(), 3),
        checkNotNull(formPresenterProvider.get(), 4),
        checkNotNull(selectionModelProvider.get(), 5));
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
