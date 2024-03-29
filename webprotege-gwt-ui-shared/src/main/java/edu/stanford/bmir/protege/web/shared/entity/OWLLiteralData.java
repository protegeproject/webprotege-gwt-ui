package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.HasLexicalForm;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.shortform.ShortForm;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("LiteralData")
public abstract class OWLLiteralData extends OWLPrimitiveData implements HasLexicalForm {

    public static OWLLiteralData get(@JsonProperty("value") @Nonnull OWLLiteral literal) {
        return new AutoValue_OWLLiteralData(literal);
    }

    @JsonCreator
    private static OWLLiteralData get(@JsonProperty("value") String value,
                                      @JsonProperty("lang") String lang,
                                      @JsonProperty(value = "datatype") String iri) {
        return get(new OWLLiteralImpl(value, lang == null ? "" : lang, Optional.ofNullable(iri).map(IRI::create).map(OWLDatatypeImpl::new).orElse(null)));
    }

    @JsonIgnore
    @Nonnull
    @Override
    public abstract OWLLiteral getObject();

    @JsonProperty("value")
    public String getValue() {
        return getLiteral().getLiteral();
    }

    @JsonProperty("datatype")
    public String getDatatype() {
        OWLDatatype datatype = getLiteral().getDatatype();
        return datatype.getIRI().toString();
    }

    @JsonIgnore
    @Override
    public PrimitiveType getType() {
        return PrimitiveType.LITERAL;
    }

    @JsonIgnore
    public OWLLiteral getLiteral() {
        return getObject();
    }

    @JsonIgnore
    @Override
    public String getBrowserText() {
        OWLLiteral literal = getLiteral();
        return literal.getLiteral();
    }

    @JsonIgnore
    @Override
    public String getUnquotedBrowserText() {
        return getBrowserText();
    }

    @JsonIgnore
    @Override
    public String getLexicalForm() {
        return getLiteral().getLiteral();
    }

    @JsonIgnore
    public boolean hasLang() {
        return getLiteral().hasLang();
    }

    @Nonnull
    public String getLang() {
        return getLiteral().getLang();
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return defaultValue;
    }

    @Override
    public Optional<OWLAnnotationValue> asAnnotationValue() {
        return Optional.of(getLiteral());
    }

    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.empty();
    }

    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.of(getLiteral());
    }

    @JsonIgnore
    @Override
    public boolean isDeprecated() {
        return super.isDeprecated();
    }

    @JsonIgnore
    @Override
    public ImmutableList<ShortForm> getShortForms() {
        return ImmutableList.of();
    }
}
