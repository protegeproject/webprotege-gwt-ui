package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Objects;

/**
 * Implementation of {@link LinearizationResidualsCapabilityView} that provides UI for managing
 * linearization residuals capabilities in WebProtege.
 */
public class LinearizationResidualsCapabilityViewImpl extends Composite implements LinearizationResidualsCapabilityView {

    private static final String VIEW_LINEARIZATION_RESIDUALS = "ViewLinearizationResiduals";
    private static final String EDIT_LINEARIZATION_RESIDUALS = "EditLinearizationResiduals";

    interface LinearizationResidualsCapabilityViewImplUiBinder extends UiBinder<HTMLPanel, LinearizationResidualsCapabilityViewImpl> {
    }

    @UiField
    SimplePanel contextCriteriaContainer;

    @UiField
    ListBox capabilityIdField;

    private static final LinearizationResidualsCapabilityViewImpl.LinearizationResidualsCapabilityViewImplUiBinder ourUiBinder =
            GWT.create(LinearizationResidualsCapabilityViewImpl.LinearizationResidualsCapabilityViewImplUiBinder.class);

    @Inject
    public LinearizationResidualsCapabilityViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        capabilityIdField.addItem(VIEW_LINEARIZATION_RESIDUALS);
        capabilityIdField.addItem(EDIT_LINEARIZATION_RESIDUALS);
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
