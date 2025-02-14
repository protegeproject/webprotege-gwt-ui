package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckBoxConfig;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;

import java.util.*;

public class LinearizationCheckboxConfig extends CheckBoxConfig {

    private final static String CHECK_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#2cd900\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M4 12.6111L8.92308 17.5L20 6.5\" stroke=\"#2cd900\" stroke-width=\"2.4\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";
    private final static String X_SVG = "<svg viewBox=\"3 2 18 21\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#ff2c2c\" stroke-width=\"0.43200000000000005\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M6.99486 7.00636C6.60433 7.39689 6.60433 8.03005 6.99486 8.42058L10.58 12.0057L6.99486 15.5909C6.60433 15.9814 6.60433 16.6146 6.99486 17.0051C7.38538 17.3956 8.01855 17.3956 8.40907 17.0051L11.9942 13.4199L15.5794 17.0051C15.9699 17.3956 16.6031 17.3956 16.9936 17.0051C17.3841 16.6146 17.3841 15.9814 16.9936 15.5909L13.4084 12.0057L16.9936 8.42059C17.3841 8.03007 17.3841 7.3969 16.9936 7.00638C16.603 6.61585 15.9699 6.61585 15.5794 7.00638L11.9942 10.5915L8.40907 7.00636C8.01855 6.61584 7.38538 6.61584 6.99486 7.00636Z\" fill=\"#ff2c2c\"></path> </g></svg>";

    private final static String UNKNOWN_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#828282\" stroke-width=\"0.24000000000000005\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M9.11241 7.82201C9.44756 6.83666 10.5551 6 12 6C13.7865 6 15 7.24054 15 8.5C15 9.75946 13.7865 11 12 11C11.4477 11 11 11.4477 11 12L11 14C11 14.5523 11.4477 15 12 15C12.5523 15 13 14.5523 13 14L13 12.9082C15.203 12.5001 17 10.7706 17 8.5C17 5.89347 14.6319 4 12 4C9.82097 4 7.86728 5.27185 7.21894 7.17799C7.0411 7.70085 7.3208 8.26889 7.84366 8.44673C8.36653 8.62458 8.93457 8.34488 9.11241 7.82201ZM12 20C12.8285 20 13.5 19.3284 13.5 18.5C13.5 17.6716 12.8285 17 12 17C11.1716 17 10.5 17.6716 10.5 18.5C10.5 19.3284 11.1716 20 12 20Z\" fill=\"#828282\"></path> </g></svg>";

    public static List<CheckboxValue> AVAILABLE_VALUES_LIST = Arrays.asList(new CheckboxValue(UNKNOWN_SVG, "UNKNOWN", "Not set value"),
                                                                             new CheckboxValue(CHECK_SVG, "TRUE", "Allowed"),
                                                                             new CheckboxValue(X_SVG, "FALSE", "Not allowed"));


    protected LinearizationCheckboxConfig() {
        super(new CheckboxValue(UNKNOWN_SVG, "UNKNOWN", "Not set value"), AVAILABLE_VALUES_LIST);
    }


}
