package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

public interface CardStackPortletView extends IsWidget {


    void setEditModeActive(boolean editModeActive);

    interface EnterEditModeHandler {
        void handleEnterEditMode();
    }

    interface ApplyEditsHandler {
        void handleApplyEdits();
    }

    interface CancelEditsHandler {
        void handleCancelEdits();
    }


    @Nonnull
    AcceptsOneWidget getTabBarContainer();

    void addView(EntityCardUi view);

    void displayApplyOutstandingEditsConfirmation(ApplyEditsHandler applyEditsHandler, CancelEditsHandler cancelEditsHandler);

    void setEnterEditModeHandler(@Nonnull EnterEditModeHandler enterEditModeHandler);

    void setApplyEditsHandler(@Nonnull ApplyEditsHandler handler);

    void setCancelEditsHandler(@Nonnull CancelEditsHandler handler);

    void setEditButtonVisible(boolean visible);

    void setApplyEditsButtonVisible(boolean visible);

    void setCancelEditsButtonVisible(boolean visible);

    void setButtonBarVisible(boolean visible);
}
