package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;

import javax.annotation.Nonnull;

public interface LinearizationCommentsView extends IsWidget, HasInitialFocusable {

    void clear();

    void setBody(@Nonnull String body);

    @Nonnull
    String getBody();
}
