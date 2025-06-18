package edu.stanford.bmir.protege.web.shared.icd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.GetLogicalDefinitionsClassAncestors")
public abstract class GetLogicalDefinitionsClassAncestorsAction implements Action<GetLogicalDefinitionsClassAncestorsResult> {

    private IRI classIri;

    private ProjectId projectId;
    @JsonCreator
    public static GetLogicalDefinitionsClassAncestorsAction create(IRI classIri, ProjectId projectId) {
        return new AutoValue_GetLogicalDefinitionsClassAncestorsAction(classIri, projectId);
    }

    public abstract IRI getClassIri();
    public abstract ProjectId getProjectId();
}
