package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.field.ImageControlDescriptor;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ImageControlDataDto")
public abstract class ImageControlDataDto implements FormControlDataDto {

    @JsonCreator
    @Nonnull
    public static ImageControlDataDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull ImageControlDescriptor descriptor,
                                          @JsonProperty(PropertyNames.IRI) @Nonnull IRI iri,
                                          @JsonProperty(PropertyNames.DEPTH) int depth) {
        return new AutoValue_ImageControlDataDto(depth, descriptor, iri);
    }

    @JsonProperty(PropertyNames.CONTROL)
    @Nonnull
    public abstract ImageControlDescriptor getDescriptor();

    @JsonProperty(PropertyNames.IRI)
    @Nullable
    protected abstract IRI getIriInternal();

    @Nonnull
    @JsonIgnore
    public Optional<IRI> getIri() {
        return Optional.ofNullable(getIriInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public FormControlData toFormControlData() {
        return ImageControlData.get(getDescriptor(),
                getIriInternal());
    }
}
