package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import java.util.*;

public class PostCoordinationScaleValue {

    private final String axisIri;
    private final String axisLabel;
    private final List<String> valueIris;
    private final PostCoordinationAxisToGenericScale genericScale;

    public PostCoordinationScaleValue(String axisIri,
                                      String axisLabel,
                                      List<String> valueIris,
                                      PostCoordinationAxisToGenericScale genericScale) {
        this.axisIri = axisIri;
        this.axisLabel = axisLabel;
        this.valueIris = valueIris;
        this.genericScale = genericScale;
    }

    public static PostCoordinationScaleValue createEmpty(String axisIri,
                                                         String axisLabel,
                                                         PostCoordinationAxisToGenericScale genericScale) {
        return create(axisIri, axisLabel, new ArrayList<>(), genericScale);
    }

    public static PostCoordinationScaleValue create(String axisIri,
                                                    String axisLabel,
                                                    List<String> valueIris,
                                                    PostCoordinationAxisToGenericScale genericScale) {
        return new PostCoordinationScaleValue(axisIri, axisLabel, valueIris, genericScale);
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

    public PostCoordinationAxisToGenericScale getGenericScale() {
        return genericScale;
    }
}
