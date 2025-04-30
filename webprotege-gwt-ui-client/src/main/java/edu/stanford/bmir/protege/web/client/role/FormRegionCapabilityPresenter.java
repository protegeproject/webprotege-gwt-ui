package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.FormRegionCapability;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class FormRegionCapabilityPresenter implements CapabilityPresenter {

    private final FormRegionCapabilityView view;

    private Optional<Capability> capability = Optional.empty();

    @Inject
    public FormRegionCapabilityPresenter(FormRegionCapabilityView view) {
        this.view = view;
    }

    @Nonnull
    @Override
    public String getTypeId() {
        return "FormRegionCapability";
    }

    @Override
    public void setCapability(Capability capability) {
        this.capability = Optional.of(capability);
        if(capability instanceof FormRegionCapability) {
            FormRegionCapability formRegionCapability = (FormRegionCapability) capability;
            view.setCapabilityId(formRegionCapability.getId());
            view.setFormRegionId(formRegionCapability.getFormRegionId());
        }
    }

    @Override
    public Optional<Capability> getCapability() {
        return capability;
    }

    @Override
    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
    }
}
