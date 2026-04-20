package edu.stanford.bmir.protege.web.client.entity;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.form.FormPresenter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

public final class DeprecateEntityPresenterFactory {

    @Nonnull
    private final Provider<ProjectId> projectIdProvider;

    @Nonnull
    private final Provider<DispatchServiceManager> dispatchProvider;

    @Nonnull
    private final Provider<DeprecateEntityView> viewProvider;

    @Nonnull
    private final Provider<FormPresenter> formPresenterProvider;

    @Inject
    public DeprecateEntityPresenterFactory(@Nonnull Provider<ProjectId> projectIdProvider,
                                           @Nonnull Provider<DispatchServiceManager> dispatchProvider,
                                           @Nonnull Provider<DeprecateEntityView> viewProvider,
                                           @Nonnull Provider<FormPresenter> formPresenterProvider) {
        this.projectIdProvider = checkNotNull(projectIdProvider);
        this.dispatchProvider = checkNotNull(dispatchProvider);
        this.viewProvider = checkNotNull(viewProvider);
        this.formPresenterProvider = checkNotNull(formPresenterProvider);
    }

    @Nonnull
    public DeprecateEntityPresenter create(@Nonnull OWLEntity entity) {
        return new DeprecateEntityPresenter(
                checkNotNull(entity),
                checkNotNull(projectIdProvider.get()),
                checkNotNull(dispatchProvider.get()),
                checkNotNull(viewProvider.get()),
                checkNotNull(formPresenterProvider.get()));
    }
}
