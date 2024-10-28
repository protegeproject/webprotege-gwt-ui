package edu.stanford.bmir.protege.web.shared.logicaldefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;

import static edu.stanford.bmir.protege.web.shared.logicaldefinition.UpdateLogicalDefinitionAction.CHANNEL;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(CHANNEL)
public abstract class UpdateLogicalDefinitionAction implements Action<UpdateLogicalDefinitionResult> {

    public static final String CHANNEL = "icatx.logicalDefinitions.UpdateLogicalDefinition";


    @JsonCreator
    public static UpdateLogicalDefinitionAction create(  @JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                                                  @JsonProperty("projectId") ProjectId projectId,
                                                                  @JsonProperty("subject") OWLClass subject,
                                                                  @JsonProperty("pristineLogicalConditions") LogicalConditions pristineLogicalConditions,
                                                                  @JsonProperty("changedLogicalConditions") LogicalConditions changedLogicalConditions,
                                                                  @JsonProperty("commitMessage") String commitMessage) {
        return new AutoValue_UpdateLogicalDefinitionAction(changeRequestId,
                projectId,
                subject,
                pristineLogicalConditions,
                changedLogicalConditions,
                commitMessage);

    }
    @JsonProperty("changeRequestId")
    public abstract ChangeRequestId getChangeRequestId();
    @JsonProperty("projectId")
    public abstract ProjectId projectId();
    @JsonProperty("subject")
    public abstract OWLClass subject();
    @JsonProperty("pristineLogicalConditions")
    public abstract LogicalConditions pristineLogicalConditions();
    @JsonProperty("changedLogicalConditions")
    public abstract LogicalConditions changedLogicalConditions();
    @JsonProperty("commitMessage")
    public abstract String commitMessage();

}
