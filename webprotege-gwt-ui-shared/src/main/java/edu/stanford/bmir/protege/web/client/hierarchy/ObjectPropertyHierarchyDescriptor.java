package edu.stanford.bmir.protege.web.client.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ObjectPropertyHierarchyDescriptor")
public abstract class ObjectPropertyHierarchyDescriptor implements HierarchyDescriptor {

    @JsonCreator
    public static ObjectPropertyHierarchyDescriptor get(@JsonProperty("roots") Set<OWLObjectProperty> roots) {
        return new AutoValue_ObjectPropertyHierarchyDescriptor(new LinkedHashSet<>(roots));
    }

    public static ObjectPropertyHierarchyDescriptor get() {
        return get(Collections.singleton(DataFactory.getOWLObjectProperty(OWLRDFVocabulary.OWL_TOP_OBJECT_PROPERTY.getIRI())));
    }

    @JsonProperty("roots")
    public abstract Set<OWLObjectProperty> getRoots();
}
