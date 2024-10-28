package edu.stanford.bmir.protege.web.client.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.Collections;
import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("DataPropertyHierarchyDescriptor")
public abstract class DataPropertyHierarchyDescriptor implements HierarchyDescriptor {


    @JsonCreator
    public static DataPropertyHierarchyDescriptor get(@JsonProperty("roots") Set<OWLDataProperty> roots) {
        return new AutoValue_DataPropertyHierarchyDescriptor(roots);
    }

    public static DataPropertyHierarchyDescriptor get() {
        return get(Collections.singleton(DataFactory.getOWLDataProperty(OWLRDFVocabulary.OWL_TOP_DATA_PROPERTY.getIRI())));
    }

    @JsonProperty("roots")
    public abstract Set<OWLDataProperty> getRoots();
}
