package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.access.BasicCapability;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
@JsonTypeName("webprotege.auth.GetProjectPermissions")
public class GetProjectPermissionsResult implements Result {

    private Set<BasicCapability> allowedActions;

    private GetProjectPermissionsResult() {
    }

    private GetProjectPermissionsResult(Set<BasicCapability> allowedActions) {
        this.allowedActions = ImmutableSet.copyOf(allowedActions);
    }

    public static GetProjectPermissionsResult create(Set<BasicCapability> allowedActions) {
        return new GetProjectPermissionsResult(allowedActions);
    }

    public Set<BasicCapability> getAllowedActions() {
        return allowedActions;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(allowedActions);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectPermissionsResult)) {
            return false;
        }
        GetProjectPermissionsResult other = (GetProjectPermissionsResult) obj;
        return this.allowedActions.equals(other.allowedActions);
    }


    @Override
    public String toString() {
        return toStringHelper("GetPermissionsResult")
                .addValue(allowedActions)
                .toString();
    }
}
