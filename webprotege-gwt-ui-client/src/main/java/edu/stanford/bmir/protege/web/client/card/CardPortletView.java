package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

public interface CardPortletView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getTabBarContainer();

    void addView(EntityCardView view);

}
