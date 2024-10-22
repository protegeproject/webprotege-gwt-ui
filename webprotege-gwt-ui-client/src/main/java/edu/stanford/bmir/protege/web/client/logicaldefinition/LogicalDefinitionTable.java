package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.common.collect.ImmutableList;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenter;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationSpecification;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.postcoordination.WhoficCustomScalesValues;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
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

    private Button valuesButton = new Button("+");

    private Set<OWLClass> availableValues = new HashSet<>();
    private LogicalDefinitionResourceBundle.LogicalDefinitionCss style;

    private WhoficCustomScalesValues superclassScalesValue;

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
        this.tableRows = new ArrayList<>();
        for(LogicalDefinitionTableRow row: rows) {
            this.tableRows.add(row);
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
        this.availableValues = new HashSet<>();
        this.valuesDropdown.clear();
        this.axisDropdown.clear();
        this.readOnly = true;
    }


    public void setAvailableAxisFromSpec(PostCoordinationSpecification spec, List<PostCoordinationTableAxisLabel> labels) {
        Map<String, LogicalDefinitionTable.DropdownElement> availableAxis = new HashMap<>();

        spec.getRequiredAxes().stream().sorted(
                (s1, s2) -> getAxisName(s1, labels).compareTo(getAxisName(s2, labels)))
                .forEach((requiredAxis) -> {
                    availableAxis.put(requiredAxis, new LogicalDefinitionTable.DropdownElement(getAxisName(requiredAxis, labels), style.dropDownMandatory()));
                });
        spec.getAllowedAxes().stream().sorted((s1, s2) -> getAxisName(s1, labels).compareTo(getAxisName(s2, labels)))
                .forEach((allowedAxis) -> {
                    availableAxis.put(allowedAxis, new LogicalDefinitionTable.DropdownElement(getAxisName(allowedAxis, labels), style.dropDownAllowed()));
                });
        spec.getNotAllowedAxes().stream().sorted((s1, s2) -> getAxisName(s1, labels).compareTo(getAxisName(s2, labels)))
                .forEach((notSetAxis) -> availableAxis.put(notSetAxis, new LogicalDefinitionTable.DropdownElement(getAxisName(notSetAxis, labels), style.dropDownNotSet()))) ;

        setAvailableAxis(availableAxis);
    }

    private String getAxisName(String axisIri, List<PostCoordinationTableAxisLabel> labels) {
        return labels.stream()
                .filter(entry -> entry.getPostCoordinationAxis().equalsIgnoreCase(axisIri))
                .map(PostCoordinationTableAxisLabel::getTableLabel).findFirst()
                .orElse("");
    }

    private void setAvailableAxis(Map<String, DropdownElement> specification) {
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
        this.availableValues = new HashSet<>();

        valuesDropdown.addItem("","");

        for(String axis: availableValues.keySet()) {
            this.availableValues.add(new OWLClassImpl(IRI.create(axis)));
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

    public void setSuperclassScalesValue(WhoficCustomScalesValues superclassScalesValue) {
        this.superclassScalesValue = superclassScalesValue;
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
        valuesButton.addClickHandler(event -> {
            config.getAddAxisValueHandler().handleAddAxisValue(this.axisDropdown.getSelectedValue(), this, this.superclassScalesValue);
        });
        this.flexTable.setWidget(this.tableRows.size()+1, 1, valuesButton);
        flexTable.getCellFormatter().addStyleName(this.tableRows.size(), 2, style.removeButtonCell());
        flexTable.getRowFormatter().addStyleName(this.tableRows.size(), style.customRowStyle());
        if(this.axisDropdown.getItemCount() > 0) {
            this.axisDropdown.setItemSelected(0, true);
        }
        if(this.valuesDropdown.getItemCount() > 0){
            this.valuesDropdown.setItemSelected(0, true);
        }
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

    public interface SelectAvailableValueHandler {
        void handleAxisValueClick(String axis);
    }

}
