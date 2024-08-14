package edu.stanford.bmir.protege.web.client.tab;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

public class TabPresenterFactory {

    private final Provider<TabView> viewProvider;

    @Inject
    public TabPresenterFactory(Provider<TabView> viewProvider) {
        this.viewProvider = checkNotNull(viewProvider);
    }

    public <K> TabPresenter<K> create(K key) {
        return new TabPresenter<>(checkNotNull(key),
                checkNotNull(viewProvider.get()));
    }
}
