package edu.stanford.bmir.protege.web.shared.icd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nullable;
import java.util.List;


@AutoValue
@GwtCompatible(serializable = true)
public abstract class AncestorClassHierarchy {

    @JsonCreator
    public static AncestorClassHierarchy create(@JsonProperty("currentNode") OWLEntityData owlClass, @JsonProperty("children") @Nullable List<AncestorClassHierarchy> children) {
        return new AutoValue_AncestorClassHierarchy(owlClass, children);
    }

    public abstract OWLEntityData getCurrentNode();

    public abstract List<AncestorClassHierarchy> getChildren();
}
