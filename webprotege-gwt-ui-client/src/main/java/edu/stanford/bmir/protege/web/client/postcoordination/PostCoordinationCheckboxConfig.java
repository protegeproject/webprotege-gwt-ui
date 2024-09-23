package edu.stanford.bmir.protege.web.client.postcoordination;

import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckBoxConfig;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class PostCoordinationCheckboxConfig extends CheckBoxConfig {
    private final Logger logger = Logger.getLogger("CheckBoxConfig");

    private final static String CHECK_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#2cd900\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M4 12.6111L8.92308 17.5L20 6.5\" stroke=\"#2cd900\" stroke-width=\"2.4\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";
    private final static String REQUIRED_SVG = "<svg viewBox=\"0 0 512 512\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" fill=\"#000000\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <title>mandatory-done</title> <g id=\"Page-1\" stroke=\"none\" stroke-width=\"1\" fill=\"none\" fill-rule=\"evenodd\"> <g id=\"icon\" fill=\"#c23f42\" transform=\"translate(114.773333, 42.666667)\"> <path d=\"M322.588167,146.773333 L367.843001,192.028167 L130.588167,429.283001 L-2.61257984e-15,298.694834 L45.254834,253.44 L130.588167,338.780167 L322.588167,146.773333 Z M169.347879,7.10542736e-15 L159.927965,83.3939394 L236.94961,49.3160173 L253.850043,103.619048 L172.672554,119.411255 L226.975584,180.08658 L180.707186,213.333333 L141.088139,141.298701 L101.746147,213.333333 L55.2006926,180.08658 L109.503723,119.411255 L28.60329,103.619048 L45.2266667,49.3160173 L122.248312,83.3939394 L112.828398,7.10542736e-15 L169.347879,7.10542736e-15 Z\" id=\"Combined-Shape\"> </path> </g> </g> </g></svg>";

    private final static String NOT_ALLOWED = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" viewBox=\"0 0 290.658 290.658\" xml:space=\"preserve\" fill=\"#000000\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <g> <g> <rect y=\"139.474\" style=\"fill:darkgrey;\" width=\"290.658\" height=\"11.711\"></rect> </g> </g> </g></svg>";

    private final static String DEFAULT_CHECK_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M4 12.6111L8.92308 17.5L20 6.5\" stroke=\"#919191\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";
    private final static String DEFAULT_REQUIRED_SVG = "<svg viewBox=\"0 0 512 512\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" fill=\"#000000\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <title>mandatory-done</title> <g id=\"Page-1\" stroke=\"none\" stroke-width=\"1\" fill=\"none\" fill-rule=\"evenodd\"> <g id=\"icon\" fill=\"#919191\" transform=\"translate(114.773333, 42.666667)\"> <path d=\"M322.588167,146.773333 L367.843001,192.028167 L130.588167,429.283001 L-2.61257984e-15,298.694834 L45.254834,253.44 L130.588167,338.780167 L322.588167,146.773333 Z M169.347879,7.10542736e-15 L159.927965,83.3939394 L236.94961,49.3160173 L253.850043,103.619048 L172.672554,119.411255 L226.975584,180.08658 L180.707186,213.333333 L141.088139,141.298701 L101.746147,213.333333 L55.2006926,180.08658 L109.503723,119.411255 L28.60329,103.619048 L45.2266667,49.3160173 L122.248312,83.3939394 L112.828398,7.10542736e-15 L169.347879,7.10542736e-15 Z\" id=\"Combined-Shape\"> </path> </g> </g> </g></svg>";

    private final static String DEFAULT_NOT_ALLOWED = "<svg version=\"1.1\" id=\"Capa_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" viewBox=\"0 0 290.658 290.658\" xml:space=\"preserve\" fill=\"#000000\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <g> <g> <rect y=\"139.474\" style=\"fill:darkgrey;\" width=\"290.658\" height=\"11.711\"></rect> </g> </g> </g></svg>";

    private CheckboxValue parentValue;


    public static List<CheckboxValue> AVAILABLE_VALUES_LIST = Arrays.asList(new CheckboxValue(NOT_ALLOWED, "UNKNOWN"),
            new CheckboxValue(CHECK_SVG, "ALLOWED"),
            new CheckboxValue(REQUIRED_SVG, "REQUIRED")
            );

    public static List<CheckboxValue> AVAILABLE_DEFAULT_VALUE_LIST = Arrays.asList(
            new CheckboxValue(DEFAULT_NOT_ALLOWED, "DEFAULT_UNKNOWN"),
            new CheckboxValue(DEFAULT_CHECK_SVG, "DEFAULT_ALLOWED"),
            new CheckboxValue(DEFAULT_REQUIRED_SVG, "DEFAULT_REQUIRED")
    );

    protected PostCoordinationCheckboxConfig() {
        super(new CheckboxValue(NOT_ALLOWED, "UNKNOWN"), AVAILABLE_VALUES_LIST);
    }

    @Override
    public CheckboxValue getNextValue(CheckboxValue checkboxValue) {
        if(parentValue != null) {
            return getNextValueForDerivedClasses(checkboxValue);
        } else {
            return super.getNextValue(checkboxValue);
        }
    }

    @Override
    public CheckboxValue findValue(String inputValue) {
        if(inputValue == null || inputValue.isEmpty()) {
            return new CheckboxValue(NOT_ALLOWED, "UNKNOWN");
        }
        List<CheckboxValue> allValues = new ArrayList<>();
        allValues.addAll(AVAILABLE_VALUES_LIST);
        allValues.addAll(AVAILABLE_DEFAULT_VALUE_LIST);

        return allValues.stream().filter(value -> value.getValue().equalsIgnoreCase(inputValue))
                .findFirst()
                .orElse(new CheckboxValue(NOT_ALLOWED, "UNKNOWN"));
    }

    private CheckboxValue getNextValueForDerivedClasses(CheckboxValue checkboxValue) {
        if(checkboxValue.getValue().startsWith("DEFAULT")) {
            return AVAILABLE_VALUES_LIST.get(0);
        }

        int pos = AVAILABLE_VALUES_LIST.indexOf(checkboxValue);
        if(pos >= 0) {
            if (pos == AVAILABLE_VALUES_LIST.size()-1) {
                return AVAILABLE_DEFAULT_VALUE_LIST.stream().filter(value -> ("DEFAULT_"+parentValue.getValue()).equalsIgnoreCase(value.getValue()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Missing parent"));
            } else {
                return AVAILABLE_VALUES_LIST.get(pos + 1);

            }
        }else {
            throw new RuntimeException("Given value " + checkboxValue + " is not in scrollable values ");
        }
    }

    public void setParentValue(CheckboxValue parentValue) {
        this.parentValue = parentValue;
    }

}
