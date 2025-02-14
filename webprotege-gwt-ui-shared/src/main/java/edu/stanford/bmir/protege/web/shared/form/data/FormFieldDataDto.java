package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptorDto;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import java.io.Serializable;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormFieldDataDto implements IsSerializable, Serializable {

    @JsonCreator
    @Nonnull
    public static FormFieldDataDto get(@JsonProperty(PropertyNames.FIELD) @Nonnull FormFieldDescriptorDto descriptor,
                                       @JsonProperty(PropertyNames.DATA) @Nonnull Page<FormControlDataDto> formControlData) {
        return new AutoValue_FormFieldDataDto(descriptor, formControlData);
    }

    @Nonnull
    @JsonProperty(PropertyNames.FIELD)
    public abstract FormFieldDescriptorDto getFormFieldDescriptor();

    /**
     * Gets the page of form control values for this field.
     */
    @Nonnull
    @JsonProperty(PropertyNames.DATA)
    public abstract Page<FormControlDataDto> getFormControlData();

    @Nonnull
    public FormFieldData toFormFieldData() {
        return FormFieldData.get(
                getFormFieldDescriptor().toFormFieldDescriptor(),
                getFormControlData().transform(FormControlDataDto::toFormControlData));
    }

    @JsonIgnore
    @Nonnull
    public FormFieldData getFormFieldData() {
        return FormFieldData.get(getFormFieldDescriptor().toFormFieldDescriptor(),
                getFormControlData().transform(FormControlDataDto::toFormControlData));
    }
}
