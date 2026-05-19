package edu.stanford.bmir.protege.web.shared.merge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.projects.MergeUploadedProject")
public abstract class MergeUploadedProjectAction extends AbstractHasProjectAction<MergeUploadedProjectResult> {


    @JsonCreator
    public static MergeUploadedProjectAction create(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                                    @JsonProperty("projectId") ProjectId projectId,
                                                    @JsonProperty("documentId") DocumentId uploadedDocumentId,
                                                    @JsonProperty("commitMessage") String commitMessage) {
        return new AutoValue_MergeUploadedProjectAction(changeRequestId, projectId, uploadedDocumentId, commitMessage);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract DocumentId getDocumentId();

    public abstract String getCommitMessage();
}
