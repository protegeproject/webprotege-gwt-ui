package edu.stanford.bmir.protege.web.client.tab;


import com.google.gwt.event.dom.client.ClickHandler;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The TabPresenter class represents a presenter for an individual tab in a tab bar view.  Note that the tab
 * is the literal clickable tab and is not the content of the tab.  Each tab has an underlying key that identifies it.
 *
 * @param <K> the type of the tab key
 */
public class TabPresenter<K> {

    @Nonnull
    private final K key;

    @Nonnull
    private final TabView view;

    private boolean selected = false;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Nonnull
    private Optional<TabContentContainer> formContainer = Optional.empty();

    @Inject
    public TabPresenter(@Nonnull K key,
                        @Nonnull TabView view) {
        this.key = checkNotNull(key);
        this.view = checkNotNull(view);
    }

    /**
     * Returns the key associated with the TabPresenter. The key is used to identify the tab.
     *
     * @return The key associated with the TabPresenter.
     */
    @Nonnull
    public K getKey() {
        return key;
    }

    /**
     * Sets the tab content container for the tab.  When the tab is selected the content will be made
     * visible.  When the tab is deselected the content will be made invisible.
     *
     * @param tabContentContainer The tab content container to set.
     */
    public void setTabContentContainer(@Nonnull TabContentContainer tabContentContainer) {
        this.formContainer = Optional.of(checkNotNull(tabContentContainer));
    }

    /**
     * Sets the label for the tab.
     *
     * @param label The LanguageMap object representing the label of the tab.
     */
    public void setLabel(LanguageMap label) {
        view.setLabel(label);
    }

    /**
     * Sets the color of the tab.
     *
     * @param color The color to set for the tab.
     *
     */
    public void setColor(Color color) {
        view.setColor(color);
    }

    /**
     * Sets the background color for the view.
     *
     * @param backgroundColor The color to set as the background color.
     */
    public void setBackgroundColor(Color backgroundColor) {
        view.setBackgroundColor(backgroundColor);
    }

    /**
     * Sets the selected state of the tab.  If the selected state is set to true then
     * the {@link TabContentContainer} that is associated with this tab will have its visibility
     * set to true.  If the selected stat is set to false then the {@link TabContentContainer} that
     * is associated with this tab will have its visibility set to false.
     *
     * @param selected true if the tab should be selected, false otherwise
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        view.setSelected(selected);
        formContainer.ifPresent(c -> c.setVisible(selected));
    }

    /**
     * Checks if the tab is selected.
     *
     * @return true if the tab is selected, false otherwise
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the click handler for the tab.  The handler will be called when the visible tab is clicked.
     *
     * @param clickHandler The ClickHandler object to set as the click handler.
     */
    public void setClickHandler(@Nonnull ClickHandler clickHandler) {
        view.setClickHandler(clickHandler);
    }

    /**
     * Retrieves the view associated with this TabPresenter.
     *
     * @return The TabView object representing the view associated with this TabPresenter.
     */
    @Nonnull
    public TabView getView() {
        return view;
    }
}
