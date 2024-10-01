package edu.stanford.bmir.protege.web.shared.postcoordination;

public class PostCoordinationAxisToGenericScale {
    private final String postcoordinationAxis;
    private final String genericPostcoordinationScaleTopClass;
    private final String allowMultiValue;

    public PostCoordinationAxisToGenericScale(String postcoordinationAxis,
                                              String genericPostcoordinationScaleTopClass,
                                              String allowMultiValue) {
        this.postcoordinationAxis = postcoordinationAxis;
        this.genericPostcoordinationScaleTopClass = genericPostcoordinationScaleTopClass;
        this.allowMultiValue = allowMultiValue;
    }

    public PostCoordinationAxisToGenericScale create(String postcoordinationAxis,
                                                     String genericPostcoordinationScaleTopClass,
                                                     String allowMultiValue) {
        return new PostCoordinationAxisToGenericScale(postcoordinationAxis, genericPostcoordinationScaleTopClass, allowMultiValue);
    }

    public String getPostcoordinationAxis() {
        return postcoordinationAxis;
    }

    public String getGenericPostcoordinationScaleTopClass() {
        return genericPostcoordinationScaleTopClass;
    }

    public String getAllowMultiValue() {
        return allowMultiValue;
    }
}
