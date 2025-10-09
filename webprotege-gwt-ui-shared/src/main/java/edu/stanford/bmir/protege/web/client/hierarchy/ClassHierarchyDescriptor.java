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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
    public EntityMatchCriteria getEntityMatchCriteria() {
        ImmutableList.Builder<EntityMatchCriteria> builder = ImmutableList.builder();
        getRoots().forEach(root -> {
            builder.add(EntityIsCriteria.get(root));
            builder.add(SubClassOfCriteria.get(root, HierarchyFilterType.ALL));
        });
        return CompositeRootCriteria.get(builder.build(), MultiMatchType.ANY);
    }
}
