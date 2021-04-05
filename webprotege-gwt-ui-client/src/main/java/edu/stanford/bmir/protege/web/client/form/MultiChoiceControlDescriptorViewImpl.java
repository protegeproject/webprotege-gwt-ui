package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-10
 */
public class MultiChoiceControlDescriptorViewImpl extends Composite implements MultiChoiceControlDescriptorView {

    interface MultiChoiceControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, MultiChoiceControlDescriptorViewImpl> {

    }


    private static MultiChoiceControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            MultiChoiceControlDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel sourceContainer;

    @Inject
    public MultiChoiceControlDescriptorViewImpl(Provider<ChoiceDescriptorPresenter> choiceDescriptorPresenterProvider) {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSourceContainer() {
        return sourceContainer;
    }

}
