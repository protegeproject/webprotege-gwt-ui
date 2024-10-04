package edu.stanford.bmir.protege.web.shared.postcoordination;

import java.util.*;

public class PostcoordinationScaleValue {

    private final String axisIri;
    private final String axisLabel;
    private final List<ScaleValueIriAndName> scaleValueIriAndNames;
    private final PostcoordinationAxisToGenericScale genericScale;

    public PostcoordinationScaleValue(String axisIri,
                                      String axisLabel,
                                      List<ScaleValueIriAndName> scaleValueIriAndNames,
                                      PostcoordinationAxisToGenericScale genericScale) {
        this.axisIri = axisIri;
        this.axisLabel = axisLabel;
        this.scaleValueIriAndNames = scaleValueIriAndNames;
        this.genericScale = genericScale;
    }

    public static PostcoordinationScaleValue createEmpty(String axisIri,
                                                         String axisLabel,
                                                         PostcoordinationAxisToGenericScale genericScale) {
        return create(axisIri, axisLabel, new ArrayList<>(), genericScale);
    }

    public static PostcoordinationScaleValue create(String axisIri,
                                                    String axisLabel,
                                                    List<ScaleValueIriAndName> valueIris,
                                                    PostcoordinationAxisToGenericScale genericScale) {
        return new PostcoordinationScaleValue(axisIri, axisLabel, valueIris, genericScale);
    }

    public String getAxisIri() {
        return axisIri;
    }

    public String getAxisLabel() {
        return axisLabel;
    }

    public List<ScaleValueIriAndName> getValueIris() {
        return scaleValueIriAndNames;
    }

    public PostcoordinationAxisToGenericScale getGenericScale() {
        return genericScale;
    }
}
