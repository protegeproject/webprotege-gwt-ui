package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.inject.Inject;

public class FormRegionCapabilityViewImpl extends Composite implements FormRegionCapabilityView {

    interface FormRegionCapabilityViewImplUiBinder extends UiBinder<HTMLPanel, FormRegionCapabilityViewImpl> {

    }

    private static FormRegionCapabilityViewImplUiBinder ourUiBinder = GWT.create(FormRegionCapabilityViewImplUiBinder.class);

    @UiField
    Label capabilityIdField;

    @UiField
    Label formRegionIdField;

    @Inject
    public FormRegionCapabilityViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setCapabilityId(String capabilityId) {
        capabilityIdField.setText(capabilityId);
    }

    @Override
    public void setFormRegionId(FormRegionId formRegionId) {
        formRegionIdField.setText(formRegionId.getId());
    }
}