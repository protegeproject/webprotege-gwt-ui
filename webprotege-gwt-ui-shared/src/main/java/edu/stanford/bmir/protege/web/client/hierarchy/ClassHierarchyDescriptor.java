package edu.stanford.bmir.protege.web.client.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public Optional<RootCriteria> getEntityMatchCriteria() {
        List<EntityMatchCriteria> matchCriteria = getRoots()
                .stream()
                .map(root -> SubClassOfCriteria.get(root, HierarchyFilterType.ALL))
                .collect(Collectors.toList());
        RootCriteria rootCriteria = CompositeRootCriteria.get(ImmutableList.copyOf(matchCriteria),
                MultiMatchType.ANY);
        return Optional.of(rootCriteria);
    }
}
