package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("entity")
public abstract class EntityFormControlData implements PrimitiveFormControlData {

    public static EntityFormControlData get(@Nonnull OWLEntity entity) {
        return new AutoValue_EntityFormControlData(entity);
    }

    @Nonnull
    @JsonProperty("@type")
    String type() {
        return getEntity().getEntityType().getPrefixedName();
    }

    @Nonnull
    @JsonProperty("iri")
    String iri() {
        return getEntity().getIRI().toString();
    }

    @Nonnull
    @JsonIgnore
    public abstract OWLEntity getEntity();

    @Nonnull
    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.of(getEntity());
    }

    @Nonnull
    @Override
    public Optional<IRI> asIri() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public OWLPrimitive getPrimitive() {
        return getEntity();
    }
}
