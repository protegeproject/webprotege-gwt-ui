package edu.stanford.bmir.protege.web.server.upload;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-15
 */
@AutoValue
public abstract class FileSubmissionId {

    public static FileSubmissionId valueOf(@Nonnull String id) {
        return new AutoValue_FileSubmissionId(id);
    }

    @Nonnull
    public abstract String getId();
}
