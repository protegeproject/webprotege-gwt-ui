package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.users.GetAuthenticatedUserDetails")
public abstract class GetAuthenticatedUserDetailsAction implements Action<GetAuthenticatedUserDetailsResult> {

    @JsonCreator
    public static GetAuthenticatedUserDetailsAction create() {
        return new AutoValue_GetAuthenticatedUserDetailsAction();
    }
}
