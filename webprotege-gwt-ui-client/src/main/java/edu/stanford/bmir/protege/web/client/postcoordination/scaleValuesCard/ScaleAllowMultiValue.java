package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.resources.client.DataResource;

import javax.annotation.Nullable;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

public enum ScaleAllowMultiValue {
    NotAllowed(Optional.of(BUNDLE.singleScaleValue()), "In the Coding Tool, this axis allows the selection of multiple values"),
    AllowAlways(Optional.of(BUNDLE.multipleScaleValues()),"In the Coding Tool, this axis allows the selection of a single value"),
    AllowedExceptFromSameBlock(Optional.of(BUNDLE.allowScaleValueIfNotFromSameBlock()),"In the Coding Tool, this axis allows the selection of multiple values, but only one value from each block");

    private final String tooltip;

    @Nullable
    private transient DataResource image;

    ScaleAllowMultiValue(Optional<DataResource> image, String tooltip) {
        this.tooltip = tooltip;
        this.image = image.orElse(null);
    }

    public String getTooltip() {
        return tooltip;
    }

    public Optional<DataResource> getImage() {
        return Optional.ofNullable(image);
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
