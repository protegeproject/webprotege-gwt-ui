package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import java.util.List;

public class ScaleValueCardViewImpl implements ScaleValueCardView {

    interface ScaleValueCardViewImplUiBinder extends UiBinder<HTMLPanel, ScaleValueCardViewImpl> {
    }

    private static ScaleValueCardViewImplUiBinder uiBinder = GWT.create(ScaleValueCardViewImplUiBinder.class);

    @UiField
    Label titleLabel;

    @UiField
    FlexTable valueTable;

    @UiField
    Button addButton;

    private final String postCoordinationAxis;
    private final List<String> scaleValues;

    public ScaleValueCardViewImpl(String postCoordinationAxis, List<String> scaleValues) {
        this.postCoordinationAxis = postCoordinationAxis;
        this.scaleValues = scaleValues;
        uiBinder.createAndBindUi(this);
        initTable();
    }

    private void initTable() {
        valueTable.setText(0, 0, "Value");
        valueTable.setText(0, 1, "");
        valueTable.setText(0, 2, "");

        for (int i = 0; i < scaleValues.size(); i++) {
            String value = scaleValues.get(i);
            int rowIndex = i + 1;

            valueTable.setText(rowIndex, 0, value);

            Button deleteButton = new Button("Delete");
            Button actionButton = new Button("Action");

            valueTable.setWidget(rowIndex, 1, deleteButton);
            valueTable.setWidget(rowIndex, 2, actionButton);

            // Add event handlers for buttons if necessary
            deleteButton.addClickHandler(event -> {
                valueTable.removeRow(rowIndex);
                scaleValues.remove(rowIndex - 1);
            });
        }
    }

    @Override
    public Object getScaleValue() {
        // Implement logic to return the scale value if needed
        return null;
    }

    @Override
    public Widget asWidget() {
        titleLabel.setText(postCoordinationAxis);
        return uiBinder.createAndBindUi(this);
    }
}
