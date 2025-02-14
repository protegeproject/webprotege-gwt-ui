package edu.stanford.bmir.protege.web.client.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@JsonTypeName("ClassHierarchyDescriptor")
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ClassHierarchyDescriptor implements HierarchyDescriptor {

    @JsonCreator
    public static ClassHierarchyDescriptor get(@JsonProperty("roots") Set<OWLClass> roots) {
        return new AutoValue_ClassHierarchyDescriptor(new LinkedHashSet<>(roots));
    }

    /**
     * Get the class hierarchy descriptor for the class hierarchy rooted at owl:Thing
     */
    public static ClassHierarchyDescriptor get() {
        return get(Collections.singleton(DataFactory.getOWLThing()));
    }

    @JsonProperty("roots")
    public abstract Set<OWLClass> getRoots();

}
