package edu.stanford.bmir.protege.web.client.card.logicaldefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.selectionModal.HierarchySelectionModalManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.logicaldefinition.*;
import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.icd.*;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.*;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogicalDefinitionCardViewImpl extends Composite implements LogicalDefinitionCardView {

    Logger logger = Logger.getLogger(LogicalDefinitionCardViewImpl.class.getName());

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

    @UiField
    HTMLPanel necessaryConditionsTableWrapper;

    private static final LogicalDefinitionCardViewImplUiBinder ourUiBinder = GWT.create(LogicalDefinitionCardViewImplUiBinder.class);

    private final DispatchServiceManager dispatchServiceManager;

    private final MessageBox messageBox;

    private final ProjectId projectId;

    private List<OWLEntityData> ancestorsList = new ArrayList<>();

    private List<PostCoordinationTableAxisLabel> labels;

    private WhoficCustomScalesValues superclassScalesValue;

    private WhoficEntityPostCoordinationSpecification superclassSpecification;

    private List<PostcoordinationAxisToGenericScale> axisToGenericScales = new ArrayList<>();

    private List<LogicalDefinitionTableWrapper> tableWrappers = new ArrayList<>();

    private LogicalConditions pristineData;

    private final UuidV4Provider uuidV4Provider;

    private OWLEntity currentEntity;

    private OWLEntityData entityData;

    private PostCoordinationTableConfiguration postCoordinationTableConfiguration;


    private final LogicalDefinitionTable necessaryConditionsTable;

    private static final WebProtegeClientBundle.ButtonsCss buttonCss = WebProtegeClientBundle.BUNDLE.buttons();

    private LogicalDefinitionChangeHandler changeHandler;

    private final HierarchySelectionModalManager hierarchyModal;

    private boolean isReadOnly = true;


    @Inject
    public LogicalDefinitionCardViewImpl(@Nonnull DispatchServiceManager dispatchServiceManager,
                                         @Nonnull MessageBox messageBox,
                                         @Nonnull UuidV4Provider uuidV4Provider,
                                         @Nonnull ProjectId projectId, HierarchySelectionModalManager hierarchyModal) {
        this.dispatchServiceManager = dispatchServiceManager;
        this.messageBox = messageBox;
        this.uuidV4Provider = uuidV4Provider;
        this.projectId = projectId;
        this.hierarchyModal = hierarchyModal;
        buttonCss.ensureInjected();
        necessaryConditionsTable = new LogicalDefinitionTable(new LogicalDefinitionTableConfig("Axis",
                "Value",
                this::initializeTable,
                this::handleAxisValueChanged));
        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();

        initWidget(ourUiBinder.createAndBindUi(this));

        dispatchServiceManager.execute(
                GetPostcoordinationAxisToGenericScaleAction.create(),
                result -> this.axisToGenericScales = result.getPostcoordinationAxisToGenericScales()
        );

        this.addDefinitionButton.setStyleName(buttonCss.addButton());

        this.addDefinitionButton.addClickHandler((event -> {
            if (tableWrappers.isEmpty()) {
                clearDefinitions();
            }
            LogicalDefinitionTableWrapper newTable = new LogicalDefinitionTableBuilder(dispatchServiceManager,
                    projectId,
                    this::handleAxisValueChanged)
                    .withLabels(this.labels)
                    .withAncestorsList(this.ancestorsList)
                    .withPostCoordinationTableConfiguration(postCoordinationTableConfiguration)
                    .withRemoveHandler((this::removeWrapper))
                    .asNewTable();
            newTable.setLogicalDefinitionChangeHandler(this.changeHandler);
            this.tableWrappers.add(newTable);
            this.definitions.add(newTable.asWidget());
            this.changeHandler.handleLogicalDefinitionCHange();

        }));

        switchToReadOnly();
        displayPlaceholder();


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
        clearTables();
        clearData();
    }

    @Override
    public void clearTables() {
        this.necessaryConditionsTable.resetTable();
        this.tableWrappers = new ArrayList<>();
        clearDefinitions();
        necessaryConditionsTableWrapper.add(necessaryConditionsTable);
    }

    @Override
    public void clearData() {
        this.pristineData = null;
        this.currentEntity = null;
    }

    @Override
    public void setEntity(OWLEntity owlEntity) {
        if (Objects.equals(this.currentEntity, owlEntity)) {
            return;
        }

        clearData();
        clearTables();
        this.currentEntity = owlEntity;

        dispatchServiceManager.beginBatch();
        dispatchServiceManager.execute(GetEntityCustomScalesAction.create(owlEntity.getIRI().toString(), projectId), postcoordination -> {
            this.superclassScalesValue = postcoordination.getWhoficCustomScaleValues();
            this.necessaryConditionsTable.setSuperclassScalesValue(superclassScalesValue);
        });

        dispatchServiceManager.execute(GetLogicalDefinitionsClassAncestorsAction.create(owlEntity.getIRI(), projectId), getHierarchyParentsResult -> {
            Set<OWLEntityData> result = new HashSet<>();
            populateAncestorsFromTree(getHierarchyParentsResult.getAncestorsTree(), result);
            ancestorsList = result.stream()
                    .filter(ancestor -> !ancestor.getIri().equals(owlEntity.getIRI()))
                    .collect(Collectors.toList());
        });

        dispatchServiceManager.execute(GetPostCoordinationTableConfigurationAction.create(owlEntity.getIRI(), projectId), config -> {
            this.labels = config.getLabels();
            this.postCoordinationTableConfiguration = config.getTableConfiguration();
            necessaryConditionsTable.setPostCoordinationTableConfiguration(postCoordinationTableConfiguration);
            populateWithExistingDefinition(owlEntity, projectId);
            populateAvailableAxisValues(owlEntity);
            this.changeHandler.handleLogicalDefinitionCHange();
        });
        dispatchServiceManager.executeCurrentBatch();
        this.necessaryConditionsTable.setLogicalDefinitionChangeHandler(changeHandler);
    }

    @Override
    public void setEntityData(OWLEntityData entityData) {
        this.entityData = entityData;
    }

    private void populateWithExistingDefinition(OWLEntity owlEntity, ProjectId projectId) {

        dispatchServiceManager.execute(
                GetEntityLogicalDefinitionAction.create(projectId, owlEntity.asOWLClass()),
                (GetEntityLogicalDefinitionResult getEntityLogicalDefinitionResult) -> {
                    if (getEntityLogicalDefinitionResult.getLogicalDefinitions() != null && !getEntityLogicalDefinitionResult.getLogicalDefinitions().isEmpty()) {
                        definitions.getElement().getStyle().setBackgroundImage(null);

                        for (LogicalDefinition logicalDefinition : getEntityLogicalDefinitionResult.getLogicalDefinitions()) {
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
                            newTable.setLogicalDefinitionChangeHandler(this.changeHandler);
                            this.tableWrappers.add(newTable);
                            this.definitions.add(newTable.asWidget());

                            newTable.addExistingRows(superClassTableRows);
                        }

                    } else {
                        displayPlaceholder();
                    }
                    if (getEntityLogicalDefinitionResult.getNecessaryConditions() != null && !getEntityLogicalDefinitionResult.getNecessaryConditions().isEmpty()) {

                        List<LogicalDefinitionTableRow> necessaryConditionsTableRows = getEntityLogicalDefinitionResult.getNecessaryConditions().stream()
                                .map(LogicalDefinitionTableRow::new)
                                .collect(Collectors.toList());

                        necessaryConditionsTable.addExistingRows(necessaryConditionsTableRows);
                    }
                    this.pristineData = getEditedData();
                    this.changeHandler.handleLogicalDefinitionCHange();
                    if(!isReadOnly) {
                        switchToEditable();
                    } else {
                        switchToReadOnly();
                    }
                });
    }

    private void displayPlaceholder() {
        clearDefinitions();
    }

    private void handleAxisValueChanged(String postCoordinationAxis, LogicalDefinitionTable table, WhoficCustomScalesValues superclassScalesValue) {
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

        Set<OWLClass> roots = selectedScales.stream().map(scale -> DataFactory.getOWLClass(IRI.create(scale)))
                .collect(Collectors.toSet());

        hierarchyModal.showModal("Select axis value", roots, table::addNewRow);
        this.changeHandler.handleLogicalDefinitionCHange();
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

    private void populateAvailableAxisValues(OWLEntity owlEntity) {
        dispatchServiceManager.execute(GetEntityPostCoordinationAction.create(owlEntity.getIRI().toString(), projectId), postcoordination -> {

            this.superclassSpecification = postcoordination.getPostCoordinationSpecification();
            if (this.superclassSpecification.getPostCoordinationSpecifications() != null) {
                Optional<PostCoordinationSpecification> mmsSpec = this.superclassSpecification.getPostCoordinationSpecifications()
                        .stream().filter(spec -> spec.getLinearizationView().equalsIgnoreCase("http://id.who.int/icd/release/11/mms"))
                        .findFirst();
                mmsSpec.ifPresent(postCoordinationSpecification -> necessaryConditionsTable.setAvailableAxisFromSpec(postCoordinationSpecification, this.labels));
            }
        });

    }


    @Override
    public void saveValues(String commitMessage) {
        LogicalConditions logCond = getEditedData();

        if (isValid()) {
            this.dispatchServiceManager.execute(
                    UpdateLogicalDefinitionAction.create(
                            ChangeRequestId.get(uuidV4Provider.get()),
                            projectId,
                            this.currentEntity.asOWLClass(),
                            this.pristineData,
                            logCond,

                            "Edited the Logical Definitons and/or Necessary Conditions for " +
                                    this.entityData.getBrowserText() + " : " + commitMessage
                    ),
                    response -> this.pristineData = LogicalConditions.create(
                            new ArrayList<>(logCond.getLogicalDefinitions()),
                            necessaryConditionsTable.getValues())
            );
        } else {
            resetPristineState();
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.isReadOnly = readOnly;
    }

    public void resetPristineState() {
        clearTables();
        if(getEntity() == null) {
            return;
        }
        populateWithExistingDefinition(getEntity(), projectId);
        populateAvailableAxisValues(getEntity());
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
        if (tableWrappers.isEmpty()) {
            displayPlaceholder();
        }
        this.changeHandler.handleLogicalDefinitionCHange();
    }

    private void populateAncestorsFromTree(AncestorClassHierarchy node, Set<OWLEntityData> accumulator) {
        accumulator.add(node.getCurrentNode());
        for (AncestorClassHierarchy child : node.getChildren()) {
            populateAncestorsFromTree(child, accumulator);
        }
    }

    @Override
    public void switchToReadOnly() {
        this.isReadOnly = true;
        toggleButtons(true);
        this.necessaryConditionsTable.setReadOnly(true);
        this.addDefinitionButton.setEnabled(false);
        this.addDefinitionButton.setVisible(false);
        for (LogicalDefinitionTableWrapper wrapper : this.tableWrappers) {
            wrapper.enableReadOnly();
        }
    }

    private void toggleButtons(boolean readOnly) {
        this.addDefinitionButton.setEnabled(!readOnly);
    }


    @Override
    public void switchToEditable() {
        this.isReadOnly = false;
        this.toggleButtons(false);
        this.necessaryConditionsTable.setReadOnly(false);
        this.addDefinitionButton.setEnabled(true);
        this.addDefinitionButton.setVisible(true);
        for (LogicalDefinitionTableWrapper wrapper : this.tableWrappers) {
            wrapper.enableEditable();
        }
    }


    private void setHelpText(HTMLPanel tooltipWrapper, String text) {
        Tooltip helpTooltip = Tooltip.createOnBottom(tooltipWrapper, text);
        helpTooltip.updateTitleContent(text);
    }

    interface LogicalDefinitionCardViewImplUiBinder extends UiBinder<HTMLPanel, LogicalDefinitionCardViewImpl> {

    }

    public LogicalConditions getPristineData() {
        if (pristineData != null) {
            return this.pristineData;
        }

        return LogicalConditions.create(Collections.emptyList(), Collections.emptyList());
    }

    public LogicalConditions getEditedData() {
        List<LogicalDefinition> definitions = this.tableWrappers.stream().map(LogicalDefinitionTableWrapper::getLogicalDefinition).collect(Collectors.toList());
        return LogicalConditions.create(definitions, necessaryConditionsTable.getValues());
    }


    public OWLEntity getEntity() {
        return this.currentEntity;
    }

    @Override
    public void clearDefinitions() {
        this.definitions.forEach(Widget::removeFromParent);
        this.definitions.clear();
    }

    @Override
    public void setLogicalDefinitionChangeHandler(LogicalDefinitionChangeHandler handler) {
        this.changeHandler = handler;
    }

    public boolean isValid() {
        LogicalConditions currState = getEditedData();
        boolean duplicateFound = verifyForDuplicates(currState.getLogicalDefinitions());
        boolean axisWithNoValueFound = currState.getLogicalDefinitions().stream().anyMatch(def -> def.getAxis2filler().isEmpty());
        if (duplicateFound) {
            messageBox.showAlert(
                    "There are several logical definitions with the same superclass, only one is allowed. Please remove the logical definitions that you do not want to keep.",
                    "");
        }
        if (axisWithNoValueFound) {
            String superClassesWithNoValues = currState.getLogicalDefinitions()
                    .stream()
                    .filter(def -> def.getAxis2filler().isEmpty())
                    .map(def -> def.getLogicalDefinitionParent().getBrowserText())
                    .collect(Collectors.joining(","));

            messageBox.showAlert(
                    "Logical definition with superclass " + superClassesWithNoValues + " has no value. Please add values if you wish to save.",
                    "");
        }
        return !axisWithNoValueFound && !duplicateFound;
    }

    @Override
    public boolean isReadOnly() {
        return this.isReadOnly;
    }
}
