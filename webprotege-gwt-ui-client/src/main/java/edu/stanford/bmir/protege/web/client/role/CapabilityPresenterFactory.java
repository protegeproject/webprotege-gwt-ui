package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.shared.access.Capability;

import javax.annotation.Nonnull;

public interface CapabilityPresenterFactory {

    @Nonnull
    String getTypeId();

    boolean isPresenterFor(@Nonnull Capability capability);

    String getLabel();

    CapabilityPresenter createPresenter();
}
