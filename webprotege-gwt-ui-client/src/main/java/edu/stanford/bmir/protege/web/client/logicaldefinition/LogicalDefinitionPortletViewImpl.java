package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyClassValue;
import edu.stanford.bmir.protege.web.shared.icd.AncestorClassHierarchy;
import edu.stanford.bmir.protege.web.shared.icd.GetClassAncestorsAction;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.GetEntityLogicalDefinitionAction;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.GetEntityLogicalDefinitionResult;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.LogicalDefinition;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.SaveLogicalDefinitionAction;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogicalDefinitionPortletViewImpl extends Composite implements LogicalDefinitionPortletView {

    Logger logger = java.util.logging.Logger.getLogger("LogicalDefinitionPortletViewImpl");

    @UiField
    HTMLPanel paneContainer;

    @UiField
    HTMLPanel superClassContainer;

    @UiField
    HTMLPanel necessaryConditionsContainer;
    @UiField
    ListBox ancestorDropdown;

    @UiField Button editValuesButton;
    @UiField Button cancelButton;

    @UiField Button saveValuesButton;

    private static final LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder ourUiBinder = GWT.create(LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder.class);

    private final DispatchServiceManager dispatchServiceManager;

    private ProjectId projectId;

    private List<OWLEntityData> ancestorsList = new ArrayList<>();

    private List<PostCoordinationTableAxisLabel> labels;

    private WhoficCustomScalesValues superclassScalesValue;

    private WhoficEntityPostCoordinationSpecification superclassSpecification;

    private List<PostcoordinationAxisToGenericScale> axisToGenericScales = new ArrayList<>();
    private LogicalDefinitionResourceBundle.LogicalDefinitionCss style;

    private boolean readOnly = true;


    private final LogicalDefinitionTable superClassTable = new LogicalDefinitionTable(new LogicalDefinitionTableConfig("Logical Definition Axis",
            "Value",
            this::initializeTable));


    private final LogicalDefinitionTable necessaryConditionsTable = new LogicalDefinitionTable(new LogicalDefinitionTableConfig("Necessary Axis",
            "Value",
            this::initializeTable));

    @Inject
    public LogicalDefinitionPortletViewImpl(DispatchServiceManager dispatchServiceManager) {
        this.dispatchServiceManager = dispatchServiceManager;
        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();
        style = LogicalDefinitionResourceBundle.INSTANCE.style();
        initWidget(ourUiBinder.createAndBindUi(this));

        superClassContainer.add(superClassTable);

        necessaryConditionsContainer.add(necessaryConditionsTable);

        dispatchServiceManager.execute(GetPostcoordinationAxisToGenericScaleAction.create(), result -> {
            this.axisToGenericScales = result.getPostcoordinationAxisToGenericScales();
        });

        this.ancestorDropdown.addChangeHandler((changeEvent) -> {
            fetchDropdownData();
        });

        switchToReadOnly();

        ancestorDropdown.setStyleName(style.logicalDefinitionDropdown());
        this.editValuesButton.addClickHandler(event -> switchToEditable());
        this.saveValuesButton.addClickHandler(event -> saveValues());
    }

    private void fetchDropdownData() {
        dispatchServiceManager.execute(GetEntityCustomScalesAction.create(ancestorDropdown.getSelectedValue(), projectId), postcoordination -> {
            this.superclassScalesValue = postcoordination.getWhoficCustomScaleValues();
        });
        dispatchServiceManager.execute(GetEntityPostCoordinationAction.create(ancestorDropdown.getSelectedValue(), projectId), postcoordination -> {

            this.superclassSpecification = postcoordination.getPostCoordinationSpecification();
            populateAvailableAxisValues();
        });
    }


    @Override
    public void setWidget(IsWidget w) {

    }
    @Override
    public void dispose() {

    }

    @Override
    public void setEntity(OWLEntity owlEntity, ProjectId projectId) {
        this.ancestorDropdown.clear();
        this.projectId = projectId;
        this.superClassTable.resetTable();
        this.necessaryConditionsTable.resetTable();
        switchToReadOnly();



        dispatchServiceManager.execute(GetClassAncestorsAction.create(owlEntity.getIRI(), projectId), getHierarchyParentsResult -> {
            Set<OWLEntityData> result = new HashSet<>();
            populateAncestorsFromTree(getHierarchyParentsResult.getAncestorsTree(), result);
            ancestorsList = new ArrayList<>(result);

            for(OWLEntityData ancestor: ancestorsList) {
                ancestorDropdown.addItem(ancestor.getBrowserText(), ancestor.getIri().toString());
            }

            dispatchServiceManager.execute(GetPostCoordinationTableConfigurationAction.create("ICD"), (config) -> {
                this.labels = config.getLabels();
            });

        });

        populateWithExistingDefinition(owlEntity, projectId);

    }

    private void populateWithExistingDefinition(OWLEntity owlEntity, ProjectId projectId) {
        dispatchServiceManager.execute(GetEntityLogicalDefinitionAction.create(projectId, owlEntity.asOWLClass()), (GetEntityLogicalDefinitionResult getEntityLogicalDefinitionResult) -> {
            if(getEntityLogicalDefinitionResult.getLogicalDefinitions() != null && !getEntityLogicalDefinitionResult.getLogicalDefinitions().isEmpty()) {
                LogicalDefinition definition = getEntityLogicalDefinitionResult.getLogicalDefinitions().get(0);
                List<LogicalDefinitionTableRow> superClassTableRows = definition.getAxis2filler().stream()
                        .map(LogicalDefinitionTableRow::new)
                        .collect(Collectors.toList());

                superClassTable.addExistingRows(superClassTableRows);

                for(int i = 0; i < ancestorDropdown.getItemCount(); i++){
                    if(definition.getLogicalDefinitionParent().getIri().toString().equalsIgnoreCase(ancestorDropdown.getValue(i))) {
                        ancestorDropdown.setItemSelected(i, true);
                    }
                }
                fetchDropdownData();
            }
            if(getEntityLogicalDefinitionResult.getNecessaryConditions() != null && !getEntityLogicalDefinitionResult.getNecessaryConditions().isEmpty()) {

                List<LogicalDefinitionTableRow> necessaryConditionsTableRows = getEntityLogicalDefinitionResult.getNecessaryConditions().stream()
                        .map(LogicalDefinitionTableRow::new)
                        .collect(Collectors.toList());

                necessaryConditionsTable.addExistingRows(necessaryConditionsTableRows);
            }
        });
    }

    private void initializeTable(String postCoordinationAxis, LogicalDefinitionTable table) {
        List<String> selectedScales = superclassScalesValue.getScaleCustomizations().stream()
                .filter(s -> s.getPostcoordinationAxis().equalsIgnoreCase(postCoordinationAxis))
                .flatMap(s -> s.getPostcoordinationScaleValues().stream())
                .collect(Collectors.toList());

        if(selectedScales.isEmpty()) {
            List<String> equivalentRoots = axisToGenericScales.stream()
                    .filter(axis -> axis.getPostcoordinationAxis().equalsIgnoreCase(postCoordinationAxis))
                    .map(PostcoordinationAxisToGenericScale::getGenericPostcoordinationScaleTopClass)
                    .collect(Collectors.toList());
            selectedScales.addAll(equivalentRoots);
        }

        Map<String, String> map = new HashMap<>();
        for(String availableAxis : selectedScales) {
            map.put(availableAxis, availableAxis);
        }

        table.setAvailableValues(map);
    }

    private void populateAvailableAxisValues() {
        Optional<PostCoordinationSpecification> mmsSpec = this.superclassSpecification.getPostCoordinationSpecifications()
                .stream().filter(spec -> spec.getLinearizationView().equalsIgnoreCase("http://id.who.int/icd/release/11/mms"))
                .findFirst();


        Map<String, LogicalDefinitionTable.DropdownElement> availableAxis = new HashMap<>();
        if(mmsSpec.isPresent()) {
           mmsSpec.get().getRequiredAxes().stream().sorted((s1, s2) -> getAxisName(s1).compareTo(getAxisName(s2)))
                    .forEach((requiredAxis) -> {
                        availableAxis.put(requiredAxis, new LogicalDefinitionTable.DropdownElement(getAxisName(requiredAxis), style.dropDownMandatory()));
                    });
            mmsSpec.get().getAllowedAxes().stream().sorted((s1, s2) -> getAxisName(s1).compareTo(getAxisName(s2)))
                    .forEach((allowedAxis) -> {
                        availableAxis.put(allowedAxis, new LogicalDefinitionTable.DropdownElement(getAxisName(allowedAxis), style.dropDownAllowed()));
                    });
            mmsSpec.get().getNotAllowedAxes().stream().sorted((s1, s2) -> getAxisName(s1).compareTo(getAxisName(s2)))
                    .forEach((notSetAxis) -> availableAxis.put(notSetAxis, new LogicalDefinitionTable.DropdownElement(getAxisName(notSetAxis), style.dropDownNotSet()))) ;
        }


        
        superClassTable.setAvailableAxis(availableAxis);
        necessaryConditionsTable.setAvailableAxis(availableAxis);
    }

    private void saveValues() {
        OWLEntityData selectedAncestor = ancestorsList.stream()
                .filter(ancestor -> ancestor.getIri().toString().equalsIgnoreCase(ancestorDropdown.getSelectedValue()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The selected value is not a known ancestor"));
        LogicalDefinition definition = LogicalDefinition.create(OWLClassData.get(selectedAncestor.asEntity().get().asOWLClass(),
                        ImmutableMap.of()),
                this.superClassTable.getValues());
        logger.info("ALEX request " + SaveLogicalDefinitionAction.create(Arrays.asList(definition), necessaryConditionsTable.getValues()));
      /*  this.dispatchServiceManager.execute(SaveLogicalDefinitionAction.create(Arrays.asList(definition), necessaryConditionsTable.getValues()), response ->{
            logger.info("ALEX response " + response);
        });*/
    }

    private OWLObjectPropertyData getAxisProperty(LogicalDefinitionTableRow row) {
        return OWLObjectPropertyData.get(new OWLObjectPropertyImpl(IRI.create(row.getPostCoordinationAxis())), ImmutableList.of(), false);
    }

    private String getAxisName(String axisIri) {
        return this.labels.stream()
                .filter(entry -> entry.getPostCoordinationAxis().equalsIgnoreCase(axisIri))
                .map(PostCoordinationTableAxisLabel::getTableLabel).findFirst()
                .orElse("");
    }

    private void populateAncestorsFromTree(AncestorClassHierarchy node, Set<OWLEntityData> accumulator) {
        accumulator.add(node.getCurrentNode());
        for(AncestorClassHierarchy child: node.getChildren()) {
            populateAncestorsFromTree(child, accumulator);
        }
    }
    private void switchToReadOnly() {
        toggleButtons(true);
        this.necessaryConditionsTable.setReadOnly();
        this.superClassTable.setReadOnly();
    }

    private void toggleButtons(boolean readOnly){
        this.readOnly = readOnly;
        this.saveValuesButton.setVisible(!this.readOnly);
        this.cancelButton.setVisible(!this.readOnly);
        this.editValuesButton.setVisible(this.readOnly);
        this.ancestorDropdown.setEnabled(!this.readOnly);
    }

    private void switchToEditable(){
        this.toggleButtons(false);
        this.necessaryConditionsTable.setEditable();
        this.superClassTable.setEditable();
    }

    interface LogicalDefinitionPortletViewImplUiBinder extends UiBinder<HTMLPanel, LogicalDefinitionPortletViewImpl> {

    }
}
