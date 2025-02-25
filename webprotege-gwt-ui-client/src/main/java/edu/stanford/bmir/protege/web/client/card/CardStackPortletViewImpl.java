package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class CardStackPortletViewImpl extends Composite implements CardStackPortletView {

    private BeginEditingHandler beginEditingHandler = () -> {};
    private FinishEditingHandler finishEditingHandler = () -> {};
    private CancelEditingHandler cancelEditingHandler = () -> {};

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
        editButton.addClickHandler(event -> beginEditingHandler.handleBeginEditing());
        cancelEditsButton.addClickHandler(event -> cancelEditingHandler.handleCancelEditing());
        applyEditsButton.addClickHandler(event -> finishEditingHandler.handleFinishEditing());
    }

    @Override
    public void addView(EntityCardUi view) {
        stack.add(view);
    }

    @Override
    public void setEditModeActive(boolean editModeActive) {
        if (editModeActive) {
            addStyleName(WebProtegeClientBundle.BUNDLE.style().entityCardStackEditModeActive());
        }
        else {
            removeStyleName(WebProtegeClientBundle.BUNDLE.style().entityCardStackEditModeActive());
        }
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTabBarContainer() {
        return tabBarContainer;
    }

    @Override
    public void setBeginEditingHandler(@Nonnull BeginEditingHandler handler) {
        this.beginEditingHandler = handler;
    }

    @Override
    public void setFinishEditingHandler(@Nonnull FinishEditingHandler handler) {
        this.finishEditingHandler = handler;
    }

    @Override
    public void setCancelEditingHandler(@Nonnull CancelEditingHandler handler) {
        this.cancelEditingHandler = handler;
    }

    @Override
    public void setEditButtonVisible(boolean visible) {
        this.editButton.setVisible(visible);
    }

    @Override
    public void setApplyButtonVisible(boolean visible) {
        this.applyEditsButton.setVisible(visible);
    }

    @Override
    public void setCancelButtonVisible(boolean visible) {
        this.cancelEditsButton.setVisible(visible);
    }

    @Override
    public void setButtonBarVisible(boolean visible) {
        buttonBar.setVisible(visible);
    }
}