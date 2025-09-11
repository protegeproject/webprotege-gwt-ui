package edu.stanford.bmir.protege.web.client.card.postcoordination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.card.EditableIcon;
import edu.stanford.bmir.protege.web.client.postcoordination.*;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.TableCellChangedHandler;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PostcoordinationCardViewImpl extends Composite implements PostcoordinationCardView {

    Logger logger = java.util.logging.Logger.getLogger("PostCoordinationPortletViewImpl");

    @UiField
    HTMLPanel paneContainer;

    @UiField
    protected FlexTable flexTable;
    @UiField
    public VerticalPanel scaleValueCardList;

    private String entityIri;

    private Map<String, PostCoordinationTableAxisLabel> labels = new HashMap<>();
    private Map<String, LinearizationDefinition> definitionMap = new HashMap<>();

    private List<PostCoordinationTableRow> tableRows = new ArrayList<>();

    private TableCellChangedHandler tableCellChanged = (isAxisEnabledOnAnyRow, checkboxValue, tableAxisLabel) -> {
    };

    private static final PostCoordinationTableResourceBundle.PostCoordinationTableCss style = PostCoordinationTableResourceBundle.INSTANCE.style();
    private static final WebProtegeClientBundle wpStyle = WebProtegeClientBundle.BUNDLE;

    private static final PostcoordinationCardViewImpl.PostcoordinationCardViewImplUiBinder ourUiBinder = GWT.create(PostcoordinationCardViewImpl.PostcoordinationCardViewImplUiBinder.class);

    private static final Messages MESSAGES = GWT.create(Messages.class);

    @Inject
    public PostcoordinationCardViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));

        style.ensureInjected();
    }

    @Override
    public Optional<WhoficEntityPostCoordinationSpecification> getTableData() {
        return createEditedSpec();
    }

    public void setReadOnlyState() {
        for (PostCoordinationTableRow row : tableRows) {
            for (PostCoordinationTableCell cell : row.getCellList()) {
                cell.setState(true);
            }
            row.getEditableIconFront().setVisible(false);
            row.getEditableIconBack().setVisible(false);
        }
    }

    public void setEditableState() {
        for (PostCoordinationTableRow row : tableRows) {
            if (row.getLinearizationDefinition().getDefinitionAccessibility().equals(LinearizationDefinitionAccessibility.EDITABLE)) {
                for (PostCoordinationTableCell cell : row.getCellList()) {
                    cell.setState(false);
                }
                row.getEditableIconFront().setVisible(true);
                row.getEditableIconBack().setVisible(true);
            }
        }
    }

    @Override
    public void resetTable() {
        flexTable.clear(true);
        while (flexTable.getRowCount() > 0) {
            flexTable.removeRow(0);
        }
        labels.clear();
        tableRows.clear();
        definitionMap.clear();
    }

    private Optional<WhoficEntityPostCoordinationSpecification> createEditedSpec() {
        WhoficEntityPostCoordinationSpecification specification = new WhoficEntityPostCoordinationSpecification(entityIri, "ICD", new ArrayList<>());
        boolean somethingChanged = false;
        for (PostCoordinationTableRow tableRow : this.tableRows) {
            PostCoordinationSpecification postCoordinationSpecification = new PostCoordinationSpecification(tableRow.getLinearizationDefinition().getLinearizationUri(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>());
            for (PostCoordinationTableCell cell : tableRow.getCellList()) {
                if (cell.isTouched()) {
                    if (cell.getValue().equalsIgnoreCase("NOT_ALLOWED")) {
                        postCoordinationSpecification.getNotAllowedAxes().add(cell.getAxisLabel().getPostCoordinationAxis());
                        somethingChanged = true;
                    }
                    if (cell.getValue().equalsIgnoreCase("ALLOWED")) {
                        postCoordinationSpecification.getAllowedAxes().add(cell.getAxisLabel().getPostCoordinationAxis());
                        somethingChanged = true;
                    }
                    if (cell.getValue().equalsIgnoreCase("REQUIRED")) {
                        postCoordinationSpecification.getRequiredAxes().add(cell.getAxisLabel().getPostCoordinationAxis());
                        somethingChanged = true;
                    }
                    if (cell.getValue().startsWith("DEFAULT")) {
                        postCoordinationSpecification.getDefaultAxes().add(cell.getAxisLabel().getPostCoordinationAxis());
                        somethingChanged = true;
                    }
                }
            }
            specification.getPostCoordinationSpecifications().add(postCoordinationSpecification);
        }
        if (somethingChanged) {
            return Optional.of(specification);
        }

        return Optional.empty();
    }

    @Override
    public void setLabels(Map<String, PostCoordinationTableAxisLabel> labels) {
        this.labels = labels;
    }

    @Override
    public void setLinearizationDefinitonMap(Map<String, LinearizationDefinition> linearizationDefinitonMap) {
        this.definitionMap = linearizationDefinitonMap;
    }

    @Override
    public void initializeTable() {
        initializeTableHeader();
        initializeTableContent();
    }

    @Override
    public VerticalPanel getScaleValueCardsView() {
        return scaleValueCardList;
    }

    private void initializeTableHeader() {
        flexTable.setWidget(0, 0, new Label("Linearization / Use"));
        flexTable.getCellFormatter().addStyleName(0, 0, style.getHeaderLabel());
        List<PostCoordinationTableAxisLabel> labelList = new ArrayList<>(this.labels.values());
        for (int i = 0; i < labelList.size(); i++) {
            addHeaderCell(labelList.get(i).getTableLabel(), i + 1);
        }
        flexTable.getCellFormatter().addStyleName(0, 0, style.getPostCoordinationHeader());
        flexTable.setWidget(0, labelList.size() + 1, new Label("Linearization / Use"));
        flexTable.getCellFormatter().addStyleName(0, labelList.size() + 1, style.getHeaderLabel());
        flexTable.getCellFormatter().addStyleName(0, labelList.size() + 1, style.getPostCoordinationHeader());
        flexTable.addStyleName(style.getPostCoordinationTable());
        flexTable.getRowFormatter().addStyleName(0, style.getHeaderLabelRow());
        flexTable.getRowFormatter().addStyleName(0, style.stickyTableHeader());
    }

    private void initializeTableContent() {
        List<LinearizationDefinition> definitions = new ArrayList<>(this.definitionMap.values());
        for (LinearizationDefinition definition : definitions) {
            PostCoordinationTableRow tableRow = new PostCoordinationTableRow(definition);
            List<PostCoordinationTableAxisLabel> labelList = new ArrayList<>(this.labels.values());
            for (PostCoordinationTableAxisLabel postCoordinationTableAxisLabel : labelList) {
                PostCoordinationTableCell cell = new PostCoordinationTableCell(definition, postCoordinationTableAxisLabel);
                cell.addValueChangeHandler(valueChanged -> tableCellChanged.handleTableCellChanged(
                                isAxisEnabledOnAnyRow(postCoordinationTableAxisLabel),
                                valueChanged.getValue(),
                                cell.getAxisLabel().getPostCoordinationAxis()
                        )
                );
                tableRow.addCell(cell);
            }
            this.tableRows.add(tableRow);
        }
        orderAndPopulateViewWithRows();
        bindCellsToParentCells();
    }


    private void orderAndPopulateViewWithRows() {
        List<PostCoordinationTableRow> orderedRows = tableRows.stream()
                .sorted((o1, o2) -> o1.getLinearizationDefinition().getSortingCode().compareToIgnoreCase(o2.getLinearizationDefinition().getSortingCode()))
                .collect(Collectors.toList());
        this.tableRows = orderedRows;

        for (int i = 0; i < orderedRows.size(); i++) {

            addRowLabel(
                    orderedRows.get(i).isDerived(),
                    orderedRows.get(i).getLinearizationDefinition().getDisplayLabel(),
                    i + 1,
                    0,
                    orderedRows.get(i).getEditableIconFront(),
                    orderedRows.get(i).getLinearizationDefinition().getCoreLinId()
            );

            for (int j = 0; j < orderedRows.get(i).getCellList().size(); j++) {
                flexTable.setWidget(i + 1, j + 1, orderedRows.get(i).getCellList().get(j).asWidget());
            }
            flexTable.getRowFormatter().addStyleName(i + 1, style.getCustomRowStyle());
            if ((i + 1) % 2 == 1) {
                flexTable.getRowFormatter().addStyleName(i + 1, style.getEvenRowStyle());
            }

            addRowLabel(
                    orderedRows.get(i).isDerived(),
                    orderedRows.get(i).getLinearizationDefinition().getDisplayLabel(),
                    i + 1,
                    orderedRows.get(i).getCellList().size() + 1,
                    orderedRows.get(i).getEditableIconBack(),
                    orderedRows.get(i).getLinearizationDefinition().getCoreLinId()
            );
        }
    }


    private boolean isAxisEnabledOnAnyRow(PostCoordinationTableAxisLabel axisLabel) {
        List<PostCoordinationTableRow> tableRowsWithAxisChecked = this.tableRows.stream()
                .filter(tableRow -> tableRow.getCellList()
                        .stream()
                        .anyMatch(cell ->
                                cell.getAxisLabel().equals(axisLabel) &&
                                        (cell.getValue().equals("ALLOWED") || cell.getValue().equals("REQUIRED"))
                        )
                )
                .collect(Collectors.toList());
        return tableRowsWithAxisChecked.size() > 0;
    }


    private void bindCellsToParentCells() {
        for (PostCoordinationTableRow row : this.tableRows) {
            if (!row.isDerived()) {
                for (PostCoordinationTableRow childRow : this.tableRows) {
                    if (childRow.isDerived() && childRow.getLinearizationDefinition().getCoreLinId().equalsIgnoreCase(row.getLinearizationDefinition().getLinearizationId())) {
                        for (PostCoordinationTableCell parentCell : row.getCellList()) {
                            for (PostCoordinationTableCell childCell : childRow.getCellList()) {
                                if (parentCell.getAxisLabel().getPostCoordinationAxis().equalsIgnoreCase(childCell.getAxisLabel().getPostCoordinationAxis())) {
                                    parentCell.addToChildCells(childCell);
                                    childCell.setIsDerived();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addRowLabel(boolean isDerived, String label, int row, int column, EditableIcon editableIcon, String coreLinId) {
        editableIcon.addStyleName(style.size75());
        editableIcon.addStyleName(style.marginLeftAuto());
        editableIcon.setVisible(false);

        InlineLabel rowLabel = new InlineLabel();
        rowLabel.setText(label);

        FlowPanel labelPanel = new FlowPanel();
        labelPanel.setStyleName(style.getRowLabel());
        if (isDerived) {
            Image telescopic = new Image(wpStyle.svgTelescopicIcon().getSafeUri());
            telescopic.setPixelSize(16, 16);
            telescopic.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
            telescopic.setTitle(MESSAGES.linearization_telescopic(coreLinId));
            labelPanel.add(telescopic);
        }
        labelPanel.add(rowLabel);
        labelPanel.add(editableIcon);

        flexTable.setWidget(row, column, labelPanel);
    }

    private void addHeaderCell(String label, int position) {
        Widget headerCell = new Label();
        headerCell.getElement().setInnerHTML(getHeaderLabelPadded(25, label));
        flexTable.setWidget(0, position, headerCell);
        flexTable.getCellFormatter().addStyleName(0, position, style.getPostCoordinationHeader());
        flexTable.getCellFormatter().addStyleName(0, position, style.getRotatedHeader());
    }

    private String getHeaderLabelPadded(int padding, String label) {

        StringBuilder result = new StringBuilder();
        int lastBreak = 0;

        for (int i = padding; i < label.length(); i += padding) {
            int spaceIndex = label.lastIndexOf(' ', i);

            if (spaceIndex > lastBreak) {
                result.append(label, lastBreak, spaceIndex).append("</br>");
                lastBreak = spaceIndex + 1;
            } else {
                result.append(label, lastBreak, i).append("</br>");
                lastBreak = i;
            }
        }

        result.append(label.substring(lastBreak));

        return result.toString();
    }

    @Override
    public void setTableCellChangedHandler(TableCellChangedHandler handler) {
        this.tableCellChanged = handler;
    }

    @Override
    public void setTableData(WhoficEntityPostCoordinationSpecification whoficSpecification) {
        logger.info("Set table data");
        this.entityIri = whoficSpecification.getWhoficEntityIri();

        for (PostCoordinationTableRow row : this.tableRows) {
            for (PostCoordinationTableCell cell : row.getCellList()) {
                cell.reset();
            }
        }

        if (!whoficSpecification.getPostCoordinationSpecifications().isEmpty()) {
            for (PostCoordinationTableRow row : this.tableRows) {
                for (PostCoordinationTableCell cell : row.getCellList()) {
                    PostCoordinationSpecification specification = whoficSpecification.getPostCoordinationSpecifications().stream()
                            .filter(spec -> spec.getLinearizationView()
                                    .equalsIgnoreCase(cell.getLinearizationDefinition().getLinearizationUri()))
                            .findFirst()
                            .orElse(null);

                    if (specification != null) {
                        if (specification.getAllowedAxes().contains(cell.getAxisLabel().getPostCoordinationAxis())) {
                            cell.setValue("ALLOWED");
                        }

                        if (specification.getRequiredAxes().contains(cell.getAxisLabel().getPostCoordinationAxis())) {
                            cell.setValue("REQUIRED");
                        }

                        if (specification.getNotAllowedAxes().contains(cell.getAxisLabel().getPostCoordinationAxis())) {
                            cell.setValue("NOT_ALLOWED");
                        }

                    } else {
                        cell.setValue("NOT_ALLOWED");
                    }
                }
            }

        }
        bindCellsToParentCells();

        for (PostCoordinationTableRow row : this.tableRows) {
            for (PostCoordinationTableCell cell : row.getCellList()) {
                cell.updateChildren();
                cell.initializeCallback();
            }
        }

        setReadOnlyState();
    }

    @Override
    public void setWidget(IsWidget w) {
        paneContainer.clear();
        paneContainer.add(w);
    }

    @Override
    public void dispose() {

    }

    interface PostcoordinationCardViewImplUiBinder extends UiBinder<HTMLPanel, PostcoordinationCardViewImpl> {

    }
}
