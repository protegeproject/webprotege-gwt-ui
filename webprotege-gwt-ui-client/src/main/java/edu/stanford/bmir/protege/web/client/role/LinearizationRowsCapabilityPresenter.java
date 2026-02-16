package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.linearization.LinearizationDefinitionsCache;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.CapabilityId;
import edu.stanford.bmir.protege.web.shared.access.LinearizationRowsCapability;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class LinearizationRowsCapabilityPresenter implements CapabilityPresenter {
    private Optional<LinearizationRowsCapability> capability = Optional.empty();
    private final LinearizationRowsCapabilityView view;
    private final CapabilityContextPresenter capabilityContextPresenter;
    private final LinearizationDefinitionsCache definitionsCache;

    @Inject
    public LinearizationRowsCapabilityPresenter(LinearizationRowsCapabilityView view,
                                                CapabilityContextPresenter capabilityContextPresenter,
                                                LinearizationDefinitionsCache definitionsCache) {
        this.view = view;
        this.capabilityContextPresenter = capabilityContextPresenter;
        this.definitionsCache = definitionsCache;
    }


    @Nonnull
    @Override
    public String getTypeId() {
        return "LinearizationRowCapability";
    }

    @Override
    public void setCapability(Capability capability) {
        if(capability instanceof LinearizationRowsCapability) {
            LinearizationRowsCapability linearizationCapability = (LinearizationRowsCapability) capability;
            this.capability = Optional.of(linearizationCapability);
            view.setCapabilityId(capability.getId().getId());
            view.setLinearizationIds(linearizationCapability.getLinearizationIds());
            capabilityContextPresenter.setCriteria(linearizationCapability.getContextCriteria());
        }
    }

    @Override
    public Optional<Capability> getCapability() {
        CompositeRootCriteria rootCriteria = capabilityContextPresenter.getCriteria().isPresent() ?
                capabilityContextPresenter.getCriteria().get() :  CompositeRootCriteria.any();
        return Optional.of(LinearizationRowsCapability.get(CapabilityId.valueOf(view.getCapabilityId()), view.getLinearizationIds(), rootCriteria));
    }

    @Override
    public void start(AcceptsOneWidget container) {
        definitionsCache.load(definitions -> {
            view.setLinearizationDefinitions(definitions);
            container.setWidget(view);
            capabilityContextPresenter.start(view.getContextCriteriaContainer());
        });
    }
}
