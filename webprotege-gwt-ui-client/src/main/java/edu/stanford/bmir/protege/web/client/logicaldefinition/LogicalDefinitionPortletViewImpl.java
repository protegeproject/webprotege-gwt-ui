package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenterFactory;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.icd.AncestorClassHierarchy;
import edu.stanford.bmir.protege.web.shared.icd.GetClassAncestorsAction;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.*;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogicalDefinitionPortletViewImpl extends Composite implements LogicalDefinitionPortletView {

    Logger logger = java.util.logging.Logger.getLogger("LogicalDefinitionPortletViewImpl");

    @UiField
    HTMLPanel paneContainer;

    @UiField
    Button addDefinitionButton;

    @UiField
    HTMLPanel definitions;

    @UiField
    HTMLPanel necessaryConditionsContainer;

    @UiField
    HTMLPanel logicalDefinitionsTooltip;

    @UiField
    HTMLPanel necessaryConditionsTooltip;

    @UiField Button editValuesButton;
    @UiField Button cancelButton;

    @UiField Button saveValuesButton;

    private static final LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder ourUiBinder = GWT.create(LogicalDefinitionPortletViewImpl.LogicalDefinitionPortletViewImplUiBinder.class);

    private final DispatchServiceManager dispatchServiceManager;

    private final MessageBox messageBox;

    private ProjectId projectId;

    private LogicalDefinitionModal logicalDefinitionModal;

    private List<OWLEntityData> ancestorsList = new ArrayList<>();

    private List<PostCoordinationTableAxisLabel> labels;

    private WhoficCustomScalesValues superclassScalesValue;

    private WhoficEntityPostCoordinationSpecification superclassSpecification;

    private List<PostcoordinationAxisToGenericScale> axisToGenericScales = new ArrayList<>();
    private LogicalDefinitionResourceBundle.LogicalDefinitionCss style;

    private List<LogicalDefinitionTableWrapper> tableWrappers = new ArrayList<>();

    private LogicalConditions pristineData;

    private UuidV4Provider uuidV4Provider;

    private OWLEntity currentEntity;

    private OWLEntityData entityData;

    private PostCoordinationTableConfiguration postCoordinationTableConfiguration;

    private final HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory;

    private final LogicalDefinitionTable necessaryConditionsTable;

    private static final WebProtegeClientBundle.ButtonsCss buttonCss = WebProtegeClientBundle.BUNDLE.buttons();


    @Inject
    public LogicalDefinitionPortletViewImpl(@Nonnull DispatchServiceManager dispatchServiceManager,
                                            @Nonnull MessageBox messageBox,
                                            @Nonnull HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory,
                                            @Nonnull UuidV4Provider uuidV4Provider,
                                            @Nonnull LogicalDefinitionModal logicalDefinitionModal) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.messageBox = messageBox;
        this.uuidV4Provider = uuidV4Provider;
        this.hierarchyPopupPresenterFactory = hierarchyPopupPresenterFactory;
        this.logicalDefinitionModal = logicalDefinitionModal;
        buttonCss.ensureInjected();
        necessaryConditionsTable = new LogicalDefinitionTable(new LogicalDefinitionTableConfig("Necessary Axis",
                "Value",
                this::initializeTable,
                this::handleAxisValueChanged));
        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();
        style = LogicalDefinitionResourceBundle.INSTANCE.style();

        initWidget(ourUiBinder.createAndBindUi(this));

        dispatchServiceManager.execute(GetPostcoordinationAxisToGenericScaleAction.create(), result -> {
            this.axisToGenericScales = result.getPostcoordinationAxisToGenericScales();
        });

        this.addDefinitionButton.setStyleName(buttonCss.addButton());

        this.addDefinitionButton.addClickHandler((event -> {
            if(tableWrappers.isEmpty()) {
                definitions.clear();
            }
            LogicalDefinitionTableWrapper newTable = new LogicalDefinitionTableBuilder(dispatchServiceManager,
                    projectId,
                    this::handleAxisValueChanged)
                    .withLabels(this.labels)
                    .withAncestorsList(this.ancestorsList)
                    .withRemoveHandler((this::removeWrapper))
                    .asNewTable();

            this.tableWrappers.add(newTable);
            this.definitions.add(newTable.asWidget());

        }));

        switchToReadOnly();
        this.editValuesButton.addClickHandler(event -> switchToEditable());
        this.saveValuesButton.addClickHandler(event -> saveValues());
        displayPlaceholder();

        cancelButton.addClickHandler((event) -> {
            this.setEntity(this.currentEntity, this.projectId);
        });

        this.setHelpText(this.logicalDefinitionsTooltip, "A Logical Definition provides a way to formally define the meaning of a precoordinated entity by " +
                "specifying a parent entity with combinations of postcoordination axes with their corresponding values.");
        this.setHelpText(this.necessaryConditionsTooltip, "A Necessary Condition provides a way to formally describe the things that are " +
                "always necessarily true about an entity by assigning values to postcoordination axes.");

    }


    @Override
    public void setWidget(IsWidget w) {

    }
    @Override
    public void dispose() {

    }

    @Override
    public void setEntity(OWLEntity owlEntity, ProjectId projectId) {
        this.projectId = projectId;
        this.necessaryConditionsTable.resetTable();
        this.tableWrappers = new ArrayList<>();
        this.definitions.clear();
        this.currentEntity = owlEntity;

        necessaryConditionsContainer.add(necessaryConditionsTable);


        dispatchServiceManager.execute(GetEntityCustomScalesAction.create(owlEntity.getIRI().toString(), projectId), postcoordination -> {
            this.superclassScalesValue = postcoordination.getWhoficCustomScaleValues();
            this.necessaryConditionsTable.setSuperclassScalesValue(superclassScalesValue);
        });

        dispatchServiceManager.execute(GetClassAncestorsAction.create(owlEntity.getIRI(), projectId), getHierarchyParentsResult -> {
            Set<OWLEntityData> result = new HashSet<>();
            populateAncestorsFromTree(getHierarchyParentsResult.getAncestorsTree(), result);
            ancestorsList = new ArrayList<>(result);

            dispatchServiceManager.execute(GetPostCoordinationTableConfigurationAction.create("ICD"), config -> {
                this.labels = config.getLabels();
                this.postCoordinationTableConfiguration = config.getTableConfiguration();
                necessaryConditionsTable.setPostCoordinationTableConfiguration(postCoordinationTableConfiguration);
                populateWithExistingDefinition(owlEntity, projectId);
                populateAvailableAxisValues(owlEntity);
            });
        });

        switchToReadOnly();
    }

    @Override
    public void setEntityData(OWLEntityData entityData) {
        this.entityData =entityData;
    }

    private void populateWithExistingDefinition(OWLEntity owlEntity, ProjectId projectId) {
        dispatchServiceManager.execute(GetEntityLogicalDefinitionAction.create(projectId, owlEntity.asOWLClass()), (GetEntityLogicalDefinitionResult getEntityLogicalDefinitionResult) -> {
            this.pristineData = LogicalConditions.create(getEntityLogicalDefinitionResult.getLogicalDefinitions(), getEntityLogicalDefinitionResult.getNecessaryConditions());
            if(getEntityLogicalDefinitionResult.getLogicalDefinitions() != null && !getEntityLogicalDefinitionResult.getLogicalDefinitions().isEmpty()) {
                definitions.getElement().getStyle().setBackgroundImage(null);

                for(LogicalDefinition logicalDefinition : getEntityLogicalDefinitionResult.getLogicalDefinitions()) {
                    List<LogicalDefinitionTableRow> superClassTableRows = logicalDefinition.getAxis2filler().stream()
                            .map(LogicalDefinitionTableRow::new)
                            .collect(Collectors.toList());

                    LogicalDefinitionTableWrapper newTable = new LogicalDefinitionTableBuilder(dispatchServiceManager,
                            projectId,
                            this::handleAxisValueChanged)
                            .withLabels(this.labels)
                            .withAncestorsList(this.ancestorsList)
                            .withPostCoordinationTableConfiguration(postCoordinationTableConfiguration)
                            .withParentIri(logicalDefinition.getLogicalDefinitionParent().getIri().toString())
                            .withRemoveHandler((this::removeWrapper))
                            .asExistingTable();

                    this.tableWrappers.add(newTable);
                    this.definitions.add(newTable.asWidget());

                    newTable.addExistingRows(superClassTableRows);
                }

            } else {
                displayPlaceholder();
            }
            if(getEntityLogicalDefinitionResult.getNecessaryConditions() != null && !getEntityLogicalDefinitionResult.getNecessaryConditions().isEmpty()) {

                List<LogicalDefinitionTableRow> necessaryConditionsTableRows = getEntityLogicalDefinitionResult.getNecessaryConditions().stream()
                        .map(LogicalDefinitionTableRow::new)
                        .collect(Collectors.toList());

                necessaryConditionsTable.addExistingRows(necessaryConditionsTableRows);
            }
        });
    }

    private void displayPlaceholder() {
        definitions.clear();
    }

    private void handleAxisValueChanged(String postCoordinationAxis, LogicalDefinitionTable table, WhoficCustomScalesValues superclassScalesValue) {
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

        Set<OWLClass> roots = selectedScales.stream().map(scale -> new OWLClassImpl(IRI.create(scale)))
                .collect(Collectors.toSet());

        logicalDefinitionModal.showModal(roots, (entityNode) -> {
            table.addNewRow(entityNode);
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

    private void populateAvailableAxisValues(OWLEntity owlEntity) {
        dispatchServiceManager.execute(GetEntityPostCoordinationAction.create(owlEntity.getIRI().toString(), projectId), postcoordination -> {

            this.superclassSpecification = postcoordination.getPostCoordinationSpecification();
            if(this.superclassSpecification.getPostCoordinationSpecifications() != null) {
                Optional<PostCoordinationSpecification> mmsSpec = this.superclassSpecification.getPostCoordinationSpecifications()
                        .stream().filter( spec -> spec.getLinearizationView().equalsIgnoreCase("http://id.who.int/icd/release/11/mms"))
                        .findFirst();
                mmsSpec.ifPresent(postCoordinationSpecification -> necessaryConditionsTable.setAvailableAxisFromSpec(postCoordinationSpecification, this.labels));
            }
        });

    }

    private void saveValues() {
       List<LogicalDefinition> definitions = this.tableWrappers.stream().map(LogicalDefinitionTableWrapper::getLogicalDefinition).collect(Collectors.toList());

       boolean hasDuplicates = verifyForDuplicates(definitions);

       if(!hasDuplicates) {
           this.dispatchServiceManager.execute(UpdateLogicalDefinitionAction.create(ChangeRequestId.get(uuidV4Provider.get()),
                   projectId,
                   this.currentEntity.asOWLClass(),
                   this.pristineData,
                   LogicalConditions.create(definitions, necessaryConditionsTable.getValues()),
                   "Edited the Logical Definitons and/or Necessary Conditions for " + this.entityData.getBrowserText()
           ), response ->{
               switchToReadOnly();
           });
       } else {
           messageBox.showAlert(
                   "There are several logical definitions with the same superclass, only one is allowed. Please remove the logical definitions that you do not want to keep.",
                   "");
       }

    }

    private boolean verifyForDuplicates(List<LogicalDefinition> definitions) {
        Set<String> parentIRISet = new HashSet<>();
        for (LogicalDefinition obj : definitions) {
            String parentIRI = obj.getLogicalDefinitionParent().getIri().toString();
            if (parentIRISet.contains(parentIRI)) {
                return true;
            } else {
                parentIRISet.add(parentIRI);
            }
        }
        return false;
    }

    private void removeWrapper(LogicalDefinitionTableWrapper wrapper) {
        this.tableWrappers.remove(wrapper);
        this.definitions.remove(wrapper.asWidget());
        if(tableWrappers.isEmpty()){
            displayPlaceholder();
        }
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
        for(LogicalDefinitionTableWrapper wrapper : this.tableWrappers) {
            wrapper.enableReadOnly();
        }
    }

    private void toggleButtons(boolean readOnly){
        this.saveValuesButton.setVisible(!readOnly);
        this.cancelButton.setVisible(!readOnly);
        this.editValuesButton.setVisible(readOnly);
        this.addDefinitionButton.setEnabled(!readOnly);
    }

    private void switchToEditable(){
        this.toggleButtons(false);
        this.necessaryConditionsTable.setEditable();
        for(LogicalDefinitionTableWrapper wrapper : this.tableWrappers) {
            wrapper.enableEditable();
        }
    }

    private void setHelpText(HTMLPanel tooltipWrapper, String text) {
        Tooltip helpTooltip = Tooltip.createOnBottom(tooltipWrapper, text);
        helpTooltip.updateTitleContent(text);
    }

    interface LogicalDefinitionPortletViewImplUiBinder extends UiBinder<HTMLPanel, LogicalDefinitionPortletViewImpl> {

    }
}
