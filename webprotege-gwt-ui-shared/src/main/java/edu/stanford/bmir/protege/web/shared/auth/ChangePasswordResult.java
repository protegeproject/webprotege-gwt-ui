package edu.stanford.bmir.protege.web.shared.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ChangePassword")
public abstract class ChangePasswordResult implements Result {

    @Nonnull
    public abstract AuthenticationResponse getResponse();

    @JsonCreator
    public static ChangePasswordResult create(@JsonProperty("response") AuthenticationResponse response) {
        return new AutoValue_ChangePasswordResult(response);
    }
}
