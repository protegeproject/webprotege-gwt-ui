package edu.stanford.bmir.protege.web.client.search;

import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

public final class EntitySearchResultPresenterFactory {

    @Nonnull
    private final Provider<EntitySearchResultView> viewProvider;

    @Nonnull
    private final Provider<SearchResultMatchPresenter> matchPresenterProvider;

    @Inject
    public EntitySearchResultPresenterFactory(@Nonnull Provider<EntitySearchResultView> viewProvider,
                                              @Nonnull Provider<SearchResultMatchPresenter> matchPresenterProvider) {
        this.viewProvider = checkNotNull(viewProvider);
        this.matchPresenterProvider = checkNotNull(matchPresenterProvider);
    }

    @Nonnull
    public EntitySearchResultPresenter create(@Nonnull EntitySearchResult result) {
        return new EntitySearchResultPresenter(checkNotNull(result), viewProvider.get(), matchPresenterProvider);
    }
}
