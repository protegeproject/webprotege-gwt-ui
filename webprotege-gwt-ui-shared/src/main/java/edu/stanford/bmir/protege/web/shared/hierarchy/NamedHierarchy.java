package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class NamedHierarchy {

    @JsonCreator
    public static NamedHierarchy get(@JsonProperty("hierarchyId") HierarchyId hierarchyId,
                                     @JsonProperty("label") LanguageMap label,
                                     @JsonProperty("description") LanguageMap description,
                                     @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor) {
        return new AutoValue_NamedHierarchy(hierarchyId, label, description, hierarchyDescriptor);
    }

    @Nonnull
    @JsonProperty("hierarchyId")
    public abstract HierarchyId getHierarchyId();

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
