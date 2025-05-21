package edu.stanford.bmir.protege.web.shared.linearization;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;

import static edu.stanford.bmir.protege.web.shared.linearization.GetContextAwareLinearizationDefinitionAction.CHANNEL;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class GetContextAwareLinearizationDefinitionAction implements Action<GetContextAwareLinearizationDefinitionResult> {
    public static final String CHANNEL = "webprotege.linearization.GetContextAwareLinearizationDefinitions";

    @JsonCreator
    public static GetContextAwareLinearizationDefinitionAction create(
            @JsonProperty("entityIRI") IRI entityIRI,
            @JsonProperty("allowedIds") List<String> allowedIds,
            @JsonProperty("projectId") ProjectId projectId
    ) {
        return new AutoValue_GetContextAwareLinearizationDefinitionAction(entityIRI,allowedIds, projectId);
    }

    @JsonProperty("entityIRI")
    public abstract IRI getEntityIri();

    @JsonProperty("allowedIds")
    public abstract List<String> getAllowedIds();

    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();


}





