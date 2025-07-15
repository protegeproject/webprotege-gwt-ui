package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
@JsonTypeName("webprotege.forms.SetEntityFormsData")
public class SetEntityFormsDataAction implements ProjectAction<SetEntityFormDataResult> {

    private ProjectId projectId;

    private OWLEntity entity;

    private String commitMessage;

    private ImmutableMap<FormId, FormData> pristineFormsData;

    private FormDataByFormId editedFormsData;

    public SetEntityFormsDataAction(@Nonnull ProjectId projectId,
                                    @Nonnull OWLEntity entity,
                                    @Nonnull String commitMessage,
                                    @Nonnull ImmutableMap<FormId, FormData> pristineFormsData,
                                    @Nonnull FormDataByFormId editedFormsData) {
        this.projectId = checkNotNull(projectId);
        this.entity = checkNotNull(entity);
        this.commitMessage = checkNotNull(commitMessage);
        this.pristineFormsData = checkNotNull(pristineFormsData);
        this.editedFormsData = checkNotNull(editedFormsData);
        checkArgument(editedFormsData.getFormIds().stream().allMatch(pristineFormsData::containsKey),
                      "Missing pristine forms data.  Edited forms: " + editedFormsData.getFormIds() + " Pristine forms: " + pristineFormsData.keySet());
    }

    @GwtSerializationConstructor
    private SetEntityFormsDataAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    @Nonnull
    public ImmutableMap<FormId, FormData> getPristineFormsData() {
        return pristineFormsData;
    }

    @Nonnull
    public FormDataByFormId getEditedFormsData() {
        return editedFormsData;
    }
}
