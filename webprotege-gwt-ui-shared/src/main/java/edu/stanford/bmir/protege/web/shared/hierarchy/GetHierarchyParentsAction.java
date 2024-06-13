package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetHierarchyParents")
public abstract class GetHierarchyParentsAction extends AbstractHasProjectAction<GetHierarchyParentsResult> {

    @JsonCreator
    public static GetHierarchyParentsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                   @JsonProperty("entity") @Nonnull OWLEntity entity,
                                                   @JsonProperty("hierarchyId") @Nonnull HierarchyId hierarchyId) {
        return new AutoValue_GetHierarchyParentsAction(projectId, entity, hierarchyId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLEntity getEntity();

    public abstract HierarchyId getHierarchyId();
}
