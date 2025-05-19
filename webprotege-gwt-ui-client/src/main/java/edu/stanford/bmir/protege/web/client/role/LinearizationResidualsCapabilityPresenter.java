package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.access.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class LinearizationResidualsCapabilityPresenter implements CapabilityPresenter {
    private Optional<LinearizationResidualsCapability> capability = Optional.empty();
    private final LinearizationResidualsCapabilityView view;
    private final CapabilityContextPresenter capabilityContextPresenter;

    private final DispatchServiceManager dispatch;


    @Inject
    public LinearizationResidualsCapabilityPresenter(LinearizationResidualsCapabilityView view,
                                                     CapabilityContextPresenter capabilityContextPresenter,
                                                     DispatchServiceManager dispatch) {
        this.view = view;
        this.capabilityContextPresenter = capabilityContextPresenter;
        this.dispatch = dispatch;
    }


    @Nonnull
    @Override
    public String getTypeId() {
        return "LinearizationResidualsCapability";
    }

    @Override
    public void setCapability(Capability capability) {
        if (capability instanceof LinearizationResidualsCapability) {
            LinearizationResidualsCapability linearizationCapability = (LinearizationResidualsCapability) capability;
            this.capability = Optional.of(linearizationCapability);
            view.setCapabilityId(capability.getId().getId());
            capabilityContextPresenter.setCriteria(linearizationCapability.getContextCriteria());
        }
    }

    @Override
    public Optional<Capability> getCapability() {
        CompositeRootCriteria rootCriteria = capabilityContextPresenter.getCriteria().isPresent() ?
                capabilityContextPresenter.getCriteria().get() : CompositeRootCriteria.any();
        return Optional.of(LinearizationResidualsCapability.get(CapabilityId.valueOf(view.getCapabilityId()), rootCriteria));
    }

    @Override
    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        capabilityContextPresenter.start(view.getContextCriteriaContainer());
    }
}
