package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.DropType;
import edu.stanford.protege.gwt.graphtree.shared.Path;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.MoveHierarchyNodeIcd")
public abstract class MoveHierarchyNodeIcdAction implements ProjectAction<MoveHierarchyNodeIcdResult> {

    @JsonCreator
    public static MoveHierarchyNodeIcdAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("hierarchyDescriptor") @Nonnull HierarchyDescriptor hierarchyDescriptor,
                                                    @JsonProperty("fromNodePath") @Nonnull Path<EntityNode> fromNodePath,
                                                    @JsonProperty("toNodeParentPath") @Nonnull Path<EntityNode> toNodeParentPath,
                                                    @JsonProperty("dropType") @Nonnull DropType dropType,
                                                    @JsonProperty("commitMessage") String commitMessage,
                                                    @JsonProperty("changeRequestId")ChangeRequestId changeRequestId) {
        return new AutoValue_MoveHierarchyNodeIcdAction(projectId, hierarchyDescriptor, fromNodePath, toNodeParentPath, dropType, commitMessage);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract HierarchyDescriptor getHierarchyDescriptor();

    @Nonnull
    public abstract Path<EntityNode> getFromNodePath();

    @Nonnull
    public abstract Path<EntityNode> getToNodeParentPath();

    @Nonnull
    public abstract DropType getDropType();

    public abstract String getCommitMessage();

    public abstract ChangeRequestId getChangeRequestId();
}
