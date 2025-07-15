package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
@AutoValue
public abstract class FormRegionPageRequest {

    @Nonnull
    public static FormRegionPageRequest get(@JsonProperty(PropertyNames.SUBJECT) @Nonnull FormSubject subject,
                                            @JsonProperty(PropertyNames.REGION_ID) @Nonnull FormRegionId formRegionId,
                                            @JsonProperty(PropertyNames.SOURCE_TYPE) @Nonnull FormPageRequest.SourceType sourceType,
                                            @JsonProperty(PropertyNames.PAGE_REQUEST) @Nonnull PageRequest pageRequest) {
        return new AutoValue_FormRegionPageRequest(subject, formRegionId, sourceType, pageRequest);
    }

    @Nonnull
    @JsonProperty(PropertyNames.SUBJECT)
    public abstract FormSubject getSubject();

    @Nonnull
    @JsonProperty(PropertyNames.REGION_ID)
    public abstract FormRegionId getFieldId();

    @Nonnull
    @JsonProperty(PropertyNames.SOURCE_TYPE)
    public abstract FormPageRequest.SourceType getSourceType();

    @Nonnull
    @JsonProperty(PropertyNames.PAGE_REQUEST)
    public abstract PageRequest getPageRequest();
}
