package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.entities.CreateObjectProperties")
public abstract class CreateObjectPropertiesAction implements CreateEntitiesInHierarchyAction<CreateObjectPropertiesResult, OWLObjectProperty> {

    @JsonCreator
    public static CreateObjectPropertiesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                        @JsonProperty("sourceText") @Nonnull String sourceText,
                                        @JsonProperty("langTag") @Nonnull String langTag,
                                        @JsonProperty("parents") @Nonnull ImmutableSet<OWLObjectProperty> parents) {
        return new AutoValue_CreateObjectPropertiesAction(projectId, sourceText, langTag, parents);
    }
}
