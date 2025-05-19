package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.shared.access.*;

import javax.annotation.Nonnull;
import javax.inject.*;

public class LinearizationResidualsCapabilityPresenterFactory implements CapabilityPresenterFactory {
    private final Provider<LinearizationResidualsCapabilityPresenter> provider;
    @Inject
    public LinearizationResidualsCapabilityPresenterFactory(Provider<LinearizationResidualsCapabilityPresenter> provider) {
        this.provider = provider;
    }
    @Nonnull
    @Override
    public String getTypeId() {
        return "LinearizationResidualsCapability";
    }

    @Override
    public boolean isPresenterFor(@Nonnull Capability capability) {
        return capability instanceof LinearizationResidualsCapability;
    }

    @Override
    public String getLabel() {
        return "Linearization Residuals Capability";
    }

    @Override
    public CapabilityPresenter createPresenter() {
        return provider.get();
    }
}
