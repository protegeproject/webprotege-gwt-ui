package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.postcoordination.PostCoordinationTableResourceBundle;

import java.util.*;

public class ScaleValueCardViewImpl implements ScaleValueCardView {

    interface ScaleValueCardViewImplUiBinder extends UiBinder<HTMLPanel, ScaleValueCardViewImpl> {
    }

    private final static ScaleValueCardViewImplUiBinder uiBinder = GWT.create(ScaleValueCardViewImplUiBinder.class);
    private final HTMLPanel rootPanel;
    private static PostCoordinationTableResourceBundle.PostCoordinationTableCss postCoordinationStyle = PostCoordinationTableResourceBundle.INSTANCE.style();


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

        rootPanel = uiBinder.createAndBindUi(this);
        titleLabel.setText(postCoordinationAxis);
        initTable();
    }

    private void initTable() {
        addHeaderCell("Value",0);
        addHeaderCell("",1);
        addHeaderCell("",2);

        for (int i = 0; i < scaleValues.size(); i++) {
            String value = scaleValues.get(i);
            int rowIndex = i + 1;

            valueTable.setWidget(rowIndex, 0, new Label(value));

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
        return rootPanel;
    }

    private void addHeaderCell(String label, int position) {
        Widget headerCell = new Label(label);
        valueTable.setWidget(0, position, headerCell);
        valueTable.getCellFormatter().addStyleName(0, position, postCoordinationStyle.getPostCoordinationHeader());
    }
}
