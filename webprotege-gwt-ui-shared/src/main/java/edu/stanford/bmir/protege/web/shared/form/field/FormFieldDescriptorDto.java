package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.HasFormRegionId;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class FormFieldDescriptorDto implements IsSerializable, HasFormRegionId {

    @JsonCreator
    @Nonnull
    public static FormFieldDescriptorDto get(@JsonProperty(PropertyNames.ID) FormRegionId formFieldId,
                                             @JsonProperty(PropertyNames.OWL_BINDING) OwlBinding owlBinding,
                                             @JsonProperty(PropertyNames.LABEL) LanguageMap newlabel,
                                             @JsonProperty(PropertyNames.FIELD_RUN) FieldRun fieldRun,
                                             @JsonProperty(PropertyNames.CONTROL) FormControlDescriptorDto descriptorDto,
                                             @JsonProperty(PropertyNames.OPTIONALITY) Optionality optionality,
                                             @JsonProperty(PropertyNames.REPEATABILITY) Repeatability repeatability,
                                             @JsonProperty(PropertyNames.PAGE_SIZE) int pageSize,
                                             @JsonProperty(PropertyNames.DEPRECATION_STRATEGY) FormFieldDeprecationStrategy deprecationStrategy,
                                             @JsonProperty(PropertyNames.READ_ONLY) boolean newReadOnly,
                                             @JsonProperty(PropertyNames.ACCESS_MODE) FormFieldAccessMode accessMode,
                                             @JsonProperty(PropertyNames.INITIAL_EXPANSIONS_STATE) ExpansionState initialExpansionState,
                                             @JsonProperty(PropertyNames.HELP) LanguageMap help) {
        if(pageSize < 0) {
            throw new IllegalArgumentException("pageSize must be greater than zero");
        }
        if(pageSize == 0) {
            pageSize = FormPageRequest.DEFAULT_PAGE_SIZE;
        }
        return new AutoValue_FormFieldDescriptorDto(formFieldId,
                owlBinding,
                newlabel,
                fieldRun,
                descriptorDto,
                optionality,
                repeatability,
                pageSize,
                deprecationStrategy,
                newReadOnly,
                accessMode,
                initialExpansionState,
                help);
    }


    @Nonnull
    @Override
    @JsonProperty(PropertyNames.ID)
    public abstract FormRegionId getId();

    @JsonProperty(PropertyNames.OWL_BINDING)
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @Nonnull
    @JsonIgnore
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    @JsonProperty(PropertyNames.LABEL)
    public abstract LanguageMap getLabel();

    @Nonnull
    @JsonProperty(PropertyNames.FIELD_RUN)
    public abstract FieldRun getFieldRun();

    @Nonnull
    @JsonProperty(PropertyNames.CONTROL)
    public abstract FormControlDescriptorDto getFormControlDescriptor();

    @Nonnull
    @JsonProperty(PropertyNames.OPTIONALITY)
    public abstract Optionality getOptionality();

    @Nonnull
    @JsonProperty(PropertyNames.REPEATABILITY)
    public abstract Repeatability getRepeatability();

    @JsonProperty(PropertyNames.PAGE_SIZE)
    public abstract int getPageSize();

    @Nonnull
    @JsonProperty(PropertyNames.DEPRECATION_STRATEGY)
    public abstract FormFieldDeprecationStrategy getDeprecationStrategy();

    @JsonProperty(PropertyNames.READ_ONLY)
    public abstract boolean isReadOnly();

    @JsonProperty(PropertyNames.ACCESS_MODE)
    public abstract FormFieldAccessMode getAccessMode();

    @Nonnull
    @JsonProperty(PropertyNames.INITIAL_EXPANSIONS_STATE)
    public abstract ExpansionState getInitialExpansionState();

    @Nonnull
    @JsonProperty(PropertyNames.HELP)
    public abstract LanguageMap getHelp();

    @JsonIgnore
    public boolean isComposite() {
        return getFormControlDescriptor() instanceof SubFormControlDescriptor;
    }

    public FormFieldDescriptor toFormFieldDescriptor() {
        return FormFieldDescriptor.get(
                getId(),
                getOwlBindingInternal(),
                getLabel(),
                getFieldRun(),
                getDeprecationStrategy(),
                getFormControlDescriptor().toFormControlDescriptor(),
                getRepeatability(),
                getPageSize(),
                getOptionality(),
                isReadOnly(),
                getInitialExpansionState(),
                getHelp()
        );
    }

    public boolean isReadWrite() {
        return getAccessMode().equals(FormFieldAccessMode.READ_WRITE);
    }
}
