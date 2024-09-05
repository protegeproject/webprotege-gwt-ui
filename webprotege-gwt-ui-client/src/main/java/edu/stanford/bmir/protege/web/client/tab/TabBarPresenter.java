package edu.stanford.bmir.protege.web.client.tab;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 * <p>
 * The TabBarPresenter class is responsible for controlling the behavior of a tab bar view.
 * It manages a list of tab presenters and interacts with the tab bar view to display and select tabs.
 *
 * @param <K> the type of the tab key
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class TabBarPresenter<K> {

    @Nonnull
    private final List<TabPresenter<K>> itemPresenters = new ArrayList<>();

    @Nonnull
    private final TabBarView view;

    private final TabPresenterFactory tabPresenterFactory;

    private SelectedTabChangedHandler selectedTabChangedHandler = () -> {};

    @Nonnull
    private Optional<SelectedTabIdStash<K>> selectedKeyStash = Optional.empty();

    @Inject
    public TabBarPresenter(@Nonnull TabBarView view, TabPresenterFactory tabPresenterFactory) {
        this.view = checkNotNull(view);
        this.tabPresenterFactory = checkNotNull(tabPresenterFactory);
    }

    /**
     * Clears the item presenters and view of the tab bar.
     * Additionally, sets the visibility of the view to false.
     */
    public void clear() {
        itemPresenters.forEach(p -> p.setSelected(false));
        itemPresenters.clear();
        view.clear();
        view.setVisible(false);
    }

    /**
     * Sets the handler for the event when the selected tab in the tab bar changes.
     *
     * @param selectedTabChangedHandler The SelectedTabChangedHandler to be set as the handler.
     *                                  Must not be null.
     */
    public void setSelectedTabChangedHandler(@Nonnull SelectedTabChangedHandler selectedTabChangedHandler) {
        this.selectedTabChangedHandler = checkNotNull(selectedTabChangedHandler);
    }

    /**
     * Sets the selected key stash for the tab bar. The selected key stash is used to store and retrieve
     * the selected tab key. It also restores the tab selection if a selected key stash is present.
     *
     * @param selectedTabIdStash The SelectedTabIdStash object representing the selected key stash.
     */
    public void setSelectedKeyStash(@Nonnull SelectedTabIdStash<K> selectedTabIdStash) {
        this.selectedKeyStash = Optional.of(selectedTabIdStash);
        selectedTabIdStash.getSelectedKey().ifPresent(this::restoreTabSelection);
    }

    private void restoreTabSelection(K key) {
        setSelected(key);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        restoreSelection();
    }

    /**
     * Adds a new tab to the tab bar with the given key, label, and content container.
     *
     * @param key                     The key associated with the tab.
     * @param label                   The label of the tab.
     * @param tabContentContainer     The container that holds the content of the tab.
     */
    public void addTab(@Nonnull K key, @Nonnull LanguageMap label, @Nonnull TabContentContainer tabContentContainer) {
        addTab(key, label, null, null, tabContentContainer);
    }

    public void addTab(@Nonnull K key, @Nonnull LanguageMap label, Color color, Color backgroundColor, @Nonnull TabContentContainer tabContentContainer) {
        TabPresenter<K> tabPresenter = tabPresenterFactory.create(key);
        itemPresenters.add(tabPresenter);
        view.addView(tabPresenter.getView());
        tabPresenter.setLabel(label);
        if (color != null) {
            tabPresenter.setColor(color);
        }
        if (backgroundColor != null) {
            tabPresenter.setBackgroundColor(backgroundColor);
        }
        tabPresenter.setTabContentContainer(tabContentContainer);
        tabPresenter.setClickHandler(event -> selectTabAndStashId(key));
        view.setVisible(itemPresenters.size() > 1);
    }


    public void setColor(K key, Color color) {
        itemPresenters.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .ifPresent(tabPresenter -> tabPresenter.setColor(color));
    }

    public void setBackgroundColor(K key, Color backgroundColor) {
        itemPresenters.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .ifPresent(tabPresenter -> tabPresenter.setBackgroundColor(backgroundColor));
    }

    private void selectTabAndStashId(@Nonnull K key) {
        setSelected(key);
        selectedKeyStash.ifPresent(stash -> stash.stashSelectedTabKey(key));
        selectedTabChangedHandler.handleSelectedTabChanged();
    }

    private void setSelected(K key) {
        for (TabPresenter<K> ip : itemPresenters) {
            boolean selected = ip.getKey().equals(key);
            ip.setSelected(selected);
        }
    }

    /**
     * Restores the selection of the tab bar. If a selected key stash is present, the method will try to restore the
     * selected tab using the stored key. If no selected tab is found, the method will set the first tab as selected.
     * If no selected key stash is present, the method will also set the first tab as selected.
     */
    public void restoreSelection() {
        this.selectedKeyStash.ifPresent(stash -> {
            Optional<K> selectedTab = stash.getSelectedKey();
            selectedTab.ifPresent(this::restoreTabSelection);
            if (!selectedTab.isPresent()) {
                setFirstTabSelected();
            }
        });
        if (!selectedKeyStash.isPresent()) {
            setFirstTabSelected();
        }
    }

    /**
     * Sets the first tab of the tab bar as selected. If the list of tabs is empty, no tab will be selected.
     */
    public void setFirstTabSelected() {
        itemPresenters.stream().findFirst().map(TabPresenter::getKey).ifPresent(this::setSelected);
    }

    /**
     * Returns the key associated with the selected tab in the tab bar.
     *
     * @return An Optional containing the key of the selected tab, or an empty Optional if no tab is selected.
     */
    public Optional<K> getSelectedTab() {
        return itemPresenters.stream()
                             .filter(TabPresenter::isSelected)
                             .map(TabPresenter::getKey)
                             .findFirst();
    }
}
