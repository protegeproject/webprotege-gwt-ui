package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.access.BasicCapability;
import edu.stanford.bmir.protege.web.shared.access.BuiltInCapability;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.access.CapabilityId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicCapabilityPresenter implements CapabilityPresenter {

    private static final String TYPE_ID = "BasicCapability";

    private final BasicCapabilityView view;

    @Inject
    public BasicCapabilityPresenter(BasicCapabilityView view) {
        this.view = view;
    }

    @Nonnull
    public static String typeId() {
        return TYPE_ID;
    }

    @Nonnull
    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    @Override
    public void setCapability(Capability capability) {
        if(capability instanceof BasicCapability) {
            view.setId(capability.getId().getId());
        }
    }

    @Override
    public Optional<Capability> getCapability() {
        String id = view.getId();
        if(id.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new BasicCapability(CapabilityId.valueOf(id)));
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        List<String> availableCapabilities = Arrays.stream(BuiltInCapability.values())
                .map(BuiltInCapability::getCapability)
                .map(BasicCapability::getId)
                .sorted()
                .map(CapabilityId::getId)
                .collect(Collectors.toList());
        view.setAvailableIds(availableCapabilities);
    }
}
