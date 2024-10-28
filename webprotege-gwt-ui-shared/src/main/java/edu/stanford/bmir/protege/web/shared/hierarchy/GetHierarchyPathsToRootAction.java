package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.GetHierarchyPathsToRoot")
public abstract class GetHierarchyPathsToRootAction extends AbstractHasProjectAction<GetHierarchyPathsToRootResult> {

    @JsonCreator
    public static GetHierarchyPathsToRootAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                       @JsonProperty("entity") @Nonnull OWLEntity entity,
                                                       @JsonProperty("hierarchyDescriptor") @Nonnull HierarchyDescriptor hierarchyDescriptor) {
        return new AutoValue_GetHierarchyPathsToRootAction(projectId, entity, hierarchyDescriptor);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract HierarchyDescriptor getHierarchyDescriptor();
}
