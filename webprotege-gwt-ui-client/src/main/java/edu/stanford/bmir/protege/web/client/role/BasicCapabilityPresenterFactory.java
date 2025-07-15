package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.shared.access.BasicCapability;
import edu.stanford.bmir.protege.web.shared.access.Capability;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

public class BasicCapabilityPresenterFactory implements CapabilityPresenterFactory {

    private final Provider<BasicCapabilityPresenter> provider;

    @Inject
    public BasicCapabilityPresenterFactory(Provider<BasicCapabilityPresenter> provider) {
        this.provider = provider;
    }

    @Override
    public boolean isPresenterFor(@Nonnull Capability capability) {
        return capability instanceof BasicCapability;
    }

    @Nonnull
    @Override
    public String getTypeId() {
        return BasicCapabilityPresenter.typeId();
    }

    @Override
    public String getLabel() {
        return "Basic Capability";
    }

    @Override
    public CapabilityPresenter createPresenter() {
        return provider.get();
    }
}
