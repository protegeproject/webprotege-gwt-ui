package edu.stanford.bmir.protege.web.client.postcoordination;

import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckBoxConfig;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;

import java.util.Arrays;
import java.util.List;

public class PostCoordinationCheckboxConfig extends CheckBoxConfig {

    private final static String CHECK_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#2cd900\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M4 12.6111L8.92308 17.5L20 6.5\" stroke=\"#2cd900\" stroke-width=\"2.4\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";
    private final static String REQUIRED_SVG = "<svg viewBox=\"0 0 512 512\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" fill=\"#000000\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <title>mandatory-done</title> <g id=\"Page-1\" stroke=\"none\" stroke-width=\"1\" fill=\"none\" fill-rule=\"evenodd\"> <g id=\"icon\" fill=\"#c23f42\" transform=\"translate(114.773333, 42.666667)\"> <path d=\"M322.588167,146.773333 L367.843001,192.028167 L130.588167,429.283001 L-2.61257984e-15,298.694834 L45.254834,253.44 L130.588167,338.780167 L322.588167,146.773333 Z M169.347879,7.10542736e-15 L159.927965,83.3939394 L236.94961,49.3160173 L253.850043,103.619048 L172.672554,119.411255 L226.975584,180.08658 L180.707186,213.333333 L141.088139,141.298701 L101.746147,213.333333 L55.2006926,180.08658 L109.503723,119.411255 L28.60329,103.619048 L45.2266667,49.3160173 L122.248312,83.3939394 L112.828398,7.10542736e-15 L169.347879,7.10542736e-15 Z\" id=\"Combined-Shape\"> </path> </g> </g> </g></svg>";

    private final static String UNKNOWN_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#828282\" stroke-width=\"0.24000000000000005\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M9.11241 7.82201C9.44756 6.83666 10.5551 6 12 6C13.7865 6 15 7.24054 15 8.5C15 9.75946 13.7865 11 12 11C11.4477 11 11 11.4477 11 12L11 14C11 14.5523 11.4477 15 12 15C12.5523 15 13 14.5523 13 14L13 12.9082C15.203 12.5001 17 10.7706 17 8.5C17 5.89347 14.6319 4 12 4C9.82097 4 7.86728 5.27185 7.21894 7.17799C7.0411 7.70085 7.3208 8.26889 7.84366 8.44673C8.36653 8.62458 8.93457 8.34488 9.11241 7.82201ZM12 20C12.8285 20 13.5 19.3284 13.5 18.5C13.5 17.6716 12.8285 17 12 17C11.1716 17 10.5 17.6716 10.5 18.5C10.5 19.3284 11.1716 20 12 20Z\" fill=\"#828282\"></path> </g></svg>";

    public static List<CheckboxValue> AVAILABLE_VALUES_LIST = Arrays.asList(new CheckboxValue(UNKNOWN_SVG, "UNKNOWN"),
            new CheckboxValue(CHECK_SVG, "ALLOWED"),
            new CheckboxValue(REQUIRED_SVG, "REQUIRED"));


    protected PostCoordinationCheckboxConfig() {
        super(new CheckboxValue(UNKNOWN_SVG, "UNKNOWN"), AVAILABLE_VALUES_LIST);
    }

}
