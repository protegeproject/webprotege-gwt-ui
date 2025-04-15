package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormRegionAccessRestrictions {

    @JsonCreator
    public static FormRegionAccessRestrictions get(@JsonProperty("formRegionId") FormRegionId formRegionId,
                                                   @JsonProperty("capabilityRoles") SetMultimap<String, RoleId> capabilityRoles) {
        return new AutoValue_FormRegionAccessRestrictions(formRegionId, ImmutableSetMultimap.copyOf(capabilityRoles));
    }

    @JsonProperty("formRegionId")
    public abstract FormRegionId getFormRegionId();

    @JsonProperty("capabilityRoles")
    public abstract SetMultimap<String, RoleId> getCapabilityRoles();
}
