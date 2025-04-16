package edu.stanford.bmir.protege.web.shared.card;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
        @JsonSubTypes.Type(FormContentDescriptor.class),
        @JsonSubTypes.Type(CustomContentDescriptor.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface EntityCardContentDescriptor {

    <R> R accept(EntityCardContentDescriptorVisitor<R> visitor);
}
