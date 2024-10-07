package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static edu.stanford.bmir.protege.web.shared.postcoordination.ProcessUploadedCustomScalesAction.CHANNEL;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class ProcessUploadedCustomScalesAction extends AbstractHasProjectAction<ProcessUploadedCustomScalesResult> {

    public static final String CHANNEL = "webprotege.postcoordination.ProcessFirstPostCoordinationScaleValues";

    @JsonCreator
    public static ProcessUploadedCustomScalesAction create(@JsonProperty("projectId") ProjectId projectId,
                                                               @JsonProperty("documentId") DocumentId uploadedDocumentId) {
        return new AutoValue_ProcessUploadedCustomScalesAction(projectId, uploadedDocumentId);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    public abstract DocumentId getDocumentId();
}
