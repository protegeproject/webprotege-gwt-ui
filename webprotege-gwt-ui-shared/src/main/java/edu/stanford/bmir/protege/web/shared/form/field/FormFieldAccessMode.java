package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FormFieldAccessMode {

    @JsonProperty("ReadWrite")
    READ_WRITE,

    @JsonProperty("ReadOnly")
    READ_ONLY;

    public boolean isReadOnly() {
        return READ_ONLY.equals(this);
    }
}
