package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.PropertyNames.*;
import static edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonPropertyOrder({ID, OWL_BINDING, LABEL, FIELD_RUN, CONTROL, REPEATABILITY, OPTIONALITY, READ_ONLY, HELP})
@GwtCompatible(serializable = true)
@AutoValue
public abstract class FormFieldDescriptor implements HasFormFieldId, HasRepeatability, IsSerializable, BoundControlDescriptor {

    @Nonnull
    public static FormFieldDescriptor get(@Nonnull FormFieldId id,
                                          @Nullable OwlBinding owlBinding,
                                          @Nullable LanguageMap formLabel,
                                          @Nullable FieldRun fieldRun,
                                          @Nullable FormFieldDeprecationStrategy deprecationStrategy,
                                          @Nonnull FormControlDescriptor fieldDescriptor,
                                          Repeatability repeatability,
                                          Optionality optionality,
                                          boolean readOnly,
                                          @Nullable ExpansionState expansionState,
                                          @Nullable LanguageMap help) {
        return new AutoValue_FormFieldDescriptor(id,
                                                 owlBinding,
                                                 formLabel == null ? LanguageMap.empty() : formLabel,
                                                 fieldRun == null ? FieldRun.START : fieldRun,
                                                 fieldDescriptor,
                                                 optionality == null ? Optionality.REQUIRED : optionality,
                                                 repeatability == null ? Repeatability.NON_REPEATABLE : repeatability,
                                                 deprecationStrategy == null ? FormFieldDeprecationStrategy.DELETE_VALUES : deprecationStrategy,
                                                 readOnly,
                                                 expansionState == null ? ExpansionState.EXPANDED : expansionState,
                                                 help == null ? LanguageMap.empty() : help);
    }

    @JsonCreator
    @Nonnull
    public static FormFieldDescriptor getFromJson(@JsonProperty(PropertyNames.ID) @Nonnull String id,
                                                  @JsonProperty(PropertyNames.OWL_BINDING) @Nullable OwlBinding owlBinding,
                                                  @JsonProperty(PropertyNames.LABEL) @Nullable LanguageMap formLabel,
                                                  @JsonProperty(PropertyNames.FIELD_RUN) @Nullable FieldRun fieldRun,
                                                  @JsonProperty(PropertyNames.DEPRECATION_STRATEGY) @Nullable FormFieldDeprecationStrategy deprecationStrategy,
                                                  @JsonProperty(PropertyNames.CONTROL) @Nonnull FormControlDescriptor fieldDescriptor,
                                                  @JsonProperty(PropertyNames.REPEATABILITY) @Nullable Repeatability repeatability,
                                                  @JsonProperty(PropertyNames.OPTIONALITY) @Nullable Optionality optionality,
                                                  @JsonProperty(PropertyNames.READ_ONLY) boolean readOnly,
                                                  @JsonProperty(PropertyNames.INITIAL_EXPANSIONS_STATE) @Nullable ExpansionState expansionState,
                                                  @JsonProperty(PropertyNames.HELP) @Nullable LanguageMap help) {
        final FormFieldId formFieldId = FormFieldId.get(checkNotNull(id));
        return get(formFieldId, owlBinding, formLabel, fieldRun, deprecationStrategy, fieldDescriptor, repeatability, optionality, readOnly, expansionState, help);
    }

    @Nonnull
    @Override
    @JsonIgnore
    public abstract FormFieldId getId();

    @JsonProperty(ID)
    protected String getFormFieldId() {
        return getId().getId();
    }

    @Nullable
    @JsonProperty(PropertyNames.OWL_BINDING)
    protected abstract OwlBinding getOwlBindingInternal();

    @Override
    @Nonnull
    @JsonIgnore
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract FieldRun getFieldRun();

    @Override
    @Nonnull
    @JsonProperty(PropertyNames.CONTROL)
    public abstract FormControlDescriptor getFormControlDescriptor();

    @Nonnull
    public abstract Optionality getOptionality();

    @Nonnull
    public abstract Repeatability getRepeatability();

    @JsonProperty(PropertyNames.DEPRECATION_STRATEGY)
    @Nonnull
    public abstract FormFieldDeprecationStrategy getDeprecationStrategy();

    public abstract boolean isReadOnly();

    @JsonProperty(PropertyNames.INITIAL_EXPANSIONS_STATE)
    @Nonnull
    public abstract ExpansionState getInitialExpansionState();

    @Nonnull
    public abstract LanguageMap getHelp();

    @JsonIgnore
    public boolean isComposite() {
        return getFormControlDescriptor() instanceof SubFormControlDescriptor;
    }
}
