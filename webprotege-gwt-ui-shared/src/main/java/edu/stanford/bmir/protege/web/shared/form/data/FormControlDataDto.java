package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.PropertyNames;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;

@JsonSubTypes({
        @Type(EntityNameControlDataDto.class),
        @Type(GridControlDataDto.class),
        @Type(ImageControlDataDto.class),
        @Type(MultiChoiceControlDataDto.class),
        @Type(NumberControlDataDto.class),
        @Type(SingleChoiceControlDataDto.class),
        @Type(TextControlDataDto.class),
        @Type(FormDataDto.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface FormControlDataDto extends IsSerializable {

    <R> R accept(FormControlDataDtoVisitorEx<R> visitor);

    @Nonnull
    FormControlData toFormControlData();

    @JsonProperty(PropertyNames.DEPTH)
    int getDepth();
}
