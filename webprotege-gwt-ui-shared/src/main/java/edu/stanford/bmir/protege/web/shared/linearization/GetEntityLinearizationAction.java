package edu.stanford.bmir.protege.web.shared.linearization;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.GetEntityLinearizations")
public abstract class GetEntityLinearizationAction implements Action<GetEntityLinearizationResult> {
    @JsonCreator
    public static GetEntityLinearizationAction create(@JsonProperty("entityIRI") IRI entityIRI,
                                                      @JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetEntityLinearizationAction(entityIRI, projectId);
    }

    public abstract IRI getEntityIri();

    public abstract ProjectId getProjectId();
}
