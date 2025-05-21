package edu.stanford.bmir.protege.web.client.card.postcoordination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.postcoordination.*;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.TableCellChangedHandler;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinitionAccessibility;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationSpecification;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.postcoordination.WhoficEntityPostCoordinationSpecification;

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

    private static final PostcoordinationCardViewImpl.PostcoordinationCardViewImplUiBinder ourUiBinder = GWT.create(PostcoordinationCardViewImpl.PostcoordinationCardViewImplUiBinder.class);

    @Inject
    public PostcoordinationCardViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));

        style.ensureInjected();
    }

    @Override
    public Optional<WhoficEntityPostCoordinationSpecification> getTableData() {
        return createEditedSpec();
    }

    public void setReadOnlyState(){
        for (PostCoordinationTableRow row : tableRows) {
            for (PostCoordinationTableCell cell : row.getCellList()) {
                cell.setState(true);
            }
        }
    }
    public void setEditableState() {
        for (PostCoordinationTableRow row : tableRows) {
            if(row.getLinearizationDefinition().getDefinitionAccessibility().equals(LinearizationDefinitionAccessibility.EDITABLE)) {
                for (PostCoordinationTableCell cell : row.getCellList()) {
                    cell.setState(false);
                }
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
    }

    private void initializeTableContent() {
        List<LinearizationDefinition> definitions = new ArrayList<>(this.definitionMap.values());
        for (LinearizationDefinition definition : definitions) {
            PostCoordinationTableRow tableRow = new PostCoordinationTableRow(definition);
            List<PostCoordinationTableAxisLabel> labelList = new ArrayList<>(this.labels.values());
            for (PostCoordinationTableAxisLabel postCoordinationTableAxisLabel : labelList) {
                PostCoordinationTableCell cell = new PostCoordinationTableCell(definition, postCoordinationTableAxisLabel, tableRow);
                cell.addValueChangeHandler(valueChanged -> {
                    tableCellChanged.handleTableCellChanged(
                            isAxisEnabledOnAnyRow(postCoordinationTableAxisLabel),
                            valueChanged.getValue(),
                            cell.getAxisLabel().getPostCoordinationAxis()
                    );
                });
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

            addRowLabel(orderedRows.get(i).isDerived(), orderedRows.get(i).getLinearizationDefinition().getDisplayLabel(), i + 1, 0);

            for(int j = 0; j < orderedRows.get(i).getCellList().size(); j ++) {
                flexTable.setWidget(i + 1, j + 1, orderedRows.get(i).getCellList().get(j).asWidget());
            }
            flexTable.getRowFormatter().addStyleName(i + 1, style.getCustomRowStyle());
            if ((i + 1) % 2 == 1) {
                flexTable.getRowFormatter().addStyleName(i + 1, style.getEvenRowStyle());
            }

            addRowLabel(orderedRows.get(i).isDerived(), orderedRows.get(i).getLinearizationDefinition().getDisplayLabel(), i + 1, orderedRows.get(i).getCellList().size() + 1);
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


    private void bindCellsToParentCells(){
        for(PostCoordinationTableRow row : this.tableRows) {
            if(!row.isDerived()) {
                for(PostCoordinationTableRow childRow : this.tableRows) {
                    if(childRow.isDerived() && childRow.getLinearizationDefinition().getCoreLinId().equalsIgnoreCase(row.getLinearizationDefinition().getLinearizationId())) {
                        for(PostCoordinationTableCell parentCell: row.getCellList()) {
                            for(PostCoordinationTableCell childCell: childRow.getCellList()) {
                                if(parentCell.getAxisLabel().getPostCoordinationAxis().equalsIgnoreCase(childCell.getAxisLabel().getPostCoordinationAxis())) {
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

    private void addRowLabel(boolean isDerived, String label, int row, int column) {
        String rowLabelString;
        if (isDerived) {
            rowLabelString = SVG + label;
        } else {
            rowLabelString = label;
        }
        Widget rowLabel = new Label();
        rowLabel.getElement().setInnerHTML(rowLabelString);
        rowLabel.addStyleName(style.getRowLabel());
        flexTable.setWidget(row, column, rowLabel);
    }

    private void addHeaderCell(String label, int position) {
        Widget headerCell = new Label();
        headerCell.getElement().setInnerHTML(getHeaderLabelPadded(25, label));
        //headerCell.getElement().setInnerHTML(label);
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

        for(PostCoordinationTableRow row: this.tableRows) {
            for(PostCoordinationTableCell cell : row.getCellList()) {
                cell.reset();
            }
        }

        if(!whoficSpecification.getPostCoordinationSpecifications().isEmpty()) {
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

        for(PostCoordinationTableRow row: this.tableRows) {
            for(PostCoordinationTableCell cell: row.getCellList()) {
                cell.updateChildren();
                cell.initializeCallback();
            }
        }

        setReadOnlyState();
    }

    private static final String SVG = "<div style='width: 12px; height: 12px; margin-right:2px;' >" +

            "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M3 7V8.2C3 9.88016 3 10.7202 3.32698 11.362C3.6146 11.9265 4.07354 12.3854 4.63803 12.673C5.27976 13 6.11984 13 7.8 13H21M21 13L17 9M21 13L17 17\" stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>" +
            "</div>";

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
