package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.users.GetAuthenticatedUserDetails")
public abstract class GetAuthenticatedUserDetailsResult implements Result {

    public abstract UserDetails getUserDetails();

    public abstract ImmutableSet<ActionId> getPermittedActions();

    @JsonCreator
    public static GetAuthenticatedUserDetailsResult create(@JsonProperty("userDetails") UserDetails userDetails,
                                                           @JsonProperty("permittedActions")ImmutableSet<ActionId> permittedActions) {
        return new AutoValue_GetAuthenticatedUserDetailsResult(userDetails, permittedActions);
    }

}
