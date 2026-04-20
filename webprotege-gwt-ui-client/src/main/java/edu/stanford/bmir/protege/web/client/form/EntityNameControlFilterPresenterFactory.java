package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.form.field.EntityNameControlDescriptorDto;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import javax.inject.Inject;
import javax.inject.Provider;

public final class EntityNameControlFilterPresenterFactory {
  private final Provider<EntityNameControlFilterView> viewProvider;

  private final Provider<EntityCriteriaPresenter> entityCriteriaPresenterProvider;

  private final Provider<DispatchServiceManager> dispatchProvider;

  private final Provider<ProjectId> projectIdProvider;

  @Inject
  public EntityNameControlFilterPresenterFactory(
      Provider<EntityNameControlFilterView> viewProvider,
      Provider<EntityCriteriaPresenter> entityCriteriaPresenterProvider,
      Provider<DispatchServiceManager> dispatchProvider,
      Provider<ProjectId> projectIdProvider) {
    this.viewProvider = checkNotNull(viewProvider, 1);
    this.entityCriteriaPresenterProvider = checkNotNull(entityCriteriaPresenterProvider, 2);
    this.dispatchProvider = checkNotNull(dispatchProvider, 3);
    this.projectIdProvider = checkNotNull(projectIdProvider, 4);
  }

  public EntityNameControlFilterPresenter create(EntityNameControlDescriptorDto descriptor) {
    return new EntityNameControlFilterPresenter(
        checkNotNull(viewProvider.get(), 1),
        checkNotNull(entityCriteriaPresenterProvider.get(), 2),
        checkNotNull(dispatchProvider.get(), 3),
        checkNotNull(descriptor, 4),
        checkNotNull(projectIdProvider.get(), 5));
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
