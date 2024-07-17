package edu.stanford.bmir.protege.web.client.form.complexcheckbox;

import java.util.Objects;

public class CheckboxValue {

    private final String svgImage;

    private final String value;


    public CheckboxValue(String svgImage, String value) {
        this.svgImage = svgImage;
        this.value = value;
    }


    public String getSvgImage() {
        return svgImage;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckboxValue that = (CheckboxValue) o;
        return  Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(svgImage, value);
    }

    @Override
    public String toString() {
        return "CheckboxValue{" +
                "value=" + value +
                '}';
    }
}
