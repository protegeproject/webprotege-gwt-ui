package edu.stanford.bmir.protege.web.client.hierarchy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(ClassHierarchyDescriptor.class),
        @JsonSubTypes.Type(ObjectPropertyHierarchyDescriptor.class),
        @JsonSubTypes.Type(DataPropertyHierarchyDescriptor.class),
        @JsonSubTypes.Type(AnnotationPropertyHierarchyDescriptor.class)
})
public interface HierarchyDescriptor {
}
