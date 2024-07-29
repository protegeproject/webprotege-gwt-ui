package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static edu.stanford.bmir.protege.web.shared.linearization.ProcessUploadedLinearizationAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class ProcessUploadedLinearizationAction extends AbstractHasProjectAction<ProcessUploadedLinearizationResult> {

    public final static String CHANNEL = "webprotege.linearization.ProcessUploadedLinearization";

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
