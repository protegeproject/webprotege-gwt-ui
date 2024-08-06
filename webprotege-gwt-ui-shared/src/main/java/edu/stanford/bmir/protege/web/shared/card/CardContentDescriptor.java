package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
        @JsonSubTypes.Type(FormCardContentDescriptor.class),
        @JsonSubTypes.Type(CardContentDescriptor.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface CardContentDescriptor {


}
