package edu.stanford.bmir.protege.web.shared.perspective;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ValueObject {
    @JsonIgnore
    String value();
}
