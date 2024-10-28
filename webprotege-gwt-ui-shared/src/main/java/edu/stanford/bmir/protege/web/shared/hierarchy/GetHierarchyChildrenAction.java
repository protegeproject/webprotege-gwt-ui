package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
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
@JsonTypeName("webprotege.hierarchies.GetHierarchyChildren")
public abstract class GetHierarchyChildrenAction extends AbstractHasProjectAction<GetHierarchyChildrenResult> {

    @JsonCreator
    public static GetHierarchyChildrenAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("entity") @Nonnull OWLEntity entity,
                                                    @JsonProperty("hierarchyDescriptor") @Nonnull HierarchyDescriptor hierarchyDescriptor,
                                                    @JsonProperty("pageRequest") @Nonnull PageRequest pageRequest) {
        return new AutoValue_GetHierarchyChildrenAction(projectId, entity, hierarchyDescriptor, pageRequest);
    }

    public static GetHierarchyChildrenAction create(@Nonnull ProjectId projectId,
                                                    @Nonnull OWLEntity entity,
                                                    @Nonnull HierarchyDescriptor hierarchyDescriptor) {
        return create(projectId, entity, hierarchyDescriptor, PageRequest.requestFirstPage());
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLEntity getEntity();

    public abstract HierarchyDescriptor getHierarchyDescriptor();

    @Nonnull
    public abstract PageRequest getPageRequest();
}
