package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.tab.TabContentContainer;

public interface EntityCardUi extends AcceptsOneWidget, IsWidget, TabContentContainer, HasAttachHandlers {


    /**
     * Applies or removes the entity-card-content--writable style to this card
     * depending on the value of the writable flag.
     * @param writable The flag that indicates whether this card is writable.
     */
    void setWritable(boolean writable);
}
