package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(ProcessUploadedSiblingsOrderingAction.CHANNEL)
public abstract class ProcessUploadedSiblingsOrderingAction extends AbstractHasProjectAction<ProcessUploadedSiblingsOrderingResult> {

    public final static String CHANNEL = "webprotege.hierarchies.ProcessUploadedSiblingsOrdering";

    @JsonCreator
    public static ProcessUploadedSiblingsOrderingAction create(@JsonProperty("projectId") ProjectId projectId,
                                                               @JsonProperty("documentId") DocumentId uploadedDocumentId) {
        return new AutoValue_ProcessUploadedSiblingsOrderingAction(projectId, uploadedDocumentId);
    }


    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract DocumentId getDocumentId();
}
