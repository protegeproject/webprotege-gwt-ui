package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationSpecification;

import javax.inject.Inject;

public class LinearizationSpecificationRowPresenter extends Composite implements IsWidget {

    @UiField
    Grid grid;
    @UiField
    Label linearization;
    @UiField
    Label isPartOf;
    @UiField
    Label isGrouping;
    @UiField
    Label linearizationParent;
    @UiField
    Label sortingLabel;
    @UiField
    Label comments;

    public LinearizationSpecificationRowPresenter(LinearizationSpecification data) {
        initWidget(ourUiBinder.createAndBindUi(this));

        // Set the values for the labels based on the data object
        linearization.setText(data.getLinearizationView());
        isGrouping.setText(data.getIsGrouping());
        linearizationParent.setText(data.getLinearizationParent());
        comments.setText(data.getCodingNote());

    }


    interface UIBinder extends UiBinder<HTMLPanel, LinearizationSpecificationRowPresenter> {

    }

    private static LinearizationSpecificationRowPresenter.UIBinder ourUiBinder = GWT.create(LinearizationSpecificationRowPresenter.UIBinder.class);

    @Inject
    public LinearizationSpecificationRowPresenter() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

}
