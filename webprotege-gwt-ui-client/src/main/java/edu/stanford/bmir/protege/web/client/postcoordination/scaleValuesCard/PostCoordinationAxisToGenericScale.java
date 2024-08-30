package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

public class PostCoordinationAxisToGenericScale {
    private final String postcoordinationAxis;
    private final String genericPostcoordinationScaleTopClass;
    private final ScaleAllowMultiValue allowMultiValue;

    public PostCoordinationAxisToGenericScale(String postcoordinationAxis,
                                              String genericPostcoordinationScaleTopClass,
                                              ScaleAllowMultiValue allowMultiValue) {
        this.postcoordinationAxis = postcoordinationAxis;
        this.genericPostcoordinationScaleTopClass = genericPostcoordinationScaleTopClass;
        this.allowMultiValue = allowMultiValue;
    }

    public PostCoordinationAxisToGenericScale create(String postcoordinationAxis,
                                                     String genericPostcoordinationScaleTopClass,
                                                     ScaleAllowMultiValue allowMultiValue) {
        return new PostCoordinationAxisToGenericScale(postcoordinationAxis, genericPostcoordinationScaleTopClass, allowMultiValue);
    }

    public String getPostcoordinationAxis() {
        return postcoordinationAxis;
    }

    public String getGenericPostcoordinationScaleTopClass() {
        return genericPostcoordinationScaleTopClass;
    }

    public ScaleAllowMultiValue getAllowMultiValue() {
        return allowMultiValue;
    }
}
