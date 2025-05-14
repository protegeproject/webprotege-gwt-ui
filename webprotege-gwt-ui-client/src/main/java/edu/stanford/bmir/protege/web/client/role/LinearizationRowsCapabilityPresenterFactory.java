package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.LinearizationRowsCapability;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

public class LinearizationRowsCapabilityPresenterFactory implements CapabilityPresenterFactory {
    private final Provider<LinearizationRowsCapabilityPresenter> provider;
    @Inject
    public LinearizationRowsCapabilityPresenterFactory(Provider<LinearizationRowsCapabilityPresenter> provider) {
        this.provider = provider;
    }
    @Nonnull
    @Override
    public String getTypeId() {
        return "LinearizationRowsCapability";
    }

    @Override
    public boolean isPresenterFor(@Nonnull Capability capability) {
        return capability instanceof LinearizationRowsCapability;
    }

    @Override
    public String getLabel() {
        return "Linearization Rows Capability";
    }

    @Override
    public CapabilityPresenter createPresenter() {
        return provider.get();
    }
}
