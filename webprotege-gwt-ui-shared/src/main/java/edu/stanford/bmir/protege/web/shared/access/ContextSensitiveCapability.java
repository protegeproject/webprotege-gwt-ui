package edu.stanford.bmir.protege.web.shared.access;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;

public interface ContextSensitiveCapability extends Capability {

    @JsonProperty("contextCriteria")
    @Nonnull
    CompositeRootCriteria getContextCriteria();
}
