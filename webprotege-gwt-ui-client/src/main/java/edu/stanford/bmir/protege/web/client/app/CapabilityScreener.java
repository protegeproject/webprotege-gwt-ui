package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.shared.access.BasicCapability;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/03/16
 */
public class CapabilityScreener {

    private final Provider<ForbiddenView> forbiddenViewProvider;

    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    public interface Callback {
        void onHasCapability();
    }

    @Inject
    public CapabilityScreener(Provider<ForbiddenView> forbiddenViewProvider,
                              LoggedInUserProjectCapabilityChecker capabilityChecker) {
        this.forbiddenViewProvider = checkNotNull(forbiddenViewProvider);
        this.capabilityChecker = checkNotNull(capabilityChecker);
    }

    public void checkCapability(@Nonnull final BasicCapability expectedCapability,
                                @Nonnull final AcceptsOneWidget acceptsOneWidget,
                                @Nonnull final Callback callback) {

        capabilityChecker.hasCapability(expectedCapability, hasCapability -> {
            if(hasCapability) {
                callback.onHasCapability();
            }
            else {
                acceptsOneWidget.setWidget(forbiddenViewProvider.get());
            }
        });
    }
}
