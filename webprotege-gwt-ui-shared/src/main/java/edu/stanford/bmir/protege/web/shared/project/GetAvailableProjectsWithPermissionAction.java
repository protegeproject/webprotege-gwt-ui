package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.access.BasicCapability;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.projects.GetAvailableProjectsWithPermission")
public abstract class GetAvailableProjectsWithPermissionAction implements Action<GetAvailableProjectsWithPermissionResult> {

    @JsonCreator
    public static GetAvailableProjectsWithPermissionAction create(@JsonProperty("permission") @Nonnull BasicCapability permission) {
        return new AutoValue_GetAvailableProjectsWithPermissionAction(permission);
    }

    @Nonnull
    public abstract BasicCapability getPermission();
}
