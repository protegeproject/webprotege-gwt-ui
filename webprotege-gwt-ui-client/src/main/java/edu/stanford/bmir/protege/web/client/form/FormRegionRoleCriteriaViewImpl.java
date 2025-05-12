package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.inject.Inject;

public class FormRegionRoleCriteriaViewImpl extends Composite implements FormRegionRoleCriteriaView {

    interface FormRegionRoleCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, FormRegionRoleCriteriaViewImpl> {

    }

    private static FormRegionRoleCriteriaViewImplUiBinder ourUiBinder = GWT.create(FormRegionRoleCriteriaViewImplUiBinder.class);

    @UiField
    SimplePanel roleIdContainer;

    @UiField
    SimplePanel contextCriteriaContainer;

    @Inject
    public FormRegionRoleCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public AcceptsOneWidget getRoleIdContainer() {
        return roleIdContainer;
    }

    @Override
    public AcceptsOneWidget getContextCriteriaContainer() {
        return contextCriteriaContainer;
    }
}