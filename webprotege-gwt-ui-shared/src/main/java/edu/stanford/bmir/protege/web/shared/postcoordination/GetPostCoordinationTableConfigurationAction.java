package edu.stanford.bmir.protege.web.shared.postcoordination;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.postcoordination.GetTablePostCoordinationAxis")
public abstract class GetPostCoordinationTableConfigurationAction implements Action<GetPostCoordinationTableConfigurationResult> {

    public static GetPostCoordinationTableConfigurationAction create(IRI entityIri, ProjectId projectId){
        return new AutoValue_GetPostCoordinationTableConfigurationAction(entityIri, projectId);
    }

    public abstract IRI getEntityIri();

    public abstract ProjectId getProjectId();
}
