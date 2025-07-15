package edu.stanford.bmir.protege.web.client.hierarchy;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.ViewId;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class HierarchyDescriptorRule {

    @JsonCreator
    public static HierarchyDescriptorRule get(@Nullable @JsonProperty("requiredPerspectiveId") PerspectiveId requiredPerspectiveId,
                                              @Nullable @JsonProperty("requiredViewId") ViewId requiredViewId,
                                              @Nonnull @JsonProperty("requiredViewProperties") Map<String, String> requiredViewProperties,
                                              @Nullable @JsonProperty("requiredFormId") FormId requiredFormId,
                                              @Nullable @JsonProperty("requiredFormFieldId") FormRegionId requiredFormFieldId,
                                              @Nonnull @JsonProperty("requiredCapabilities") Set<Capability> requiredCapabilities,
                                              @Nonnull @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor) {
        return new AutoValue_HierarchyDescriptorRule(requiredPerspectiveId, requiredViewId, requiredViewProperties, requiredFormId, requiredFormFieldId, requiredCapabilities, hierarchyDescriptor);
    }

    public static HierarchyDescriptorRule empty() {
        return get(null, null, Collections.emptyMap(), null, null, Collections.emptySet(), ClassHierarchyDescriptor.get());
    }

    @Nullable
    @JsonProperty("requiredPerspectiveId")
    public abstract PerspectiveId getRequiredPerspectiveId();

    @Nullable
    @JsonProperty("requiredViewId")
    public abstract ViewId getRequiredViewId();

    @Nonnull
    @JsonProperty("requiredViewProperties")
    public abstract Map<String, String> getRequiredViewProperties();

    @Nullable
    @JsonProperty("requiredFormId")
    public abstract FormId getRequiredFormId();

    @Nullable
    @JsonProperty("requiredFormFieldId")
    public abstract FormRegionId getRequiredFormFieldId();

    @Nonnull
    @JsonProperty("requiredCapabilities")
    public abstract Set<Capability> getRequiredCapabilities();

    @Nonnull
    @JsonProperty("hierarchyDescriptor")
    public abstract HierarchyDescriptor getHierarchyDescriptor();
}
