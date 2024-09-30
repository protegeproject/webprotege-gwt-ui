package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.TableCellChangedHandler;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PostCoordinationPortletViewImpl extends Composite implements PostCoordinationPortletView {
    Logger logger = java.util.logging.Logger.getLogger("PostCoordinationPortletViewImpl");

    @UiField
    HTMLPanel paneContainer;

    @UiField
    protected FlexTable flexTable;
    @UiField
    public VerticalPanel scaleValueCardList;
    @UiField
    Button saveValuesButton;
    @UiField
    Button editValuesButton;

    @UiField
    Button cancelButton;

    private boolean readOnly = true;

    private String entityIri;
    private ProjectId projectId;

    private Map<String, PostCoordinationTableAxisLabel> labels;
    private Map<String, LinearizationDefinition> definitionMap;

    private List<PostCoordinationTableRow> tableRows = new ArrayList<>();
    private final DispatchServiceManager dispatch;

    private TableCellChangedHandler tableCellChanged = (isAxisEnabledOnAnyRow, checkboxValue, tableAxisLabel) -> {
    };

    private EditButtonHandler editButtonHandler = () -> {
    };
    private CancelButtonHandler cancelButtonHandler = () -> {
    };
    private SaveButtonHandler saveButtonHandler = (Optional<WhoficEntityPostCoordinationSpecification> specificationOptional) -> {
    };

    private static final PostCoordinationTableResourceBundle.PostCoordinationTableCss style = PostCoordinationTableResourceBundle.INSTANCE.style();

    private static final PostCoordinationPortletViewImpl.PostCoordinationPortletViewImplUiBinder ourUiBinder = GWT.create(PostCoordinationPortletViewImpl.PostCoordinationPortletViewImplUiBinder.class);

    @Inject
    public PostCoordinationPortletViewImpl(DispatchServiceManager dispatch) {
        initWidget(ourUiBinder.createAndBindUi(this));

        saveValuesButton.addClickHandler(event -> saveButtonHandler.saveValues(createEditedSpec()));
        cancelButton.addClickHandler(event -> cancelButtonHandler.handleCancelButton());
        editValuesButton.addClickHandler(event -> editButtonHandler.enableEditMode());
        saveValuesButton.setVisible(!readOnly);
        editValuesButton.setVisible(readOnly);
        this.dispatch = dispatch;
        style.ensureInjected();
    }


    public void setEditMode(boolean editMode) {
        readOnly = !editMode;
        setTableState(readOnly);
        saveValuesButton.setVisible(editMode);
        cancelButton.setVisible(editMode);
        editValuesButton.setVisible(!editMode);
    }

    @Override
    public void setEditButtonHandler(EditButtonHandler handler) {
        this.editButtonHandler = handler;
    }

    @Override
    public void setCancelButtonHandler(CancelButtonHandler handler) {
        this.cancelButtonHandler = handler;
    }

    @Override
    public void setSaveButtonHandler(SaveButtonHandler handler) {
        this.saveButtonHandler = handler;
    }

    private void setTableState(boolean readOnly) {
        for (PostCoordinationTableRow row : tableRows) {
            for (PostCoordinationTableCell cell : row.getCellList()) {
                cell.setState(readOnly);
            }
        }
    }

    private Optional<WhoficEntityPostCoordinationSpecification> createEditedSpec() {
        WhoficEntityPostCoordinationSpecification specification = new WhoficEntityPostCoordinationSpecification(entityIri, "ICD", new ArrayList<>());
        boolean somethingChanged = false;
        for (PostCoordinationTableRow tableRow : this.tableRows) {
            PostCoordinationSpecification postCoordinationSpecification = new PostCoordinationSpecification(tableRow.getLinearizationDefinition().getWhoficEntityIri(),
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
    public void setProjectId(ProjectId projectId) {
        this.projectId = projectId;
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

    @Override
    public void setWidget(IsWidget w) {

    }

    @Override
    public void dispose() {

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
        for (int i = 0; i < definitions.size(); i++) {
            PostCoordinationTableRow tableRow = new PostCoordinationTableRow(definitions.get(i));
            addRowLabel(tableRow.isDerived(), definitions.get(i).getDisplayLabel(), i + 1, 0);
            List<PostCoordinationTableAxisLabel> labelList = new ArrayList<>(this.labels.values());
            for (int j = 0; j < labelList.size(); j++) {
                LinearizationDefinition linDef = definitions.get(i);
                PostCoordinationTableAxisLabel axisLabel = labelList.get(j);
                PostCoordinationTableCell cell = new PostCoordinationTableCell(linDef, axisLabel, tableRow);
                cell.addValueChangeHandler(valueChanged -> {
                    tableCellChanged.handleTableCellChanged(
                            isAxisEnabledOnAnyRow(axisLabel),
                            valueChanged.getValue(),
                            cell.getAxisLabel().getPostCoordinationAxis()
                    );
                });
                flexTable.setWidget(i + 1, j + 1, cell.asWidget());
                tableRow.addCell(cell);
            }
            addRowLabel(false, definitions.get(i).getDisplayLabel(), i + 1, labelList.size() + 1);

            flexTable.getRowFormatter().addStyleName(i + 1, style.getCustomRowStyle());
            if ((i + 1) % 2 == 1) {
                flexTable.getRowFormatter().addStyleName(i + 1, style.getEvenRowStyle());
            }
            this.tableRows.add(tableRow);
        }

        bindCellsToParentCells();
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
                    if(childRow.isDerived() && childRow.getLinearizationDefinition().getCoreLinId().equalsIgnoreCase(row.getLinearizationDefinition().getId())) {
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
                                    .equalsIgnoreCase(cell.getLinearizationDefinition().getWhoficEntityIri()))
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

        setTableState(true);
        editValuesButton.setVisible(true);
        saveValuesButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    private static final String SVG = "<div style='width: 12px; height: 12px; margin-right:2px;' >" +

            "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M3 7V8.2C3 9.88016 3 10.7202 3.32698 11.362C3.6146 11.9265 4.07354 12.3854 4.63803 12.673C5.27976 13 6.11984 13 7.8 13H21M21 13L17 9M21 13L17 17\" stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>" +
            "</div>";

    interface PostCoordinationPortletViewImplUiBinder extends UiBinder<HTMLPanel, PostCoordinationPortletViewImpl> {

    }
}
