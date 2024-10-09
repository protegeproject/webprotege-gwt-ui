package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.user.client.ui.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogicalDefinitionTable implements IsWidget {
    Logger logger = java.util.logging.Logger.getLogger("LogicalDefinitionTable");

    private LogicalDefinitionTableConfig config;

    private FlexTable flexTable = new FlexTable();
    private ListBox axisDropdown = new ListBox();
    private ListBox valuesDropdown = new ListBox();
    private LogicalDefinitionResourceBundle.LogicalDefinitionCss style;

    private Map<String, String> availableAxis = new HashMap<>();

    private Map<String, String> availableValues = new HashMap<>();

    private List<LogicalDefinitionTableRow> tableRows = new ArrayList<>();

    public LogicalDefinitionTable(LogicalDefinitionTableConfig config) {
        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();
        style = LogicalDefinitionResourceBundle.INSTANCE.style();
        this.config = config;
        initializeSuperClassTableHeader();
        injectTableControls();
        setControlsHandlers();
    }

    @Override
    public Widget asWidget() {
        return flexTable;
    }

    public void setAvailableAxis(Map<String,String> availableAxis) {
        this.availableAxis = availableAxis;
        populateAvailableAxisValues();
    }

    public void setAvailableValues(Map<String, String> availableValues) {
        this.availableValues = availableValues;

        this.valuesDropdown.clear();

        valuesDropdown.addItem("","");

        for(String axis: availableValues.keySet()) {
            valuesDropdown.addItem(axis, availableValues.get(axis));
        }

    }

    private void initializeSuperClassTableHeader() {
        flexTable.setStyleName(style.superClassTable());
        addHeaderCell(config.getAxisLabel(), 0);
        addHeaderCell(config.getValueLabel(), 1);
        addHeaderCell("", 2);
        flexTable.getRowFormatter().addStyleName(0, style.superClassTableHeader());

    }

    private void addHeaderCell(String headerText, int column) {
        Widget headerCell = new Label(headerText);
        flexTable.setWidget(0, column, headerCell);
        flexTable.getCellFormatter().addStyleName(0, column, style.tableText());
    }

    private void injectTableControls() {
        this.flexTable.setWidget(this.tableRows.size()+1, 0, axisDropdown);
        this.flexTable.setWidget(this.tableRows.size()+1, 1, valuesDropdown);
    }

    private void populateAvailableAxisValues() {
        this.axisDropdown.clear();
        this.axisDropdown.addItem("", "");
        for(String axis: availableAxis.keySet()) {
            logger.info("ALEX injectez " + axis + " si valoare " + availableAxis.get(axis));
            this.axisDropdown.addItem(availableAxis.get(axis), axis);
        }
    }

    private void setControlsHandlers(){
        this.axisDropdown.addChangeHandler((changeEvent) -> {
            String selectedPostCoordinationIri = axisDropdown.getSelectedValue();
            config.getChangeHandler().handleChange(selectedPostCoordinationIri, this);
        });

        valuesDropdown.addChangeHandler((changeEvent) -> {
            String selectedPostCoordinationIri = axisDropdown.getSelectedValue();
            if(selectedPostCoordinationIri != null && !selectedPostCoordinationIri.isEmpty()){
                Optional<LogicalDefinitionTableRow> existingRow = tableRows.stream()
                        .filter(row -> selectedPostCoordinationIri.equalsIgnoreCase(row.getPostCoordinationAxis()))
                        .findFirst();
                if(!existingRow.isPresent() && valuesDropdown.getSelectedValue() != null && !valuesDropdown.getSelectedValue().isEmpty()) {
                    LogicalDefinitionTableRow row = new LogicalDefinitionTableRow();
                    row.setPostCoordinationAxis(axisDropdown.getSelectedValue());
                    row.setPostCoordinationLabel(axisDropdown.getSelectedItemText());
                    row.setPostCoordinationValue(valuesDropdown.getSelectedValue());
                    row.setPostCoordinationValueLabel(valuesDropdown.getSelectedValue());
                    this.tableRows.add(row);
                    addNewRowToTable(row);
                }
            }
        });
    }

    private void addNewRowToTable(LogicalDefinitionTableRow row) {
        Widget axisCell = new Label(row.getPostCoordinationLabel());
        flexTable.setWidget(this.tableRows.size(), 0, axisCell);
        flexTable.setWidget(this.tableRows.size(), 1, new Label(row.getPostCoordinationValueLabel()));
        flexTable.getCellFormatter().addStyleName(this.tableRows.size(), 0, style.tableText());
        flexTable.getCellFormatter().addStyleName(this.tableRows.size(), 1, style.tableText());

        Button button = new Button();
        button.setText("X");
        button.addClickHandler((click)-> this.removeSuperClassAxis(this.tableRows.size() - 1, row.getPostCoordinationAxis()));
        this.flexTable.setWidget(this.tableRows.size(), 2, button);
        this.flexTable.setWidget(this.tableRows.size()+1, 0, axisDropdown);
        this.flexTable.setWidget(this.tableRows.size()+1, 1, valuesDropdown);

        flexTable.getRowFormatter().addStyleName(this.tableRows.size(), style.customRowStyle());
    }

    private void removeSuperClassAxis(int row, String postCoordinationAxisIri) {
        this.flexTable.removeRow(row);
        this.tableRows = this.tableRows.stream()
                .filter(r -> !r.getPostCoordinationAxis().equalsIgnoreCase(postCoordinationAxisIri))
                .collect(Collectors.toList());
    }

}
