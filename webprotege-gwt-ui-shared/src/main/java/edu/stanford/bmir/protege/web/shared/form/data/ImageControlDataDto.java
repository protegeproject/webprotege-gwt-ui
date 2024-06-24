package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.field.ImageControlDescriptor;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ImageControlDataDto")
public abstract class ImageControlDataDto implements FormControlDataDto {

    public ImageControlDataDto(){

    }
    @JsonCreator
    @Nonnull
    public static ImageControlDataDto get(@JsonProperty("descriptor") @Nonnull ImageControlDescriptor descriptor,
                                          @JsonProperty("iri") @Nonnull IRI iri,
                                          @JsonProperty("depth") int depth) {
        return new AutoValue_ImageControlDataDto(depth, descriptor, iri);
    }

    @JsonProperty("descriptor")
    @Nonnull
    public abstract ImageControlDescriptor getDescriptor();

    @JsonProperty("iri")
    @Nullable
    protected abstract IRI getIriInternal();

    @Nonnull
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
