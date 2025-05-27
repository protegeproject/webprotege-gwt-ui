package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.card.*;
import edu.stanford.bmir.protege.web.client.library.button.DeleteButton;
import edu.stanford.bmir.protege.web.client.postcoordination.PostCoordinationTableResourceBundle;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.postcoordination.ScaleValueIriAndName;

public class ScaleValueCardViewImpl implements ScaleValueCardView {

    interface ScaleValueCardViewImplUiBinder extends UiBinder<HTMLPanel, ScaleValueCardViewImpl> {
    }

    private static final ScaleValueCardViewImplUiBinder uiBinder = GWT.create(ScaleValueCardViewImplUiBinder.class);
    private final HTMLPanel rootPanel;
    private HTML headerHtml;
    private boolean isCollapsed = false;
    private final String collapseIcon = "&#9660;";
    private final String expandIcon = "&#9654;";
    private final String spaceSymbol = "&nbsp;";
    private static final PostCoordinationTableResourceBundle.PostCoordinationTableCss postCoordinationStyle = PostCoordinationTableResourceBundle.INSTANCE.style();

    private static final WebProtegeClientBundle.ButtonsCss buttonCss = WebProtegeClientBundle.BUNDLE.buttons();

    private DeleteScaleValueButtonHandler deleteScaleValueButtonHandler = (value) -> {
    };

    @UiField
    FlexTable valueTable;

    private Button addButton;

    private boolean isReadOnly = true;

    private final EditableIcon editableIconHeader;

    public ScaleValueCardViewImpl() {
        editableIconHeader = new EditableIconImpl();
        editableIconHeader.setVisible(false);
        editableIconHeader.addStyleName(postCoordinationStyle.editableIconBackground());

        rootPanel = uiBinder.createAndBindUi(this);
        createAddButton();
        setReadOnly(isReadOnly);
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
    public void addHeader(String headerText, ScaleAllowMultiValue scaleAllowMultiValue) {
        String imageUri = scaleAllowMultiValue.getImage()
                .map(i -> i.getSafeUri().asString())
                .orElse("");
        StringBuilder sb = new StringBuilder();
        sb.append("<span class=\"")
                .append(postCoordinationStyle.toggleIcon())
                .append("\">")
                .append(isCollapsed ? expandIcon : collapseIcon)
                .append("</span> ")
                .append(headerText)
                .append("&nbsp;")
                .append("<img src='")
                .append(imageUri)
                .append("' class='")
                .append(postCoordinationStyle.headerIcon())
                .append("' title='")
                .append(scaleAllowMultiValue.getTooltip())
                .append("'/>");

        headerHtml = new HTML(sb.toString());

        FlowPanel inner = new FlowPanel();
        inner.setStyleName(postCoordinationStyle.scaleValueHeader());
        inner.add(headerHtml);
        inner.add(editableIconHeader);

        FocusPanel headerWrapper = new FocusPanel(inner);
        headerWrapper.setStyleName(postCoordinationStyle.scaleValueHeader());
        headerWrapper.addClickHandler(evt -> toggleTable());

        valueTable.setWidget(0, 0, headerWrapper);
        valueTable.getFlexCellFormatter().setColSpan(0, 0, 2);
    }

    @Override
    public void addRow(ScaleValueIriAndName value) {
        int addButtonRowIndex = valueTable.getRowCount();

        valueTable.insertRow(addButtonRowIndex - 1);
        setRowContents(addButtonRowIndex - 1, value);
    }


    private void setRowContents(int rowIndex, ScaleValueIriAndName value) {
        valueTable.setWidget(rowIndex, 0, new Label(value.getScaleValueName()));

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

    @Override
    public void setDeleteValueButtonHandler(DeleteScaleValueButtonHandler handler) {
        this.deleteScaleValueButtonHandler = handler;
    }

    private void toggleTable() {
        isCollapsed = !isCollapsed;

        for (int i = 1; i < valueTable.getRowCount(); i++) {
            valueTable.getRowFormatter().setVisible(i, !isCollapsed);
        }

        String currentHtml = headerHtml.getHTML();
        String icon = isCollapsed ? expandIcon : collapseIcon;

        if (currentHtml.contains("<span class=\"" + postCoordinationStyle.toggleIcon() + "\">")) {
            int spanEnd = currentHtml.indexOf("</span>") + 7; // 7 to move past "</span>"

            currentHtml = currentHtml.substring(spanEnd).trim(); // Extract text after the span
        }

        headerHtml.setHTML("<span class=\"" + postCoordinationStyle.toggleIcon() + "\">" + icon + "</span> " + currentHtml);
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

    @Override
    public void setEditMode(boolean enabled) {
        setReadOnly(!enabled);
    }

    private void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
        addButton.setVisible(!readOnly);
        addButton.setEnabled(!readOnly);
        editableIconHeader.setVisible(!readOnly);

        int lastRow = valueTable.getRowCount() - 1;
        if (lastRow >= 0 && valueTable.getWidget(lastRow, 0) == addButton) {
            valueTable.getRowFormatter().setVisible(lastRow, !readOnly);
        }

        for (int i = 1; i < valueTable.getRowCount(); i++) {
            int cellsInRow = valueTable.getCellCount(i);
            if (readOnly) {
                valueTable.getWidget(i, cellsInRow - 1).setVisible(false);
                valueTable.getCellFormatter().addStyleName(i, cellsInRow - 1, postCoordinationStyle.disabled());
            } else {
                valueTable.getWidget(i, cellsInRow - 1).setVisible(true);
                valueTable.getCellFormatter().removeStyleName(i, cellsInRow - 1, postCoordinationStyle.disabled());
            }
        }
    }

}
