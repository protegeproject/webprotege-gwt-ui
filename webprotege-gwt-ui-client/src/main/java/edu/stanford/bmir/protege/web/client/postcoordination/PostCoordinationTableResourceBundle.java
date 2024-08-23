package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

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
    }

}
