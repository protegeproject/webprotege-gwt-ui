package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.LogicalDefinition;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogicalDefinitionTableWrapperImpl extends Composite implements LogicalDefinitionTableWrapper {
    Logger logger = java.util.logging.Logger.getLogger("LogicalDefinitionTableWrapperImpl");

    @UiField
    HTMLPanel paneContainer;

    @UiField
    HTMLPanel ancestorWrapper;

    @UiField
    ListBox ancestorDropdown;

    @UiField
    Button deleteTableWrapper;

    @UiField
    Label superClassLabel;

    @UiField
    HTMLPanel tableWrapper;

    @UiField
    Label ancestorSelectedSuperclass;

    private final DispatchServiceManager dispatchServiceManager;

    private final LogicalDefinitionResourceBundle.LogicalDefinitionCss style;

    private final ProjectId projectId;
    private List<PostCoordinationTableAxisLabel> labels;

    private WhoficCustomScalesValues superclassScalesValue;
    private WhoficEntityPostCoordinationSpecification superclassSpecification;
    private List<PostcoordinationAxisToGenericScale> axisToGenericScales = new ArrayList<>();
    private static final LogicalDefinitionTableWrapperImpl.LogicalDefinitionTableWrapperImplUiBinder ourUiBinder =
            GWT.create(LogicalDefinitionTableWrapperImpl.LogicalDefinitionTableWrapperImplUiBinder.class);

    private List<OWLEntityData> ancestorsList;


    private final LogicalDefinitionTable superClassTable;
    private String parentIri;
    private static final WebProtegeClientBundle.ButtonsCss buttonCss = WebProtegeClientBundle.BUNDLE.buttons();


    public LogicalDefinitionTableWrapperImpl(DispatchServiceManager dispatchServiceManager,
                                             ProjectId projectId,
                                             LogicalDefinitionTableConfig.AddAxisValueHandler addAxisValueHandler) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        buttonCss.ensureInjected();
        initWidget(ourUiBinder.createAndBindUi(this));
        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();
        style = LogicalDefinitionResourceBundle.INSTANCE.style();

        this.ancestorDropdown.addChangeHandler(
                changeEvent -> fetchDropdownData(ancestorDropdown.getSelectedValue())
        );

        dispatchServiceManager.execute(
                GetPostcoordinationAxisToGenericScaleAction.create(),
                result -> this.axisToGenericScales = result.getPostcoordinationAxisToGenericScales()
        );
        superClassTable = new LogicalDefinitionTable(new LogicalDefinitionTableConfig("Axis",
                "Value",
                this::initializeTable,
                addAxisValueHandler));

        tableWrapper.add(superClassTable);
        ancestorDropdown.setStyleName(style.logicalDefinitionDropdown());
        superClassLabel.setText("Superclass");
    }

    @Override
    public void setWidget(IsWidget w) {

    }

    @Override
    public void dispose() {

    }

    private void fetchDropdownData(String iri) {
        dispatchServiceManager.execute(GetEntityCustomScalesAction.create(iri, projectId), postcoordination -> {
            this.superclassScalesValue = postcoordination.getWhoficCustomScaleValues();
            superClassTable.setSuperclassScalesValue(this.superclassScalesValue);
        });
        dispatchServiceManager.execute(GetEntityPostCoordinationAction.create(iri, projectId), postcoordination -> {

            this.superclassSpecification = postcoordination.getPostCoordinationSpecification();
            populateAvailableAxisValues();
        });
    }

    private void populateAvailableAxisValues() {
        Optional<PostCoordinationSpecification> mmsSpec = this.superclassSpecification.getPostCoordinationSpecifications()
                .stream().filter(spec -> spec.getLinearizationView().equalsIgnoreCase("http://id.who.int/icd/release/11/mms"))
                .findFirst();

        mmsSpec.ifPresent(postCoordinationSpecification -> superClassTable.setAvailableAxisFromSpec(postCoordinationSpecification, this.labels));
    }


    private void initializeTable(String postCoordinationAxis, LogicalDefinitionTable table) {
        List<String> selectedScales = superclassScalesValue.getScaleCustomizations().stream()
                .filter(s -> s.getPostcoordinationAxis().equalsIgnoreCase(postCoordinationAxis))
                .flatMap(s -> s.getPostcoordinationScaleValues().stream())
                .collect(Collectors.toList());

        if (selectedScales.isEmpty()) {
            List<String> equivalentRoots = axisToGenericScales.stream()
                    .filter(axis -> axis.getPostcoordinationAxis().equalsIgnoreCase(postCoordinationAxis))
                    .map(PostcoordinationAxisToGenericScale::getGenericPostcoordinationScaleTopClass)
                    .collect(Collectors.toList());
            selectedScales.addAll(equivalentRoots);
        }

        Map<String, String> map = new HashMap<>();
        for (String availableAxis : selectedScales) {
            map.put(availableAxis, availableAxis);
        }

        table.setAvailableValues(map);
    }

    @Override
    public void setEntity(OWLEntity owlEntity) {

    }

    @Override
    public void setAncestorList(List<OWLEntityData> ancestorsList) {
        this.ancestorsList = ancestorsList;
        for (OWLEntityData ancestor : ancestorsList) {
            ancestorDropdown.addItem(ancestor.getBrowserText(), ancestor.getIri().toString());
        }
    }

    @Override
    public void setLabels(List<PostCoordinationTableAxisLabel> labels) {
        this.labels = labels;
    }

    @Override
    public void addExistingRows(List<LogicalDefinitionTableRow> rows) {
        this.superClassTable.addExistingRows(rows);
    }

    @Override
    public void setParentIRI(String parentIRI) {
        this.parentIri = parentIRI;
    }

    @Override
    public void enableReadOnly() {
        ancestorDropdown.setEnabled(false);
        ancestorDropdown.setVisible(false);
        if(ancestorSelectedSuperclass.getText() == null || ancestorSelectedSuperclass.getText().isEmpty()) {
            ancestorSelectedSuperclass.setText(ancestorDropdown.getSelectedItemText());
        }
        ancestorSelectedSuperclass.setVisible(true);
        deleteTableWrapper.setEnabled(false);
        deleteTableWrapper.setVisible(false);
        superClassTable.setReadOnly(true);
    }

    @Override
    public void enableEditable() {
        ancestorSelectedSuperclass.setVisible(true);
        ancestorDropdown.setVisible(false);
        ancestorDropdown.setEnabled(false);
        deleteTableWrapper.setEnabled(true);
        deleteTableWrapper.setVisible(true);
        superClassTable.setReadOnly(false);
    }

    @Override
    public void asExistingTable() {
        Optional<OWLEntityData> ancestorOptional = ancestorsList.stream()
                .filter(a -> a.getIri().toString().equalsIgnoreCase(parentIri))
                .findFirst();

        if (ancestorOptional.isPresent()) {
            ancestorSelectedSuperclass.setText(ancestorOptional.get().getBrowserText());
            ancestorSelectedSuperclass.setVisible(true);
            ancestorDropdown.setVisible(false);
            ancestorDropdown.setEnabled(false);

            fetchDropdownData(ancestorOptional.get().getIri().toString());
        }
    }

    @Override
    public void setPostCoordinationTableConfiguration(PostCoordinationTableConfiguration postCoordinationTableConfiguration) {
        this.superClassTable.setPostCoordinationTableConfiguration(postCoordinationTableConfiguration);
    }

    @Override
    public void asNewTable() {
        ancestorSelectedSuperclass.setVisible(false);
        ancestorDropdown.setVisible(true);
        ancestorDropdown.setEnabled(true);

        if (ancestorDropdown.getItemCount() > 1) {
            ancestorDropdown.setItemSelected(1, true);
            fetchDropdownData(ancestorDropdown.getSelectedValue());
        }
    }

    @Override
    public void setRemoveTableHandleWrapper(RemoveTableHandler removeTableHandler) {
        this.deleteTableWrapper.addClickHandler(event -> removeTableHandler.removeTable(this));
    }

    @Override
    public LogicalDefinition getLogicalDefinition() {
        String superClassIri = parentIri != null ? parentIri : ancestorDropdown.getSelectedValue();
        OWLEntityData selectedAncestor = ancestorsList.stream()
                .filter(ancestor -> ancestor.getIri().toString().equalsIgnoreCase(superClassIri))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The selected value is not a known ancestor"));

        return LogicalDefinition.create(OWLClassData.get(selectedAncestor.getEntity().asOWLClass(),
                selectedAncestor.getShortFormsMap()), this.superClassTable.getValues());
    }

    interface LogicalDefinitionTableWrapperImplUiBinder extends UiBinder<HTMLPanel, LogicalDefinitionTableWrapperImpl> {

    }

    public interface RemoveTableHandler {
        void removeTable(LogicalDefinitionTableWrapper wrapper);
    }
}
