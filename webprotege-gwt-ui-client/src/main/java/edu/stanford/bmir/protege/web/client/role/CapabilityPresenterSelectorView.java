package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

public interface CapabilityPresenterSelectorView extends IsWidget {

    void clearTypeIds();

    void setSelectedTypeId(String selectedTypeId);

    void clearPresenter();

    void setCapabilityEditable(boolean editable);

    interface SelectedTypeIdChangedHandler {
        void handleSelectedTypeIdChanged(String selectedTypeId);
    }

    void addTypeId(String typeId, String label);

    @Nonnull
    AcceptsOneWidget getPresenterContainer();

    String getSelectedTypeId();

    void setSelectedTypeIdChangedHandler(SelectedTypeIdChangedHandler handler);
}
