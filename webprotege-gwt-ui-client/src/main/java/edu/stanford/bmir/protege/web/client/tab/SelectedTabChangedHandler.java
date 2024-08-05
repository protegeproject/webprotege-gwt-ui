package edu.stanford.bmir.protege.web.client.tab;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-13
 */
public interface SelectedTabChangedHandler {

    /**
     * Handles the event when the selected tab in the tab bar changes.
     * This method is called to notify any registered listeners that the selected tab has changed.
     */
    void handleSelectedTabChanged();
}
