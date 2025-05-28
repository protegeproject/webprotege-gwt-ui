package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.*;

public interface PostCoordinationTableResourceBundle extends ClientBundle {


    PostCoordinationTableResourceBundle INSTANCE = GWT.create(PostCoordinationTableResourceBundle.class);

    @Source("PostCoordinationTable.css")
    PostCoordinationTableCss style();


    interface PostCoordinationTableCss extends CssResource {

        @ClassName("postCoordinationHeader")
        String getPostCoordinationHeader();

        @ClassName("customRowStyle")
        String getCustomRowStyle();

        @ClassName("headerLabel")
        String getHeaderLabel();

        @ClassName("postCoordinationTable")
        String getPostCoordinationTable();

        @ClassName("rowLabel")
        String getRowLabel();

        @ClassName("rotatedHeader")
        String getRotatedHeader();

        @ClassName("headerLabelRow")
        String getHeaderLabelRow();

        @ClassName("evenRowStyle")
        String getEvenRowStyle();

        @ClassName("scaleValueHeader")
        String scaleValueHeader();

        @ClassName("scaleValueRow")
        String scaleValueRow();

        @ClassName("scaleValueCard")
        String scaleValueCard();

        @ClassName("scaleValue-table-button-cell")
        String scaleValueTableButtonCell();


        @ClassName("scaleValue-table-value-cell")
        String scaleValueTableValueCell();

        @ClassName("scaleValueHeaderDescription")
        String scaleValueHeaderDescription();

        @ClassName("toggle-icon")
        String toggleIcon();

        @ClassName("disabled")
        String disabled();

        @ClassName("header-icon")
        String headerIcon();

        @ClassName("whiteBackground")
        String whiteBackground();

        @ClassName("size75")
        String size75();

        @ClassName("marginLeftAuto")
        String marginLeftAuto();
    }

}
