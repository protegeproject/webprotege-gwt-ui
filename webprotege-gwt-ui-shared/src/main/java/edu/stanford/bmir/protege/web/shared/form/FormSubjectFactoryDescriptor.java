package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-11
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormSubjectFactoryDescriptor {

    @JsonCreator
    public static FormSubjectFactoryDescriptor get(@Nonnull @JsonProperty(PropertyNames.ENTITY_TYPE) EntityType entityType,
                                                   @Nullable @JsonProperty(PropertyNames.PARENT) OWLClass parent,
                                                   @Nonnull @JsonProperty(PropertyNames.TARGET_ONTOLOGY_IRI) Optional<IRI> targetOntologyIri) {
        return new AutoValue_FormSubjectFactoryDescriptor(entityType,
                                                          parent,
                                                          targetOntologyIri.orElse(null));
    }

    public static String getDefaultGeneratedNamePattern() {
        return "id-${uuid}";
    }

    @Nonnull
    @JsonProperty(PropertyNames.ENTITY_TYPE)
    public abstract EntityType<?> getEntityType();

    /**
     * Gets a list of parents that can be used to position the fresh entity in
     * a hierarchy
     */
    @JsonIgnore
    @Nonnull
    public Optional<OWLClass> getParent() {
        return Optional.ofNullable(getParentInternal());
    }

    @JsonProperty(PropertyNames.PARENT)
    @Nullable
    protected abstract OWLClass getParentInternal();

    @JsonProperty(PropertyNames.TARGET_ONTOLOGY_IRI)
    @Nullable
    protected abstract IRI getTargetOntologyIriInternal();

    @JsonIgnore
    @Nonnull
    public Optional<IRI> getTargetOntologyIri() {
        return Optional.ofNullable(getTargetOntologyIriInternal());
    }
}
