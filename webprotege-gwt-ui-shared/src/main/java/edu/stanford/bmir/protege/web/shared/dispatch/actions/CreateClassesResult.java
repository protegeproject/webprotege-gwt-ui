package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.CreateClasses")
public abstract class CreateClassesResult implements CreateEntitiesInHierarchyResult<OWLClass> {

    @JsonCreator
    public static CreateClassesResult create(@JsonProperty("projectId") ProjectId projectId,
                               @JsonProperty("entities") ImmutableSet<EntityNode> classes,
                                             @JsonProperty("changeRequestId") ChangeRequestId changeRequestId) {
        return new AutoValue_CreateClassesResult(projectId, classes, changeRequestId);
    }
}
