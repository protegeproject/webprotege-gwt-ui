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
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.CreateDataProperties")
public abstract class CreateDataPropertiesResult implements CreateEntitiesInHierarchyResult<OWLDataProperty> {

    @JsonCreator
    public static CreateDataPropertiesResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                      @JsonProperty("entities") @Nonnull ImmutableSet<EntityNode> entities,
                                                    @JsonProperty("changeRequestId") ChangeRequestId changeRequestId) {
        return new AutoValue_CreateDataPropertiesResult(projectId, entities, changeRequestId);
    }
}
