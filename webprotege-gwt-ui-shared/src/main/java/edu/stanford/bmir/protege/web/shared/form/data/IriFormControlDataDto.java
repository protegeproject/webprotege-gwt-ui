package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("IriFormControlDataDto")
public abstract class IriFormControlDataDto extends PrimitiveFormControlDataDto {

    @JsonCreator
    public static IriFormControlDataDto get(@JsonProperty(PropertyNames.IRI) @Nonnull IRIData iriData) {
        return new AutoValue_IriFormControlDataDto(iriData);
    }

    @Nonnull
    @JsonProperty(PropertyNames.IRI)
    public abstract IRIData getIri();

    @Nonnull
    @Override
    public PrimitiveFormControlData toPrimitiveFormControlData() {
        return IriFormControlData.get(getIri().getObject());
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    @JsonIgnore
    public OWLPrimitiveData getPrimitiveData() {
        return getIri();
    }
}
