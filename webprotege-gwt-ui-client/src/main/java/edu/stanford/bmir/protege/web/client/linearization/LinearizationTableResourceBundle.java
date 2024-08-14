package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface LinearizationTableResourceBundle extends ClientBundle {


    LinearizationTableResourceBundle INSTANCE = GWT.create(LinearizationTableResourceBundle.class);

    @Source("LinearizationTable.css")
    LinearizationCss style();



    interface LinearizationCss extends CssResource {

        @ClassName("customRowStyle")
        String customRowStyle();

        @ClassName("linearizationTable")
        String getLinearizationTable();

        @ClassName("linearizationHeader")
        String getLinearizationHeader();

        @ClassName("tableText")
        String getTableText();

        @ClassName("tableCheckBox")
        String getTableCheckBox();

        @ClassName("tableCheckboxHeader")
        String getTableCheckboxHeader();

        @ClassName("wideColumn")
        String getWideColumn();

        @ClassName("enabledCheckbox")
        String getEnabledCheckbox();

        @ClassName("secondaryParent")
        String getSecondaryParent();

        @ClassName("linearizationDefinition")
        String getLinearizationDefinition();

        @ClassName("linearizationParentCell")
        String getLinearizationParentCell();
    }

}
