package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.Stream;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class GridColumnDescriptorDto {


    @JsonCreator
    @Nonnull
    public static GridColumnDescriptorDto get(@JsonProperty(PropertyNames.ID) @Nonnull GridColumnId columnId,
                                              @JsonProperty(PropertyNames.OPTIONALITY) @Nonnull Optionality optionality,
                                              @JsonProperty(PropertyNames.REPEATABILITY) @Nonnull Repeatability repeatability,
                                              @JsonProperty(PropertyNames.OWL_BINDING) @Nullable OwlBinding binding,
                                              @JsonProperty(PropertyNames.LABEL) @Nonnull LanguageMap label,
                                              @JsonProperty(PropertyNames.CONTROL) @Nonnull FormControlDescriptorDto formControlDescriptorDto) {
        return new AutoValue_GridColumnDescriptorDto(columnId,
                                                     optionality,
                                                     repeatability,
                                                     binding,
                                                     label,
                                                     formControlDescriptorDto);
    }


    @Nonnull
    @JsonProperty(PropertyNames.ID)
    public abstract GridColumnId getId();

    @Nonnull
    @JsonProperty(PropertyNames.OPTIONALITY)
    public abstract Optionality getOptionality();

    @Nonnull
    @JsonProperty(PropertyNames.REPEATABILITY)
    public abstract Repeatability getRepeatability();

    @Nullable
    @JsonProperty(PropertyNames.OWL_BINDING)
    protected abstract OwlBinding getOwlBindingInternal();

    @Nonnull
    @JsonIgnore
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract FormControlDescriptorDto getFormControlDescriptor();

    public GridColumnDescriptor toGridColumnDescriptor() {
        return GridColumnDescriptor.get(
                getId(),
                getOptionality(),
                getRepeatability(),
                getOwlBindingInternal(),
                getLabel(),
                getFormControlDescriptor().toFormControlDescriptor()
        );
    }

    @JsonIgnore
    public int getNestedColumnCount() {
        FormControlDescriptorDto formControlDescriptor = getFormControlDescriptor();
        if(formControlDescriptor instanceof GridControlDescriptorDto) {
            return ((GridControlDescriptorDto) getFormControlDescriptor()).getNestedColumnCount();
        }
        else {
            return 1;
        }
    }


    @JsonIgnore
    public Stream<GridColumnDescriptorDto> getLeafColumnDescriptors() {
        FormControlDescriptorDto formControlDescriptor = getFormControlDescriptor();
        if(formControlDescriptor instanceof GridControlDescriptorDto) {
            // This is not a leaf column
            return ((GridControlDescriptorDto) formControlDescriptor).getLeafColumns();
        }
        else {
            // This is a leaf column
            return Stream.of(this);
        }
    }


    @JsonIgnore
    public boolean isLeafColumnDescriptor() {
        return !(getFormControlDescriptor() instanceof GridControlDescriptorDto);
    }

}
