package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.ProcessUploadedLinearization")
public abstract class ProcessUploadedLinearizationAction extends AbstractHasProjectAction<ProcessUploadedLinearizationResult> {

    @JsonCreator
    public static ProcessUploadedLinearizationAction create(@JsonProperty("projectId") ProjectId projectId,
                                                            @JsonProperty("documentId") DocumentId uploadedDocumentId) {
        return new AutoValue_ProcessUploadedLinearizationAction(projectId, uploadedDocumentId);
    }


    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract DocumentId getDocumentId();
}
