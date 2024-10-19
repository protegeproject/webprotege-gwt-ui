package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.SetNamedHierarchies")
public abstract class SetNamedHierarchiesAction implements ProjectAction<SetNamedHierarchiesResult> {

    @JsonCreator
    public static SetNamedHierarchiesAction get(@JsonProperty("projectId") ProjectId projectId,
                                                @JsonProperty("namedHierarchies") List<NamedHierarchy> namedHierarchies) {
        return new AutoValue_SetNamedHierarchiesAction(projectId, namedHierarchies);
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @Nonnull
    @JsonProperty("namedHierarchies")
    public abstract List<NamedHierarchy> getNamedHierarchies();
}
