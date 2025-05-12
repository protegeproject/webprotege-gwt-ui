package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.access.Capability;

import javax.annotation.Nonnull;
import java.util.Optional;


public interface CapabilityPresenter {

    @Nonnull
    String getTypeId();

    void setCapability(Capability capability);

    Optional<Capability> getCapability();

    void start(AcceptsOneWidget container);
}