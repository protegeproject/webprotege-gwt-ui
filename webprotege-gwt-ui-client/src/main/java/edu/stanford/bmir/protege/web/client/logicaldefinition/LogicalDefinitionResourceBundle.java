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

        @ClassName("definitionsEmptyState")
        String definitionsEmptyState();

        @ClassName("tableText")
        String tableText();

        @ClassName("superClassTableHeader")
        String superClassTableHeader();

        @ClassName("customRowStyle")
        String customRowStyle();

        @ClassName("dropDownMandatory")
        String dropDownMandatory();
        @ClassName("dropDownAllowed")
        String dropDownAllowed();
        @ClassName("dropDownNotSet")
        String dropDownNotSet();

        @ClassName("logicalDefinitionDropdown")
        String logicalDefinitionDropdown();

        @ClassName("superClassName")
        String superClassName();

        @ClassName("removeButtonCell")
        String removeButtonCell();

        @ClassName("selectValueModalWrapper")
        String selectValueModalWrapper();

        @ClassName("mainModalView")
        String mainModalView();
        @ClassName("addAxisValueButton")
        String addAxisValueButton();

        @ClassName("axisColumn")
        String axisColumn();

        @ClassName("valuesColumn")
        String valuesColumn();
        @ClassName("addAxisValueCell")
        String addAxisValueCell();

        @ClassName("axisDropdownPlaceholder")
        String axisDropdownPlaceholder();

        default String getxSvg(){
            return X_SVG;
        }

        default String getPlusSvg(){
            return PLUS_SVG;
        }

        default String getDeleteSvg() {
            return DELETE_SVG;
        }

        default String getPlusGreySvg(){
            return PLUS_GREY_SVG;
        }
        String X_SVG = "<svg viewBox=\"3 2 18 21\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#ff2c2c\" stroke-width=\"0.43200000000000005\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M6.99486 7.00636C6.60433 7.39689 6.60433 8.03005 6.99486 8.42058L10.58 12.0057L6.99486 15.5909C6.60433 15.9814 6.60433 16.6146 6.99486 17.0051C7.38538 17.3956 8.01855 17.3956 8.40907 17.0051L11.9942 13.4199L15.5794 17.0051C15.9699 17.3956 16.6031 17.3956 16.9936 17.0051C17.3841 16.6146 17.3841 15.9814 16.9936 15.5909L13.4084 12.0057L16.9936 8.42059C17.3841 8.03007 17.3841 7.3969 16.9936 7.00638C16.603 6.61585 15.9699 6.61585 15.5794 7.00638L11.9942 10.5915L8.40907 7.00636C8.01855 6.61584 7.38538 6.61584 6.99486 7.00636Z\" fill=\"#ff2c2c\"></path> </g></svg>";

        String PLUS_SVG ="<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M6 12H18M12 6V18\" stroke=\"#1ec400\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";

        String DELETE_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M5 6.77273H9.2M19 6.77273H14.8M9.2 6.77273V5.5C9.2 4.94772 9.64772 4.5 10.2 4.5H13.8C14.3523 4.5 14.8 4.94772 14.8 5.5V6.77273M9.2 6.77273H14.8M6.4 8.59091V15.8636C6.4 17.5778 6.4 18.4349 6.94673 18.9675C7.49347 19.5 8.37342 19.5 10.1333 19.5H13.8667C15.6266 19.5 16.5065 19.5 17.0533 18.9675C17.6 18.4349 17.6 17.5778 17.6 15.8636V8.59091M9.2 10.4091V15.8636M12 10.4091V15.8636M14.8 10.4091V15.8636\" stroke=\"#464455\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";

        String PLUS_GREY_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#9f9f9f\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M6 12H18M12 6V18\" stroke=\"#b7b7b7\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";

    }


}
