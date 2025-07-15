package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.FormRegionCapability;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

public class FormRegionCapabilityPresenterFactory implements CapabilityPresenterFactory {

    private final Provider<FormRegionCapabilityPresenter> provider;

    @Inject
    public FormRegionCapabilityPresenterFactory(Provider<FormRegionCapabilityPresenter> provider) {
        this.provider = provider;
    }

    @Nonnull
    @Override
    public String getTypeId() {
        return "FormRegionCapability";
    }

    @Override
    public boolean isPresenterFor(@Nonnull Capability capability) {
        return capability instanceof FormRegionCapability;
    }

    @Override
    public String getLabel() {
        return "Form Region Capability";
    }

    @Override
    public CapabilityPresenter createPresenter() {
        return provider.get();
    }
}
