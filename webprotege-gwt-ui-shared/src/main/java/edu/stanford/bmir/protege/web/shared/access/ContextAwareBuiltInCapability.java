package edu.stanford.bmir.protege.web.shared.access;

import com.google.common.base.CaseFormat;


public enum ContextAwareBuiltInCapability {

    VIEW_LINEARIZATION_RESIDUALS,
    EDIT_LINEARIZATION_RESIDUALS,
    VIEW_POSTCOORDINATION_SCALE_VALUES,
    EDIT_POSTCOORDINATION_SCALE_VALUES,
    VIEW_LOGICAL_DEFINITIONS,
    EDIT_LOGICAL_DEFINITIONS;;


    private final Capability capability;

    ContextAwareBuiltInCapability() {
        this.capability = new BasicCapability(CapabilityId.valueOf(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name())));
    }

    public Capability getCapability() {
        return capability;
    }
}
