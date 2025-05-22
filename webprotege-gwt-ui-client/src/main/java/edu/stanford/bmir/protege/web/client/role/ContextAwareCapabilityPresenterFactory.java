package edu.stanford.bmir.protege.web.client.role;

import edu.stanford.bmir.protege.web.shared.access.*;

import javax.annotation.Nonnull;
import javax.inject.*;

public class ContextAwareCapabilityPresenterFactory implements CapabilityPresenterFactory {
    private final Provider<ContextAwareCapabilityPresenter> provider;
    @Inject
    public ContextAwareCapabilityPresenterFactory(Provider<ContextAwareCapabilityPresenter> provider) {
        this.provider = provider;
    }
    @Nonnull
    @Override
    public String getTypeId() {
        return "ContextAwareCapability";
    }

    @Override
    public boolean isPresenterFor(@Nonnull Capability capability) {
        return capability instanceof ContextAwareCapability;
    }

    @Override
    public String getLabel() {
        return "Context Aware Capability";
    }

    @Override
    public CapabilityPresenter createPresenter() {
        return provider.get();
    }
}
