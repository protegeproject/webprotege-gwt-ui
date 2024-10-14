package edu.stanford.bmir.protege.web.shared.logicaldefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import static edu.stanford.bmir.protege.web.shared.logicaldefinition.GetEntityLogicalDefinitionAction.CHANNEL;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class GetEntityLogicalDefinitionAction implements Action<GetEntityLogicalDefinitionResult> {

    public static final String CHANNEL = "icatx.logicalDefinitions.LogicalDefinition";



    @JsonCreator
    public static GetEntityLogicalDefinitionAction create(@JsonProperty("projectId") ProjectId projectId,
                                                              @JsonProperty("subject") OWLClass subject) {
        return new AutoValue_GetEntityLogicalDefinitionAction(projectId, subject);
    }

    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @JsonProperty("subject")
    public abstract OWLClass getSubject();


}
