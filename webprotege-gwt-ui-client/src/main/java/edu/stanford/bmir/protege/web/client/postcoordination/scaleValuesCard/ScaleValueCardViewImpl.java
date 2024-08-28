package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.button.DeleteButton;
import edu.stanford.bmir.protege.web.client.postcoordination.PostCoordinationTableResourceBundle;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

public class ScaleValueCardViewImpl implements ScaleValueCardView {

    interface ScaleValueCardViewImplUiBinder extends UiBinder<HTMLPanel, ScaleValueCardViewImpl> {
    }

    private static final ScaleValueCardViewImplUiBinder uiBinder = GWT.create(ScaleValueCardViewImplUiBinder.class);
    private final HTMLPanel rootPanel;
    private static final PostCoordinationTableResourceBundle.PostCoordinationTableCss postCoordinationStyle = PostCoordinationTableResourceBundle.INSTANCE.style();

    private static final WebProtegeClientBundle.ButtonsCss buttonCss = WebProtegeClientBundle.BUNDLE.buttons();

    private DeleteScaleValueButtonHandler deleteScaleValueButtonHandler = (value) -> {
    };

    @UiField
    FlexTable valueTable;

    private Button addButton;

    public ScaleValueCardViewImpl() {
        rootPanel = uiBinder.createAndBindUi(this);
        createAddButton();
    }

    private void createAddButton() {
        addButton = new Button();
        addButton.setStyleName(buttonCss.addButton());
        addButton.setTitle("Select value");
    }

    @Override
    public void setAddButtonClickHandler(ClickHandler clickHandler) {
        addButton.addClickHandler(clickHandler);
    }

    @Override
    public void clearTable() {
        valueTable.removeAllRows();
    }

    @Override
    public void addHeader(String headerText, String description) {
        GWT.log("Adding header. Current row count: " + valueTable.getRowCount());

        // Combine header text and description using HTML with inline span for description
        HTML headerHtml = new HTML(headerText + "<br><span class=\"" + postCoordinationStyle.scaleValueHeaderDescription() + "\">" + description + "</span>");

        // Add the combined header HTML to the table cell and apply the scaleValueHeader style to the cell
        valueTable.setWidget(0, 0, headerHtml);
        valueTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        valueTable.getCellFormatter().setStyleName(0, 0, postCoordinationStyle.scaleValueHeader());

        GWT.log("Header added. Current row count: " + valueTable.getRowCount());
    }

    @Override
    public void addRow(String value) {
        int addButtonRowIndex = valueTable.getRowCount();

        valueTable.insertRow(addButtonRowIndex - 1);
        setRowContents(addButtonRowIndex - 1, value);
    }


    private void setRowContents(int rowIndex, String value) {
        valueTable.setWidget(rowIndex, 0, new Label(value));

        Button deleteButton = new DeleteButton();

        valueTable.setWidget(rowIndex, 1, deleteButton);

        valueTable.getCellFormatter().setStyleName(rowIndex, 0, postCoordinationStyle.scaleValueTableValueCell());
        valueTable.getCellFormatter().setStyleName(rowIndex, 1, postCoordinationStyle.scaleValueTableButtonCell());

        valueTable.getRowFormatter().addStyleName(rowIndex, postCoordinationStyle.scaleValueRow());

        deleteButton.addClickHandler(event -> {
            deleteScaleValueButtonHandler.handleDeleteButton(value);
            int row = valueTable.getCellForEvent(event).getRowIndex();
            GWT.log("Deleting row at index: " + row);
            valueTable.removeRow(row);
            GWT.log("Row deleted. Current row count: " + valueTable.getRowCount());
        });

        GWT.log("New row added. Current row count: " + valueTable.getRowCount());
    }

    public void setDeleteValueButtonHandler(DeleteScaleValueButtonHandler handler) {
        this.deleteScaleValueButtonHandler = handler;
    }

    @Override
    public void addSelectValueButton() {
        int lastRow = valueTable.getRowCount();
        valueTable.setWidget(lastRow, 0, addButton);
        valueTable.getFlexCellFormatter().setColSpan(lastRow, 0, 2);
        valueTable.getFlexCellFormatter().addStyleName(lastRow, 0, postCoordinationStyle.scaleValueTableValueCell());
        valueTable.getRowFormatter().addStyleName(lastRow, postCoordinationStyle.scaleValueRow());
        GWT.log("Select value button added. Row count: " + valueTable.getRowCount());
    }

    @Override
    public Widget asWidget() {
        return rootPanel;
    }
}
