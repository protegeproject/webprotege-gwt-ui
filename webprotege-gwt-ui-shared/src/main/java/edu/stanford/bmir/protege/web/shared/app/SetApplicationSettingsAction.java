package edu.stanford.bmir.protege.web.shared.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.application.SetApplicationSettings")
public abstract class SetApplicationSettingsAction implements Action<SetApplicationSettingsResult> {

    @GwtIncompatible
    public static SetApplicationSettingsAction create(@Nonnull ApplicationSettings applicationSettings) {
        return create(ChangeRequestId.get(UUID.randomUUID().toString()), applicationSettings);
    }

    @JsonCreator
    public static SetApplicationSettingsAction create(@JsonProperty("changeRequestId") @Nonnull ChangeRequestId changeRequestId,
                                                      @JsonProperty("applicationSettings") @Nonnull ApplicationSettings applicationSettings) {
        return new AutoValue_SetApplicationSettingsAction(changeRequestId, applicationSettings);
    }

    @Nonnull
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();

    public abstract ApplicationSettings getApplicationSettings();
}
