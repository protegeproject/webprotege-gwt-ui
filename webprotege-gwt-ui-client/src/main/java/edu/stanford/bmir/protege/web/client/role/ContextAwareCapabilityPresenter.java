package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.access.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class ContextAwareCapabilityPresenter implements CapabilityPresenter {
    private Optional<ContextAwareCapability> capability = Optional.empty();
    private final ContextAwareCapabilityView view;
    private final CapabilityContextPresenter capabilityContextPresenter;

    private final DispatchServiceManager dispatch;


    @Inject
    public ContextAwareCapabilityPresenter(ContextAwareCapabilityView view,
                                           CapabilityContextPresenter capabilityContextPresenter,
                                           DispatchServiceManager dispatch) {
        this.view = view;
        this.capabilityContextPresenter = capabilityContextPresenter;
        this.dispatch = dispatch;
    }


    @Nonnull
    @Override
    public String getTypeId() {
        return "ContextAwareCapability";
    }

    @Override
    public void setCapability(Capability capability) {
        if (capability instanceof ContextAwareCapability) {
            ContextAwareCapability contextAwareCapability = (ContextAwareCapability) capability;
            this.capability = Optional.of(contextAwareCapability);
            view.setCapabilityId(capability.getId().getId());
            capabilityContextPresenter.setCriteria(contextAwareCapability.getContextCriteria());
        }
    }

    @Override
    public Optional<Capability> getCapability() {
        CompositeRootCriteria rootCriteria = capabilityContextPresenter.getCriteria().isPresent() ?
                capabilityContextPresenter.getCriteria().get() : CompositeRootCriteria.any();
        return Optional.of(ContextAwareCapability.get(CapabilityId.valueOf(view.getCapabilityId()), rootCriteria));
    }

    @Override
    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        capabilityContextPresenter.start(view.getContextCriteriaContainer());
    }
}
