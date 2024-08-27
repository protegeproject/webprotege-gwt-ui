package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import java.util.*;

public class PostCoordinationScaleValue {

    private final String axisIri;
    private final String axisLabel;
    private final List<String> valueIris;

    public PostCoordinationScaleValue(String axisIri, String axisLabel, List<String> valueIris) {
        this.axisIri = axisIri;
        this.axisLabel = axisLabel;
        this.valueIris = valueIris;
    }

    public static PostCoordinationScaleValue createEmpty(String axisIri, String axisLabel){
        return new PostCoordinationScaleValue(axisIri, axisLabel, new ArrayList<>());
    }

    public String getAxisIri() {
        return axisIri;
    }

    public String getAxisLabel() {
        return axisLabel;
    }

    public List<String> getValueIris() {
        return valueIris;
    }
}
