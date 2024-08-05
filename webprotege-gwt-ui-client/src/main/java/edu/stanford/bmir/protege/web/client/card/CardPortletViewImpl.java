package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class CardPortletViewImpl extends Composite implements CardPortletView {

    interface CardPortletViewImplUiBinder extends UiBinder<HTMLPanel, CardPortletViewImpl> {
    }

    private static CardPortletViewImplUiBinder ourUiBinder = GWT.create(CardPortletViewImplUiBinder.class);
    @UiField
    HTMLPanel stack;
    @UiField
    SimplePanel tabBarContainer;

    @Inject
    public CardPortletViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void addView(EntityCardView view) {
        stack.add(view);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTabBarContainer() {
        return tabBarContainer;
    }
}