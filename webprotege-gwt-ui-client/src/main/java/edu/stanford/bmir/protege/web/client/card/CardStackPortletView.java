package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

public interface CardStackPortletView extends IsWidget {


    void setEditModeActive(boolean editModeActive);

    interface BeginEditingHandler {
        void handleBeginEditing();
    }

    interface FinishEditingHandler {
        void handleFinishEditing();
    }

    interface CancelEditingHandler {
        void handleCancelEditing();
    }


    @Nonnull
    AcceptsOneWidget getTabBarContainer();

    void addView(EntityCardUi view);

    void setBeginEditingHandler(@Nonnull BeginEditingHandler beginEditingHandler);

    void setFinishEditingHandler(@Nonnull FinishEditingHandler handler);

    void setCancelEditingHandler(@Nonnull CancelEditingHandler handler);

    void setEditButtonVisible(boolean visible);

    void setApplyButtonVisible(boolean visible);

    void setCancelButtonVisible(boolean visible);

    void setButtonBarVisible(boolean visible);
}
