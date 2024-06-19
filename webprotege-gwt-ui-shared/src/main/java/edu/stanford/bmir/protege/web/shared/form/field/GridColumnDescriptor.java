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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-24
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridColumnDescriptor implements BoundControlDescriptor {

    @Nonnull
    public static GridColumnDescriptor get(@Nonnull GridColumnId id,
                                           @Nullable Optionality optionality,
                                           @Nullable Repeatability repeatability,
                                           @Nullable OwlBinding owlBinding,
                                           @Nonnull LanguageMap columnLabel,
                                           @Nonnull FormControlDescriptor formControlDescriptor) {
        return new AutoValue_GridColumnDescriptor(id,
                                                  optionality == null ? Optionality.REQUIRED : optionality,
                                                  repeatability == null ? Repeatability.NON_REPEATABLE : repeatability,
                                                  owlBinding, columnLabel, formControlDescriptor);
    }

    @JsonCreator
    @Nonnull
    public static GridColumnDescriptor get(@Nonnull @JsonProperty(PropertyNames.ID) String id,
                                           @Nullable @JsonProperty(PropertyNames.OPTIONALITY) Optionality optionality,
                                           @Nullable @JsonProperty(PropertyNames.REPEATABILITY) Repeatability repeatability,
                                           @Nullable @JsonProperty(PropertyNames.OWL_BINDING) OwlBinding owlBinding,
                                           @Nonnull @JsonProperty(PropertyNames.LABEL) LanguageMap columnLabel,
                                           @Nonnull @JsonProperty(PropertyNames.CONTROL) FormControlDescriptor formControlDescriptor) {
        return new AutoValue_GridColumnDescriptor(GridColumnId.get(id),
                                                  optionality == null ? Optionality.REQUIRED : optionality,
                                                  repeatability == null ? Repeatability.NON_REPEATABLE : repeatability,
                                                  owlBinding, columnLabel, formControlDescriptor);
    }

    @JsonIgnore
    @Nonnull
    public abstract GridColumnId getId();

    @JsonProperty(PropertyNames.ID)
    public String getGridColumnId() {
        return getId().getId();
    }

    @JsonIgnore
    public Stream<GridColumnDescriptor> getLeafColumnDescriptors() {
        FormControlDescriptor formControlDescriptor = getFormControlDescriptor();
        if(formControlDescriptor instanceof GridControlDescriptor) {
            // This is not a leaf column
            return ((GridControlDescriptor) formControlDescriptor).getLeafColumns();
        }
        else {
            // This is a leaf column
            return Stream.of(this);
        }
    }

    @JsonIgnore
    public Stream<GridColumnId> getLeafColumnIds() {
        return getLeafColumnDescriptors().map(GridColumnDescriptor::getId);
    }

    @JsonIgnore
    public boolean isLeafColumnDescriptor() {
        return !(getFormControlDescriptor() instanceof GridControlDescriptor);
    }

    @Nonnull
    public abstract Optionality getOptionality();

    @Nonnull
    public abstract Repeatability getRepeatability();

    @JsonProperty(PropertyNames.OWL_BINDING)
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @Override
    @Nonnull
    @JsonIgnore
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Override
    @Nonnull
    @JsonProperty(PropertyNames.CONTROL)
    public abstract FormControlDescriptor getFormControlDescriptor();

    @JsonIgnore
    public int getNestedColumnCount() {
        FormControlDescriptor formControlDescriptor = getFormControlDescriptor();
        if(formControlDescriptor instanceof GridControlDescriptor) {
            return ((GridControlDescriptor) getFormControlDescriptor()).getNestedColumnCount();
        }
        else {
            return 1;
        }
    }

    @JsonIgnore
    public boolean isRepeatable() {
        return getRepeatability().isRepeatable();
    }

    // TODO: Column width, grow, shrink

}
