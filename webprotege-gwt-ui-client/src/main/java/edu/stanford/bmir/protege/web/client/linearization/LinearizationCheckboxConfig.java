package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckBoxConfig;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;

import java.util.*;

public class LinearizationCheckboxConfig extends CheckBoxConfig {

    private final static String CHECK_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#05dd00\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M4 12.6111L8.92308 17.5L20 6.5\" stroke=\"#2cd900\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";
    private final static String X_SVG = "<svg fill=\"#ff2424\" viewBox=\"0 0 32 32\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#ff2424\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M18.8,16l5.5-5.5c0.8-0.8,0.8-2,0-2.8l0,0C24,7.3,23.5,7,23,7c-0.5,0-1,0.2-1.4,0.6L16,13.2l-5.5-5.5 c-0.8-0.8-2.1-0.8-2.8,0C7.3,8,7,8.5,7,9.1s0.2,1,0.6,1.4l5.5,5.5l-5.5,5.5C7.3,21.9,7,22.4,7,23c0,0.5,0.2,1,0.6,1.4 C8,24.8,8.5,25,9,25c0.5,0,1-0.2,1.4-0.6l5.5-5.5l5.5,5.5c0.8,0.8,2.1,0.8,2.8,0c0.8-0.8,0.8-2.1,0-2.8L18.8,16z\"></path> </g></svg>";

    private final static String UNKNOWN_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M12 19H12.01M8.21704 7.69689C8.75753 6.12753 10.2471 5 12 5C14.2091 5 16 6.79086 16 9C16 10.6565 14.9931 12.0778 13.558 12.6852C12.8172 12.9988 12.4468 13.1556 12.3172 13.2767C12.1629 13.4209 12.1336 13.4651 12.061 13.6634C12 13.8299 12 14.0866 12 14.6L12 16\" stroke=\"#828282\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";

    public static List<CheckboxValue> AVAILABLE_VALUES_LIST = Arrays.asList(new CheckboxValue(UNKNOWN_SVG, "UNKNOWN"),
                                                                             new CheckboxValue(X_SVG, "FALSE"),
                                                                             new CheckboxValue(CHECK_SVG, "TRUE"));


    protected LinearizationCheckboxConfig() {
        super(new CheckboxValue(UNKNOWN_SVG, "UNKNOWN"), AVAILABLE_VALUES_LIST);
    }


}
