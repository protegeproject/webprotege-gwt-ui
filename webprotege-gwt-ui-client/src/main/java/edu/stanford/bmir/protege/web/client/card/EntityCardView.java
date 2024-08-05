package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface EntityCardView extends IsWidget, AcceptsOneWidget {
    void setLabel(String label);
}
