package edu.stanford.bmir.protege.web.client.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.Collections;
import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("AnnotationPropertyHierarchyDescriptor")
public abstract class AnnotationPropertyHierarchyDescriptor implements HierarchyDescriptor {
    
    @JsonCreator
    public static AnnotationPropertyHierarchyDescriptor get(@JsonProperty("roots") Set<OWLAnnotationProperty> roots) {
        return new AutoValue_AnnotationPropertyHierarchyDescriptor(roots);
    }

    public static AnnotationPropertyHierarchyDescriptor get() {
        return get(Collections.emptySet());
    }

    @JsonProperty("roots")
    public abstract Set<OWLAnnotationProperty> getRoots();

}
