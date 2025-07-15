package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class EntityFormSelector {

    public static EntityFormSelector get(@Nonnull @JsonProperty(PropertyNames.PROJECT_ID) ProjectId projectId,
                                         @Nonnull @JsonProperty(PropertyNames.CRITERIA) CompositeRootCriteria criteria,
                                         @Nonnull @JsonProperty(PropertyNames.PURPOSE) FormPurpose purpose,
                                         @Nonnull @JsonProperty(PropertyNames.FORM_ID) FormId formId) {
        return new AutoValue_EntityFormSelector(projectId, purpose, criteria, formId);
    }

    @JsonCreator
    protected static EntityFormSelector create(@Nonnull @JsonProperty(PropertyNames.PROJECT_ID) ProjectId projectId,
                                         @Nonnull @JsonProperty(PropertyNames.CRITERIA) CompositeRootCriteria criteria,
                                         @Nullable @JsonProperty(PropertyNames.PURPOSE) FormPurpose purpose,
                                         @Nonnull @JsonProperty(PropertyNames.FORM_ID) FormId formId) {
        FormPurpose normFormPurpose = purpose == null ? FormPurpose.ENTITY_EDITING : purpose;
        return get(projectId, criteria, normFormPurpose, formId);
    }

    @JsonProperty(PropertyNames.PROJECT_ID)
    public abstract ProjectId getProjectId();

    @JsonProperty(PropertyNames.PURPOSE)
    public abstract FormPurpose getPurpose();

    @JsonProperty(PropertyNames.CRITERIA)
    public abstract CompositeRootCriteria getCriteria();

    @JsonProperty(PropertyNames.FORM_ID)
    public abstract FormId getFormId();

}
