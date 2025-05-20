package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.IRI;

import static edu.stanford.bmir.protege.web.shared.permissions.GetAuthorizedCapabilitiesForEntityAction.CHANNEL;

@JsonTypeName(CHANNEL)
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetAuthorizedCapabilitiesForEntityAction implements ProjectAction<GetAuthorizedCapabilitiesForEntityResult> {


    public static final String CHANNEL = "webprotege.authorization.GetAuthorizedCapabilitiesForEntity";

    @JsonCreator
    public static GetAuthorizedCapabilitiesForEntityAction create(@JsonProperty("projectId") ProjectId projectId,
                                                                  @JsonProperty("userId") UserId userId,
                                                                  @JsonProperty("entityIri") IRI entityIri) {
        return new AutoValue_GetAuthorizedCapabilitiesForEntityAction(projectId, userId, entityIri);
    }

    @JsonProperty("userId")
    public abstract UserId getUserId();

    @JsonProperty("entityIri")
    public abstract IRI getEntityIri();
}
