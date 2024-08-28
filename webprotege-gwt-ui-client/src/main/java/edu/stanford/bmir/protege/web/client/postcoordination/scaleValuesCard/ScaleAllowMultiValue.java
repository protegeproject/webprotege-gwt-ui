package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

public enum ScaleAllowMultiValue {
    NotAllowed("This does not allow multiple values"),
    AllowAlways("This always allows multiple values"),
    AllowedExceptFromSameBlock("This allows multiple values except from the same block");

    private final String description;

    ScaleAllowMultiValue(String description) {
        this.description = description;
    }

    public String getBrowserTest() {
        return description;
    }

    public static ScaleAllowMultiValue fromString(String value) {
        for (ScaleAllowMultiValue scaleAllowMultiValue : ScaleAllowMultiValue.values()) {
            if (scaleAllowMultiValue.name().equalsIgnoreCase(value)) {
                return scaleAllowMultiValue;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
