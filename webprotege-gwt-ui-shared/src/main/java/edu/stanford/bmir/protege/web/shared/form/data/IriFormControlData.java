package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-07
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class IriFormControlData implements PrimitiveFormControlData {

    public static IriFormControlData get(@JsonProperty(PropertyNames.IRI) @Nonnull IRI iri) {
        return new AutoValue_IriFormControlData(iri);
    }

    @JsonProperty(PropertyNames.IRI)
    @Nonnull
    public abstract IRI getIri();

    @Nonnull
    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<IRI> asIri() {
        return Optional.of(getIri());
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public OWLPrimitive getPrimitive() {
        return getIri();
    }
}
