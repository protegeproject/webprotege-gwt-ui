package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
@JsonTypeName("webprotege.hierarchies.GetHierarchyRoots")
public class GetHierarchyRootsAction implements ProjectAction<GetHierarchyRootsResult> {

    private ProjectId projectId;

    private HierarchyDescriptor hierarchyDescriptor;

    private GetHierarchyRootsAction(@Nonnull ProjectId projectId, @Nonnull HierarchyDescriptor hierarchyDescriptor) {
        this.projectId = checkNotNull(projectId);
        this.hierarchyDescriptor = checkNotNull(hierarchyDescriptor);
    }

    @GwtSerializationConstructor
    private GetHierarchyRootsAction() {
    }

    @JsonCreator
    public static GetHierarchyRootsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                 @JsonProperty("hierarchyDescriptor") @Nonnull HierarchyDescriptor hierarchyDescriptor) {
        return new GetHierarchyRootsAction(projectId, hierarchyDescriptor);
    }

    @JsonProperty("projectId")
    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @JsonProperty("hierarchyDescriptor")
    @Nonnull
    public HierarchyDescriptor getHierarchyDescriptor() {
        return hierarchyDescriptor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hierarchyDescriptor, projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetHierarchyRootsAction)) {
            return false;
        }
        GetHierarchyRootsAction other = (GetHierarchyRootsAction) obj;
        return this.hierarchyDescriptor.equals(other.hierarchyDescriptor)
                && this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetHierarchyRootsAction")
                          .addValue(projectId)
                          .addValue(hierarchyDescriptor)
                          .toString();
    }
}
