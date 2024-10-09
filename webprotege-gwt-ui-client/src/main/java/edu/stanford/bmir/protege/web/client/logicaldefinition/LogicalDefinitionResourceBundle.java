package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface LogicalDefinitionResourceBundle extends ClientBundle {

    LogicalDefinitionResourceBundle INSTANCE = GWT.create(LogicalDefinitionResourceBundle.class);


    @Source("LogicalDefinition.css")
    LogicalDefinitionResourceBundle.LogicalDefinitionCss style();

    interface LogicalDefinitionCss extends CssResource {

        @ClassName("superClassTable")
        String superClassTable();

        @ClassName("tableText")
        String tableText();

        @ClassName("superClassTableHeader")
        String superClassTableHeader();

        @ClassName("customRowStyle")
        String customRowStyle();


    }


}
