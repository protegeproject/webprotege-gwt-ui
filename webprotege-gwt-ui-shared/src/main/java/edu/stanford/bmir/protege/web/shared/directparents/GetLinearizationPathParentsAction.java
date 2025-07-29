package edu.stanford.bmir.protege.web.shared.directparents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.linearization.GetParentsThatAreLinearizationPathParents")
public abstract class GetLinearizationPathParentsAction extends AbstractHasProjectAction<GetLinearizationPathParentsResult> {

    @JsonCreator
    public static GetLinearizationPathParentsAction create(@JsonProperty("currentEntityIri")
                                                           IRI currentEntityIri,
                                                           @JsonProperty("parentEntityIris")
                                                           Set<IRI> parentEntityIris,
                                                           @JsonProperty("projectId") ProjectId projectId) {
        return new AutoValue_GetLinearizationPathParentsAction(currentEntityIri, parentEntityIris, projectId);
    }

    public abstract IRI getCurrentEntityIri();

    public abstract Set<IRI> getParentEntityIris();

    public abstract ProjectId getProjectId();

}
