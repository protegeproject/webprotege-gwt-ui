package edu.stanford.bmir.protege.web.shared.postcoordination;

import java.util.*;

public class PostcoordinationScaleValue {

    private final String axisIri;
    private final String axisLabel;
    private final List<String> valueIris;
    private final PostcoordinationAxisToGenericScale genericScale;

    public PostcoordinationScaleValue(String axisIri,
                                      String axisLabel,
                                      List<String> valueIris,
                                      PostcoordinationAxisToGenericScale genericScale) {
        this.axisIri = axisIri;
        this.axisLabel = axisLabel;
        this.valueIris = valueIris;
        this.genericScale = genericScale;
    }

    public static PostcoordinationScaleValue createEmpty(String axisIri,
                                                         String axisLabel,
                                                         PostcoordinationAxisToGenericScale genericScale) {
        return create(axisIri, axisLabel, new ArrayList<>(), genericScale);
    }

    public static PostcoordinationScaleValue create(String axisIri,
                                                    String axisLabel,
                                                    List<String> valueIris,
                                                    PostcoordinationAxisToGenericScale genericScale) {
        return new PostcoordinationScaleValue(axisIri, axisLabel, valueIris, genericScale);
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

    public PostcoordinationAxisToGenericScale getGenericScale() {
        return genericScale;
    }
}
