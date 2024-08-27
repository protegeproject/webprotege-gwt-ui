package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.event.dom.client.ClickHandler;

public interface ScaleValueCardView extends IsWidget {

    void setAddButtonClickHandler(ClickHandler clickHandler);

    void clearTable();

    void addHeader(String headerText);

    void addRow(String value) ;

    void addSelectValueButton();

    void setDeleteValueButtonHandler(DeleteScaleValueButtonHandler handler);
}
