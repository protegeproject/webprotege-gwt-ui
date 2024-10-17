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

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("webprotege.hierarchies.AddNamedHierarchy")
public abstract class AddNamedHierarchyAction implements ProjectAction<AddNamedHierarchyResult> {

    @JsonCreator
    public static AddNamedHierarchyAction get(@JsonProperty("projectId") ProjectId projectId,
                                              @JsonProperty("label") LanguageMap label,
                                              @JsonProperty("description") LanguageMap description,
                                              @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor) {
        return new AutoValue_AddNamedHierarchyAction(projectId, label, description, hierarchyDescriptor);
    }

    @Nonnull
    @Override
    @JsonProperty("projectId")
    public abstract ProjectId getProjectId();

    @Nonnull
    @JsonProperty("label")
    public abstract LanguageMap getLabel();

    @Nonnull
    @JsonProperty("description")
    public abstract LanguageMap getDescription();

    @Nonnull
    @JsonProperty("hierarchyDescriptor")
    public abstract HierarchyDescriptor getHierarchyDescriptor();
}
