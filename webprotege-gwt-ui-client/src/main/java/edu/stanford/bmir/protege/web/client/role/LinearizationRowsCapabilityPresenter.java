package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.CapabilityId;
import edu.stanford.bmir.protege.web.shared.access.LinearizationRowsCapability;
import edu.stanford.bmir.protege.web.shared.linearization.GetLinearizationDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LinearizationRowsCapabilityPresenter implements CapabilityPresenter {
    private Optional<LinearizationRowsCapability> capability = Optional.empty();
    private final LinearizationRowsCapabilityView view;
    private final CapabilityContextPresenter capabilityContextPresenter;

    private final DispatchServiceManager dispatch;

    private List<LinearizationDefinition> definitionList = new ArrayList<>();


    @Inject
    public LinearizationRowsCapabilityPresenter(LinearizationRowsCapabilityView view,
                                                CapabilityContextPresenter capabilityContextPresenter, DispatchServiceManager dispatch) {
        this.view = view;
        this.capabilityContextPresenter = capabilityContextPresenter;
        this.dispatch = dispatch;
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
        if(definitionList.isEmpty()) {
            dispatch.execute(GetLinearizationDefinitionsAction.create(), response -> {
                this.definitionList = response.getDefinitionList();
                view.setLinearizationDefinitions(response.getDefinitionList());
                container.setWidget(view);
                capabilityContextPresenter.start(view.getContextCriteriaContainer());
            });
        } else {
            view.setLinearizationDefinitions(this.definitionList);
            container.setWidget(view);
            capabilityContextPresenter.start(view.getContextCriteriaContainer());
        }

    }
}
