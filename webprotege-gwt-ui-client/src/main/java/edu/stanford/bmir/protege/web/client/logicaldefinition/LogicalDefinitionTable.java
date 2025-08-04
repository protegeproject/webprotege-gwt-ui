package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.common.collect.ImmutableList;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
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

    private List<String> orderedPostCoordinationAxisIris = new ArrayList<>();

    private WhoficCustomScalesValues superclassScalesValue;

    private boolean readOnly = true;

    private LogicalDefinitionChangeHandler logicalDefinitionChangeHandler;

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
        valuesButton.addClickHandler(event -> config.getAddAxisValueHandler()
                .handleAddAxisValue(this.axisDropdown.getSelectedValue(), this, this.superclassScalesValue)
        );
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

        List<String> sortedRequiredAxes = spec.getRequiredAxes().stream()
                .sorted((s1, s2) -> {
                    int s1Pos = this.orderedPostCoordinationAxisIris.indexOf(s1);
                    int s2Pos = this.orderedPostCoordinationAxisIris.indexOf(s2);
                    return s1Pos - s2Pos;
                })
                .collect(Collectors.toList());

        sortedRequiredAxes.forEach((requiredAxis) -> availableAxis.put(
                        requiredAxis,
                        new DropdownElement(getAxisName(requiredAxis, labels), style.dropDownMandatory())
                )
        );

        if(!sortedRequiredAxes.isEmpty()){
            String lastAxis = sortedRequiredAxes.get(sortedRequiredAxes.size() -1);
            availableAxis.get(lastAxis).setFinalElement(true);
        }

        List<String> sortedAllowedAxis = spec.getAllowedAxes().stream()
                .sorted((s1, s2) -> {
                    int s1Pos = this.orderedPostCoordinationAxisIris.indexOf(s1);
                    int s2Pos = this.orderedPostCoordinationAxisIris.indexOf(s2);
                    return s1Pos - s2Pos;
                })
                .collect(Collectors.toList());

        sortedAllowedAxis.forEach((allowedAxis) -> availableAxis.put(
                        allowedAxis,
                        new DropdownElement(getAxisName(allowedAxis, labels), style.dropDownAllowed())
                )
        );

        if(!sortedAllowedAxis.isEmpty()) {
            String lastAllowedAxis = sortedAllowedAxis.get(sortedAllowedAxis.size() -1);
            availableAxis.get(lastAllowedAxis).setFinalElement(true);
        }

        for(String postCoordinationAxis : this.orderedPostCoordinationAxisIris) {
            if(availableAxis.get(postCoordinationAxis) == null) {
                availableAxis.put(postCoordinationAxis, new LogicalDefinitionTable.DropdownElement(getAxisName(postCoordinationAxis, labels), style.dropDownNotSet()));
            }
        }

        setAvailableAxis(availableAxis);
    }

    private String getAxisName(String axisIri, List<PostCoordinationTableAxisLabel> labels) {
        String response = labels.stream()
                .filter(entry -> entry.getPostCoordinationAxis().equalsIgnoreCase(axisIri))
                .map(PostCoordinationTableAxisLabel::getTableLabel).findFirst()
                .orElse("");

        return response;
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
                if(dropdownElement.isFinalElement) {
                    options.getItem(i).addClassName(style.lastItemInDropdown());
                }
            }
        }
    }

    public void setAvailableValues(Map<String, String> availableValues) {
        this.availableValues = new HashSet<>();

        for(String axis: availableValues.keySet()) {
            this.availableValues.add(DataFactory.getOWLClass(axis));
        }
    }


    public void setPostCoordinationTableConfiguration(PostCoordinationTableConfiguration postCoordinationTableConfiguration){
        this.orderedPostCoordinationAxisIris.addAll(postCoordinationTableConfiguration.getPostCoordinationAxes());

        for(PostCoordinationCompositeAxis coordinationCompositeAxis : postCoordinationTableConfiguration.getCompositePostCoordinationAxes()) {
            this.orderedPostCoordinationAxisIris.addAll(coordinationCompositeAxis.getSubAxis());
        }
    }

    public void setReadOnly(boolean readOnly){
        this.axisDropdown.setEnabled(!readOnly);
        this.axisDropdown.setVisible(!readOnly);
        this.valuesButton.setVisible(!readOnly);
        this.setDeleteRowButtonVisible(!readOnly);
        this.readOnly = readOnly;
    }

    private void setDeleteRowButtonVisible(boolean visible) {
        for(int i = 1; i<=this.tableRows.size(); i++){
            this.flexTable.getWidget(i, 2).setVisible(visible);
        }
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
        addHeaderCell(config.getAxisLabel(),  0);
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

    public void setLogicalDefinitionChangeHandler(LogicalDefinitionChangeHandler logicalDefinitionChangeHandler) {
        this.logicalDefinitionChangeHandler = logicalDefinitionChangeHandler;
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
                logicalDefinitionChangeHandler.handleLogicalDefinitionCHange();
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
        if(readOnly) {
            removeButton.setVisible(false);
        }
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

            logicalDefinitionChangeHandler.handleLogicalDefinitionCHange();
        }
    }

    static class DropdownElement {
        private final String label;
        private final String cssClass;

        private boolean isFinalElement = false;


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
        public void setFinalElement(boolean finalElement) {
            isFinalElement = finalElement;
        }
    }

}
