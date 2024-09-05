package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class CardStackPortletViewImpl extends Composite implements CardStackPortletView {

    private EnterEditModeHandler enterEditModeHandler = () -> {};
    private ApplyEditsHandler applyEditsHandler = () -> {};
    private CancelEditsHandler cancelEditsHandler = () -> {};

    interface CardStackPortletViewImplUiBinder extends UiBinder<HTMLPanel, CardStackPortletViewImpl> {
    }

    private static CardStackPortletViewImplUiBinder ourUiBinder = GWT.create(CardStackPortletViewImplUiBinder.class);
    @UiField
    HTMLPanel stack;
    @UiField
    SimplePanel tabBarContainer;
    @UiField
    Button applyEditsButton;
    @UiField
    Button cancelEditsButton;
    @UiField
    Button editButton;
    @UiField
    HTMLPanel buttonBar;

    @Inject
    public CardStackPortletViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        applyEditsButton.setVisible(true);
        cancelEditsButton.setVisible(true);
        editButton.setVisible(true);
        editButton.addClickHandler(event -> enterEditModeHandler.handleEnterEditMode());
        cancelEditsButton.addClickHandler(event -> cancelEditsHandler.handleCancelEdits());
        applyEditsButton.addClickHandler(event -> applyEditsHandler.handleApplyEdits());
    }

    @Override
    public void addView(EntityCardContainer view) {
        stack.add(view);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTabBarContainer() {
        return tabBarContainer;
    }

    @Override
    public void displayApplyOutstandingEditsConfirmation(ApplyEditsHandler applyEditsHandler, CancelEditsHandler cancelEditsHandler) {

    }

    @Override
    public void setEnterEditModeHandler(@Nonnull EnterEditModeHandler handler) {
        this.enterEditModeHandler = handler;
    }

    @Override
    public void setApplyEditsHandler(@Nonnull ApplyEditsHandler handler) {
        this.applyEditsHandler = handler;
    }

    @Override
    public void setCancelEditsHandler(@Nonnull CancelEditsHandler handler) {
        this.cancelEditsHandler = handler;
    }

    @Override
    public void setEditButtonVisible(boolean visible) {
        this.editButton.setVisible(visible);
    }

    @Override
    public void setApplyEditsButtonVisible(boolean visible) {
        this.applyEditsButton.setVisible(visible);
    }

    @Override
    public void setCancelEditsButtonVisible(boolean visible) {
        this.cancelEditsButton.setVisible(visible);
    }

    @Override
    public void setButtonBarVisible(boolean visible) {
        buttonBar.setVisible(visible);
    }
}