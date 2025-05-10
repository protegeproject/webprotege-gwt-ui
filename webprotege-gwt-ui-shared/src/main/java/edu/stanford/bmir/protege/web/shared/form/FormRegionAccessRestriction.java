package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.access.CapabilityId;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormRegionAccessRestriction {

    @JsonCreator
    public static FormRegionAccessRestriction get(@JsonProperty("formRegionId") FormRegionId formRegionId,
                                                  @JsonProperty("roleId") RoleId roleId,
                                                  @JsonProperty("capabilityId") CapabilityId capabilityId,
                                                  @JsonProperty("criteria") CompositeRootCriteria criteria) {
        return new AutoValue_FormRegionAccessRestriction(formRegionId, roleId, capabilityId, criteria);
    }

    @JsonProperty("formRegionId")
    public abstract FormRegionId getFormRegionId();

    /**
     * The role that this access restriction applies to.
     */
    @JsonProperty("roleId")
    public abstract RoleId getRoleId();

    /**
     * The capability that is granted
     */
    @JsonProperty("capabilityId")
    public abstract CapabilityId getCapabilityId();

    /**
     * The context criteria that decides whether the capability is granted.
     */
    @JsonProperty("criteria")
    public abstract CompositeRootCriteria getContextCriteria();

}
