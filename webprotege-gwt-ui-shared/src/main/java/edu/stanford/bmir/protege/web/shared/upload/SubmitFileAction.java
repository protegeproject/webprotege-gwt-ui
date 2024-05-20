package edu.stanford.bmir.protege.web.shared.upload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-02
 */
@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName("webprotege.files.SubmitFile")
public abstract class SubmitFileAction implements Action<SubmitFileResult> {

    @JsonCreator
    public static SubmitFileAction create(String base64EncodedContents) {
        return new AutoValue_SubmitFileAction(base64EncodedContents);
    }

    @JsonProperty("fileContents")
    public abstract String getBase64EncodedContents();

}
