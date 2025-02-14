package edu.stanford.bmir.protege.web.shared.postcoordination;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.postcoordination.GetEntityScaleValues")
public abstract class GetEntityCustomScalesAction implements Action<GetEntityCustomScalesResult> {
    @JsonProperty("entityIRI")
    public abstract String getEntityIri();

    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    public static GetEntityCustomScalesAction create(String entityIri, ProjectId projectId) {
        return new AutoValue_GetEntityCustomScalesAction(entityIri, projectId);
    }
}
