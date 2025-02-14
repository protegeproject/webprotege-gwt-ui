package edu.stanford.bmir.protege.web.client.form.complexcheckbox;

import java.util.Objects;

public class CheckboxValue {
    private final String svgImage;

    private final String value;

    private final String tooltip;


    public CheckboxValue(String svgImage, String value, String tooltip) {
        this.svgImage = svgImage;
        this.value = value;
        this.tooltip = tooltip;
    }


    public String getSvgImage() {
        return svgImage;
    }

    public String getValue() {
        return value;
    }

    public String getTooltip() {
        return tooltip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckboxValue that = (CheckboxValue) o;
        return Objects.equals(svgImage, that.svgImage) && Objects.equals(value, that.value) && Objects.equals(tooltip, that.tooltip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(svgImage, value, tooltip);
    }
}

