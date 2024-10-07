package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.event.dom.client.ClickHandler;
import edu.stanford.bmir.protege.web.shared.postcoordination.ScaleValueIriAndName;

public interface ScaleValueCardView extends IsWidget {

    void setAddButtonClickHandler(ClickHandler clickHandler);

    void clearTable();

    void addHeader(String headerText, ScaleAllowMultiValue scaleAllowMultiValue);

    void addRow(ScaleValueIriAndName value) ;

    void addSelectValueButton();

    void setDeleteValueButtonHandler(DeleteScaleValueButtonHandler handler);
    void setEditMode(boolean enabled);
}
