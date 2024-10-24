package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.common.collect.ImmutableList;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
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
    private Button valuesButton = new Button();
    private Set<OWLClass> availableValues = new HashSet<>();
    private LogicalDefinitionResourceBundle.LogicalDefinitionCss style;

    private WhoficCustomScalesValues superclassScalesValue;

    private boolean readOnly = true;
    private List<LogicalDefinitionTableRow> tableRows = new ArrayList<>();

    private static final WebProtegeClientBundle.ButtonsCss buttonCss = WebProtegeClientBundle.BUNDLE.buttons();


    public LogicalDefinitionTable(LogicalDefinitionTableConfig config) {
        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();
        style = LogicalDefinitionResourceBundle.INSTANCE.style();
        this.config = config;
        initializeSuperClassTableHeader();
        injectTableControls();
        setControlsHandlers();
        axisDropdown.setStyleName(style.logicalDefinitionDropdown());
        axisDropdown.addStyleName(style.axisDropdownPlaceholder());
        valuesButton.addStyleName(style.addAxisValueButton());
        valuesButton.setStyleName(buttonCss.addButton());
        valuesButton.setEnabled(false);
        valuesButton.addClickHandler(event -> {
            config.getAddAxisValueHandler().handleAddAxisValue(this.axisDropdown.getSelectedValue(), this, this.superclassScalesValue);
        });
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
        axisDropdown.addItem("Select axis", "");

        for(String axis : specification.keySet()) {
            axisDropdown.addItem(specification.get(axis).label, axis);
        }

        SelectElement selectElement = SelectElement.as(axisDropdown.getElement());
        NodeList<OptionElement> options = selectElement.getOptions();

        for (int i = 1; i < options.getLength(); i++) {
            String optionValue = options.getItem(i).getValue();
            DropdownElement dropdownElement = specification.get(optionValue);
            if(dropdownElement != null) {
                options.getItem(i).addClassName(dropdownElement.cssClass);
            }
        }
    }

    public void setAvailableValues(Map<String, String> availableValues) {
        this.availableValues = new HashSet<>();

        for(String axis: availableValues.keySet()) {
            this.availableValues.add(new OWLClassImpl(IRI.create(axis)));
        }
    }


    public void setEditable(){
        this.axisDropdown.setEnabled(true);
        this.readOnly = false;
    }

    public void setReadOnly(){
        this.axisDropdown.setEnabled(false);
        this.valuesButton.setEnabled(false);
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
        flexTable.getCellFormatter().setStyleName(0, 0, style.axisColumn());
        flexTable.getCellFormatter().setStyleName(0, 1, style.valuesColumn());
        flexTable.getCellFormatter().setStyleName(0, 2, style.removeButtonCell());
    }

    private void addHeaderCell(String headerText, int column) {
        Widget headerCell = new Label(headerText);
        flexTable.setWidget(0, column, headerCell);
    }

    public void setSuperclassScalesValue(WhoficCustomScalesValues superclassScalesValue) {
        this.superclassScalesValue = superclassScalesValue;
    }

    private void injectTableControls() {
        this.flexTable.setWidget(this.tableRows.size()+1, 0, axisDropdown);
        this.flexTable.setWidget(this.tableRows.size()+1, 1, valuesButton);
    }

    private void setControlsHandlers(){
        this.axisDropdown.addChangeHandler((changeEvent) -> {
            String selectedPostCoordinationIri = axisDropdown.getSelectedValue();
            if(selectedPostCoordinationIri.isEmpty()) {
                axisDropdown.addStyleName(style.axisDropdownPlaceholder());
            } else {
                axisDropdown.removeStyleName(style.axisDropdownPlaceholder());
                this.valuesButton.setEnabled(true);
                config.getChangeHandler().handleChange(selectedPostCoordinationIri, this);
            }
        });
    }

    public void addNewRow(EntityNode entityNode) {
        if(entityNode != null){

            boolean rowAlreadyExists = this.tableRows.stream()
                    .anyMatch(row -> row.getPostCoordinationAxis().equalsIgnoreCase(axisDropdown.getSelectedValue()) &&
                            row.getPostCoordinationValue().equalsIgnoreCase(entityNode.getEntity().getIRI().toString()));
            if(!rowAlreadyExists && !axisDropdown.getSelectedValue().isEmpty()) {
                LogicalDefinitionTableRow row = new LogicalDefinitionTableRow();
                row.setPostCoordinationAxis(axisDropdown.getSelectedValue());
                row.setPostCoordinationAxisLabel(axisDropdown.getSelectedItemText());
                row.setPostCoordinationValue(entityNode.getEntity().getIRI().toString());
                row.setPostCoordinationValueLabel(entityNode.getBrowserText());
                this.tableRows.add(row);
                addNewRowToTable(row);
            }
        }
    }

    private void addNewRowToTable(LogicalDefinitionTableRow row) {
        Widget axisCell = new Label(row.getPostCoordinationAxisLabel());

        flexTable.setWidget(this.tableRows.size(), 0, axisCell);
        flexTable.setWidget(this.tableRows.size(), 1, new Label(row.getPostCoordinationValueLabel()));

        flexTable.getCellFormatter().addStyleName(this.tableRows.size(), 0, style.tableText());
        flexTable.getCellFormatter().addStyleName(this.tableRows.size(), 1, style.tableText());
        flexTable.getCellFormatter().addStyleName(this.tableRows.size(),  0, style.axisColumn());
        flexTable.getCellFormatter().addStyleName(this.tableRows.size(),  1, style.valuesColumn());


        Button removeButton = new Button();
        removeButton.addClickHandler((click) -> this.removeSuperClassAxis(row));
        removeButton.setStyleName(buttonCss.deleteButton());

        this.flexTable.setWidget(this.tableRows.size(), 2, removeButton);

        this.flexTable.setWidget(this.tableRows.size()+1, 0, axisDropdown);
        axisDropdown.addStyleName(style.axisDropdownPlaceholder());
        this.flexTable.setWidget(this.tableRows.size()+1, 1, valuesButton);

        flexTable.getCellFormatter().addStyleName(this.tableRows.size() + 1, 0, style.axisColumn());
        flexTable.getCellFormatter().addStyleName(this.tableRows.size() + 1, 1, style.addAxisValueCell());
        flexTable.getCellFormatter().addStyleName(this.tableRows.size() + 1, 1, style.valuesColumn());
        flexTable.getCellFormatter().addStyleName(this.tableRows.size(), 2, style.removeButtonCell());

        flexTable.getRowFormatter().setStyleName(this.tableRows.size(), style.customRowStyle());

        if(this.axisDropdown.getItemCount() > 0) {
            this.axisDropdown.setItemSelected(0, true);
        }
    }

    private void removeSuperClassAxis(LogicalDefinitionTableRow row) {
        if(!readOnly) {
            for(int i = 0; i < flexTable.getRowCount(); i++) {
                String existingLabel = flexTable.getText(i, 0);
                String existingValue = flexTable.getText(i, 1);
                if(existingLabel.equalsIgnoreCase(row.getPostCoordinationAxisLabel()) && existingValue.equalsIgnoreCase(row.getPostCoordinationValueLabel())) {
                    flexTable.removeRow(i);
                }
            }

            this.tableRows = this.tableRows.stream()
                    .filter((LogicalDefinitionTableRow r) -> !r.getPostCoordinationAxis().equalsIgnoreCase(row.getPostCoordinationAxis()) ||
                            !r.getPostCoordinationValue().equalsIgnoreCase(row.getPostCoordinationValue()))
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
