package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
public interface PrimitiveFormControlData {

    @JsonCreator
    public static PrimitiveFormControlData get(OWLPrimitive primitive) {
        if(primitive instanceof OWLEntity) {
            return get((OWLEntity) primitive);
        }
        else if(primitive instanceof IRI) {
            return get((IRI) primitive);
        }
        else {
            return get((OWLLiteral) primitive);
        }
    }

    static PrimitiveFormControlData get(OWLEntity entity) {
        return EntityFormControlData.get(entity);
    }

    static PrimitiveFormControlData get(IRI iri) {
        return IriFormControlData.get(iri);
    }

    static PrimitiveFormControlData get(OWLLiteral literal) {
        return LiteralFormControlData.get(literal);
    }

    static PrimitiveFormControlData get(String text) {
        return LiteralFormControlData.get(DataFactory.getOWLLiteral(text));
    }

    static PrimitiveFormControlData get(double value) {
        return LiteralFormControlData.get(DataFactory.getOWLLiteral(value));
    }

    static PrimitiveFormControlData get(boolean value) {
        return LiteralFormControlData.get(DataFactory.getOWLLiteral(value));
    }

    @Nonnull
    Optional<OWLEntity> asEntity();

    @Nonnull
    Optional<IRI> asIri();

    @Nonnull
    Optional<OWLLiteral> asLiteral();

    @Nonnull
    @JsonIgnore
    OWLPrimitive getPrimitive();
}
