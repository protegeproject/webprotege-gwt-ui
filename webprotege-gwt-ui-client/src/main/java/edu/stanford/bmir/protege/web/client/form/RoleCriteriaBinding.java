package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.access.RoleId;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class RoleCriteriaBinding {

    public static RoleCriteriaBinding get(RoleId roleId, CompositeRootCriteria criteria) {
        return new AutoValue_RoleCriteriaBinding(roleId, criteria);
    }

    public static RoleCriteriaBinding get(RoleId roleId) {
        return get(roleId, CompositeRootCriteria.any());
    }

    public abstract RoleId getRoleId();

    @Nonnull
    public abstract CompositeRootCriteria getCriteria();
}
