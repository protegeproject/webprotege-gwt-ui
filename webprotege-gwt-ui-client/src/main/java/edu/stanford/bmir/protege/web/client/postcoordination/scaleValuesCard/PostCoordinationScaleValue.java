package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import java.util.List;

public class PostCoordinationScaleValue {

    private String axisIri;
    private String axisLabel;
    private List<String> valueIris;

    public PostCoordinationScaleValue(String axisIri, String axisLabel, List<String> valueIris) {
        this.axisIri = axisIri;
        this.axisLabel = axisLabel;
        this.valueIris = valueIris;
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
