package edu.stanford.bmir.protege.web.shared.gh;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-21
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GitHubTimeStamp {

    @JsonCreator
    public static GitHubTimeStamp valueOf(@Nonnull String value) {
        return new AutoValue_GitHubTimeStamp(value);
    }

    public static GitHubTimeStamp epoch() {
        return valueOf("1970-01-01T00:00:00Z");
    }

    @JsonValue
    public abstract String getValue();
}
