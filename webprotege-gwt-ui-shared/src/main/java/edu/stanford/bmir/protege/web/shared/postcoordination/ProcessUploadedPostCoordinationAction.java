package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static edu.stanford.bmir.protege.web.shared.postcoordination.ProcessUploadedPostCoordinationAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class ProcessUploadedPostCoordinationAction extends AbstractHasProjectAction<ProcessUploadedPostCoordinationResult> {

    public final static String CHANNEL = "webprotege.postcoordination.ProcessUploadedPostCoordination";

    @JsonCreator
    public static ProcessUploadedPostCoordinationAction create(@JsonProperty("projectId") ProjectId projectId,
                                                            @JsonProperty("documentId") DocumentId uploadedDocumentId) {
        return new AutoValue_ProcessUploadedPostCoordinationAction(projectId, uploadedDocumentId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract DocumentId getDocumentId();

}
