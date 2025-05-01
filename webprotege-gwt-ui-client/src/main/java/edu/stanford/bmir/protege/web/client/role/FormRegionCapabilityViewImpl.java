package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.inject.Inject;

public class FormRegionCapabilityViewImpl extends Composite implements FormRegionCapabilityView {

    interface FormRegionCapabilityViewImplUiBinder extends UiBinder<HTMLPanel, FormRegionCapabilityViewImpl> {

    }

    private static FormRegionCapabilityViewImplUiBinder ourUiBinder = GWT.create(FormRegionCapabilityViewImplUiBinder.class);

    @UiField
    ListBox capabilityIdField;

    @UiField
    Label formRegionIdField;

    @UiField
    SimplePanel contextCriteriaContainer;


    @Inject
    public FormRegionCapabilityViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        capabilityIdField.addItem("ViewFormRegion");
        capabilityIdField.addItem("EditFormRegion");
    }

    @Override
    public void setCapabilityId(String capabilityId) {
        capabilityIdField.setSelectedIndex(0);
        for(int i = 0; i < capabilityIdField.getItemCount(); i++) {
            if(capabilityIdField.getValue(i).equals(capabilityId)) {
                capabilityIdField.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public void setFormRegionId(FormRegionId formRegionId) {
        formRegionIdField.setText(formRegionId.getId());
    }

    @Override
    public AcceptsOneWidget getContextCriteriaContainer() {
        return contextCriteriaContainer;
    }
}