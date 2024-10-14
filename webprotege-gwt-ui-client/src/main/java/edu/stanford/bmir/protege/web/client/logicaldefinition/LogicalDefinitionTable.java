package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.common.collect.ImmutableList;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.linearization.LinearizationTableRow;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

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

    private boolean readOnly = true;
    private List<LogicalDefinitionTableRow> tableRows = new ArrayList<>();

    public LogicalDefinitionTable(LogicalDefinitionTableConfig config) {
        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();
        style = LogicalDefinitionResourceBundle.INSTANCE.style();
        this.config = config;
        initializeSuperClassTableHeader();
        injectTableControls();
        setControlsHandlers();
        axisDropdown.setStyleName(style.logicalDefinitionDropdown());
        valuesDropdown.setStyleName(style.logicalDefinitionDropdown());
    }

    @Override
    public Widget asWidget() {
        return flexTable;
    }


    public void addExistingRows(List<LogicalDefinitionTableRow> rows) {
        this.tableRows = rows;
        for(LogicalDefinitionTableRow row: rows) {
            addNewRowToTable(row);
        }
    }

    public void resetTable(){
        if(flexTable.getRowCount() > 2) {
            int count = 0;
            int nrOfRemovableRows = flexTable.getRowCount() - 2;
            while(count < nrOfRemovableRows) {
                this.flexTable.removeRow(1);
                count++;
            }
        }


        this.tableRows = new ArrayList<>();
        this.valuesDropdown.clear();
        this.axisDropdown.clear();
        this.readOnly = true;
    }

    public void setAvailableAxis(Map<String, DropdownElement> specification) {
        axisDropdown.clear();
        axisDropdown.addItem("", "");

        for(String axis : specification.keySet()) {
            axisDropdown.addItem(specification.get(axis).label, axis);
        }

        SelectElement selectElement = SelectElement.as(axisDropdown.getElement());
        NodeList<OptionElement> options = selectElement.getOptions();

        for (int i = 0; i < options.getLength(); i++) {
            String optionValue = options.getItem(i).getValue();
            DropdownElement dropdownElement = specification.get(optionValue);
            if(dropdownElement != null) {
                options.getItem(i).addClassName(dropdownElement.cssClass);
            }
        }
    }

    public void setAvailableValues(Map<String, String> availableValues) {

        this.valuesDropdown.clear();

        valuesDropdown.addItem("","");

        for(String axis: availableValues.keySet()) {
            valuesDropdown.addItem(axis, availableValues.get(axis));
        }
    }


    public void setEditable(){
        this.axisDropdown.setEnabled(true);
        this.valuesDropdown.setEnabled(true);
        this.readOnly = false;
    }

    public void setReadOnly(){
        this.axisDropdown.setEnabled(false);
        this.valuesDropdown.setEnabled(false);
        this.readOnly = true;
    }

    public List<LogicalDefinitionTableRow> getRows(){
        return this.tableRows;
    }

    public List<PropertyClassValue> getValues(){
        return this.tableRows.stream()
                .map(row -> PropertyClassValue.get(getAxisProperty(row), getValueClassData(row), State.ASSERTED))
                .collect(Collectors.toList());
    }

    private OWLObjectPropertyData getAxisProperty(LogicalDefinitionTableRow row) {
        return OWLObjectPropertyData.get(new OWLObjectPropertyImpl(IRI.create(row.getPostCoordinationAxis())), ImmutableList.of(), false);
    }

    private OWLClassData getValueClassData(LogicalDefinitionTableRow row) {
        return  OWLClassData.get(new OWLClassImpl(IRI.create(row.getPostCoordinationValue())), ImmutableList.of(), false);
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
                    row.setPostCoordinationAxisLabel(axisDropdown.getSelectedItemText());
                    row.setPostCoordinationValue(valuesDropdown.getSelectedValue());
                    row.setPostCoordinationValueLabel(valuesDropdown.getSelectedValue());
                    this.tableRows.add(row);
                    addNewRowToTable(row);
                }
            }
        });
    }

    private void addNewRowToTable(LogicalDefinitionTableRow row) {
        Widget axisCell = new Label(row.getPostCoordinationAxisLabel());
        flexTable.setWidget(this.tableRows.size(), 0, axisCell);
        flexTable.setWidget(this.tableRows.size(), 1, new Label(row.getPostCoordinationValueLabel()));
        flexTable.getCellFormatter().addStyleName(this.tableRows.size(), 0, style.tableText());
        flexTable.getCellFormatter().addStyleName(this.tableRows.size(), 1, style.tableText());

        Label removeCell = new Label();
        removeCell.getElement().setInnerHTML(style.getxSvg());
        removeCell.addClickHandler((click)-> this.removeSuperClassAxis(row.getPostCoordinationAxisLabel(), row.getPostCoordinationAxis(), row.getPostCoordinationValueLabel()));

        this.flexTable.setWidget(this.tableRows.size(), 2, removeCell);
        this.flexTable.setWidget(this.tableRows.size()+1, 0, axisDropdown);
        this.flexTable.setWidget(this.tableRows.size()+1, 1, valuesDropdown);

        flexTable.getCellFormatter().addStyleName(this.tableRows.size(), 2, style.removeButtonCell());
        flexTable.getRowFormatter().addStyleName(this.tableRows.size(), style.customRowStyle());
    }

    private void removeSuperClassAxis(String postCoordinationLabel, String postCoordinationAxis, String postCoordinationValue) {
        if(!readOnly) {
            for(int i = 0; i < flexTable.getRowCount(); i++) {
                String existingLabel = flexTable.getText(i, 0);
                String existingValue = flexTable.getText(i, 1);
                if(existingLabel.equalsIgnoreCase(postCoordinationLabel) && existingValue.equalsIgnoreCase(postCoordinationValue)) {
                    flexTable.removeRow(i);
                }
            }

            this.tableRows = this.tableRows.stream()
                    .filter(r -> !r.getPostCoordinationAxis().equalsIgnoreCase(postCoordinationAxis))
                    .collect(Collectors.toList());
        }
    }

    static class DropdownElement {
        private final String label;
        private final String cssClass;


        DropdownElement(String label, String cssClass) {
            this.label = label;
            this.cssClass = cssClass;
        }

        public String getLabel() {
            return label;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

}
