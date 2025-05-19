package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.access.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

/**
 * Implementation of {@link ContextAwareCapabilityView} that provides UI for managing
 * linearization residuals capabilities in WebProtege.
 */
public class ContextAwareCapabilityViewImpl extends Composite implements ContextAwareCapabilityView {

    interface ContextAwareCapabilityViewImplUiBinder extends UiBinder<HTMLPanel, ContextAwareCapabilityViewImpl> {
    }

    @UiField
    SimplePanel contextCriteriaContainer;

    @UiField
    ListBox capabilityIdField;

    private static final ContextAwareCapabilityViewImplUiBinder ourUiBinder =
            GWT.create(ContextAwareCapabilityViewImplUiBinder.class);

    @Inject
    public ContextAwareCapabilityViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        Arrays.stream(ContextAwareBuiltInCapability.values())
                .map(ContextAwareBuiltInCapability::getCapability)
                .map(Capability::getId)
                .sorted()
                .map(CapabilityId::getId)
                .forEach(cap -> capabilityIdField.addItem(cap));
    }

    @Override
    public void setCapabilityId(@Nonnull String capabilityId) {
        Objects.requireNonNull(capabilityId, "capabilityId must not be null");

        boolean found = false;
        for (int i = 0; i < capabilityIdField.getItemCount(); i++) {
            if (capabilityIdField.getValue(i).equals(capabilityId)) {
                capabilityIdField.setSelectedIndex(i);
                found = true;
                break;
            }
        }

        if (!found) {
            capabilityIdField.setSelectedIndex(0);
        }
    }

    @Override
    public String getCapabilityId() {
        return capabilityIdField.getSelectedValue();
    }

    @Override
    public AcceptsOneWidget getContextCriteriaContainer() {
        return contextCriteriaContainer;
    }
}
