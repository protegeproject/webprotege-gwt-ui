package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("FormPageRequest")
public abstract class FormPageRequest {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public enum SourceType {
        CONTROL_STACK,
        GRID_CONTROL
    }

    @JsonCreator
    @Nonnull
    public static FormPageRequest get(@JsonProperty(PropertyNames.FORM_ID) @Nonnull FormId formId,
                                      @JsonProperty(PropertyNames.SUBJECT) @Nonnull FormSubject subject,
                                      @JsonProperty(PropertyNames.REGION_ID) @Nonnull FormRegionId formFieldId,
                                      @JsonProperty(PropertyNames.SOURCE_TYPE) @Nonnull SourceType sourceType,
                                      @JsonProperty(PropertyNames.PAGE_REQUEST) @Nonnull PageRequest pageRequest) {
        return new AutoValue_FormPageRequest(formId, subject, formFieldId, sourceType, pageRequest);
    }

    @Nonnull
    @JsonProperty(PropertyNames.FORM_ID)
    public abstract FormId getFormId();

    @Nonnull
    @JsonProperty(PropertyNames.SUBJECT)
    public abstract FormSubject getSubject();

    @JsonProperty(PropertyNames.REGION_ID)
    @Nonnull
    public abstract FormRegionId getFieldId();

    @Nonnull
    @JsonProperty(PropertyNames.SOURCE_TYPE)
    public abstract SourceType getSourceType();

    @Nonnull
    @JsonProperty(PropertyNames.PAGE_REQUEST)
    public abstract PageRequest getPageRequest();
}
