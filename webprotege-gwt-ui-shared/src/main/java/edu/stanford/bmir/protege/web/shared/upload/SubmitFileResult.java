package edu.stanford.bmir.protege.web.shared.upload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-02
 */
@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName("webprotege.files.SubmitFile")
public abstract class SubmitFileResult implements Result {

    @JsonCreator
    public static SubmitFileResult create(@JsonProperty("fileSubmissionId") DocumentId fileSubmissionId) {
        return new AutoValue_SubmitFileResult(fileSubmissionId);
    }

    public abstract DocumentId getFileSubmissionId();

}
