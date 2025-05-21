package edu.stanford.bmir.protege.web.client.card.postcoordination;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.event.shared.*;
import edu.stanford.bmir.protege.web.client.card.*;
import edu.stanford.bmir.protege.web.client.card.linearization.LinearizationCardPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.selectionModal.HierarchySelectionModalManager;
import edu.stanford.bmir.protege.web.client.linearization.LinearizationCapabilities;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.*;
import edu.stanford.bmir.protege.web.client.ui.*;
import edu.stanford.bmir.protege.web.shared.*;
import edu.stanford.bmir.protege.web.shared.access.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Card;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;

@Card(id = "postcoordination.card")
public class PostcoordinationCardPresenter implements CustomContentEntityCardPresenter, EntityCardEditorPresenter {

    private static final Logger logger = Logger.getLogger(LinearizationCardPresenter.class.getName());

    private final PostcoordinationCardView view;
    private final DispatchServiceManager dispatch;

    private final ProjectId projectId;
    private WebProtegeEventBus eventBus;

    private Map<String, ScaleValueCardPresenter> scaleValueCardPresenters = new LinkedHashMap<>();

    private Map<String, PostCoordinationTableAxisLabel> tableLabelsForAxes = new HashMap<>();
    private Map<String, PostCoordinationTableAxisLabel> scaleLabelsForAxes = new HashMap<>();
    private List<PostCoordinationCompositeAxis> compositeAxisList = new ArrayList<>();
    private Map<String, PostcoordinationAxisToGenericScale> genericScale = new HashMap<>();
    private final List<PostCoordinationCustomScales> postCoordinationCustomScalesList = new ArrayList<>();

    private Map<String, LinearizationDefinition> definitionMap = new HashMap<>();
    private List<String> scaleCardsOrderByAxis = new LinkedList<>();
    private Optional<OWLEntity> selectedEntity;
    private HandlerManager handlerManager = new HandlerManager(this);

    private boolean editMode = false;

    private final HierarchySelectionModalManager hierarchySelectionManager;


    private final DisplayContextManager displayContextManager = new DisplayContextManager(context -> {
    });

    private ImmutableSet<Capability> capabilities = ImmutableSet.of();

    private boolean canEditScaleValues;
    private boolean canViewScaleValues;

    @Inject
    @AutoFactory
    public PostcoordinationCardPresenter(PostcoordinationCardView view,
                                         DispatchServiceManager dispatch,
                                         ProjectId projectid,
                                         HierarchySelectionModalManager hierarchySelectionManager) {
        this.view = view;
        this.dispatch = dispatch;
        this.projectId = projectid;
        this.hierarchySelectionManager = hierarchySelectionManager;
    }

    @Override
    public void start(EntityCardUi ui, WebProtegeEventBus eventBus) {
        try {
            this.eventBus = eventBus;
            clearEverything();
            selectedEntity = Optional.empty();
            view.setTableCellChangedHandler(handleTableCellChanged());
            this.setReadOnlyMode();
            ui.setWidget(view);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error ", e);
        }
    }

    @Override
    public void requestFocus() {
        logger.info("In request focus");
    }

    @Override
    public void stop() {
        logger.info("In stops");

    }

    @Override
    public void setEntity(OWLEntity entity) {
        this.setReadOnlyMode();
        this.selectedEntity = Optional.of(entity);
        loadEntity();
    }

    @Override
    public void clearEntity() {
        this.selectedEntity = Optional.empty();
        handlerManager.fireEvent(new DirtyChangedEvent());
    }

    @Override
    public void setCapabilities(ImmutableSet<Capability> capabilities) {
        this.capabilities = capabilities;
        canEditScaleValues = CardCapabilityChecker.hasCapability(ContextAwareBuiltInCapability.EDIT_POSTCOORDINATION_SCALE_VALUES.getCapability(), capabilities);
        canViewScaleValues = canEditScaleValues || CardCapabilityChecker.hasCapability(ContextAwareBuiltInCapability.VIEW_POSTCOORDINATION_SCALE_VALUES.getCapability(), capabilities);
    }

    @Override
    public void beginEditing() {
        this.setEditableMode();
    }

    @Override
    public void cancelEditing() {
        setReadOnlyMode();
        loadEntity();
    }

    @Override
    public void finishEditing(String commitMessage) {
        this.setReadOnlyMode();

        List<PostCoordinationCustomScales> newCustomScales = getUpdateCustomScaleValues();
        Optional<WhoficEntityPostCoordinationSpecification> specification = view.getTableData();
        dispatch.execute(SaveEntityCustomScaleAction.create(projectId, WhoficCustomScalesValues.create(selectedEntity.get().toStringID(), newCustomScales)), (result) -> {
        });
        specification.ifPresent(spec -> dispatch.execute(
                        SaveEntityPostCoordinationAction.create(projectId, spec),
                        (result) -> {
                            loadEntity();
                            fireEvent(new DirtyChangedEvent());
                        }
                )
        );

    }

    @Override
    public boolean isDirty() {
        if (!this.editMode) {
            return false;
        }
        List<PostCoordinationCustomScales> newCustomScales = getUpdateCustomScaleValues();
        Optional<WhoficEntityPostCoordinationSpecification> specification = view.getTableData();

        List<PostCoordinationCustomScales> newCustomScalesList =
                newCustomScales.stream()
                        .map(coord -> PostCoordinationCustomScales.create(coord.getPostcoordinationScaleValues().stream().sorted().collect(Collectors.toList()), coord.getPostcoordinationAxis()))
                        .sorted(Comparator.comparing(PostCoordinationCustomScales::getPostcoordinationAxis))
                        .collect(Collectors.toList());

        List<PostCoordinationCustomScales> existingScalesList = postCoordinationCustomScalesList.stream()
                .map(coord -> PostCoordinationCustomScales.create(coord.getPostcoordinationScaleValues().stream().sorted().collect(Collectors.toList()), coord.getPostcoordinationAxis()))
                .sorted(Comparator.comparing(PostCoordinationCustomScales::getPostcoordinationAxis))
                .collect(Collectors.toList());

        return !newCustomScalesList.equals(existingScalesList) || specification.isPresent();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return handlerManager.addHandler(DirtyChangedEvent.TYPE, handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }


    private List<String> createOrderAxisListWithSubAxis(List<String> postCoordinationAxes, List<PostCoordinationCompositeAxis> compositeAxisList) {
        List<String> orderedAxisList = new LinkedList<>(postCoordinationAxes);

        compositeAxisList.forEach(compositeAxis ->
                {
                    int indexForCurrAxis = orderedAxisList.indexOf(compositeAxis.getPostCoordinationAxis());
                    List<String> subAxisList = new LinkedList<>(compositeAxis.getSubAxis());
                    orderedAxisList.addAll(indexForCurrAxis + 1, subAxisList);
                    orderedAxisList.remove(indexForCurrAxis);
                }
        );
        return orderedAxisList;
    }

    private List<PostCoordinationCustomScales> getUpdateCustomScaleValues() {
        Collection<ScaleValueCardPresenter> scaleValueCardPresenters1 = scaleValueCardPresenters.values();
        return scaleValueCardPresenters1
                .stream()
                .map(scalePresenter -> {
                    List<String> scaleValueIris = scalePresenter.getValues().getValueIris()
                            .stream()
                            .map(ScaleValueIriAndName::getScaleValueIri)
                            .collect(Collectors.toList());

                    return PostCoordinationCustomScales.create(scaleValueIris, scalePresenter.getValues().getAxisIri());
                })
                .collect(Collectors.toList());
    }


    private ScaleValueCardPresenter createScaleValueCardPresenter(PostCoordinationTableAxisLabel axis, PostcoordinationScaleValue scaleValue) {
        ScaleValueCardPresenter cardPresenter = new ScaleValueCardPresenter(dispatch, projectId, hierarchySelectionManager);
        cardPresenter.setScaleValue(scaleValue);
        cardPresenter.setPostCoordinationAxis(axis);
        cardPresenter.setHandleChange(() -> {
            logger.info("Emitting dirty changed event");
            handlerManager.fireEvent(new DirtyChangedEvent());

        });
        return cardPresenter;
    }


    protected void loadEntity() {
        if (!this.selectedEntity.isPresent()) {
            logger.log(Level.INFO, "No entity to display");
        } else {
            dispatch.execute(GetPostCoordinationTableConfigurationAction.create(this.selectedEntity.get().getIRI(), projectId), result -> {
                if (result.getTableConfiguration().getPostCoordinationAxes() != null && !result.getTableConfiguration().getPostCoordinationAxes().isEmpty()) {
                    try {
                        Map<String, PostCoordinationTableAxisLabel> tableLabels = new HashMap<>();
                        List<String> scaleCardsOrder = new LinkedList<>();

                        for (String availableAxis : result.getTableConfiguration().getPostCoordinationAxes()) {
                            PostCoordinationTableAxisLabel existingLabel = result.getLabels().stream()
                                    .filter(label -> label.getPostCoordinationAxis().equalsIgnoreCase(availableAxis))
                                    .findFirst()
                                    .orElseThrow(() -> {
                                        logger.info("Couldn't find label for " + availableAxis);
                                        return new RuntimeException("Couldn't find label for " + availableAxis);
                                    });
                            tableLabels.put(availableAxis, existingLabel);
                            scaleCardsOrder.add(availableAxis);
                        }


                        Map<String, PostCoordinationTableAxisLabel> scaleLabels = new HashMap<>(tableLabels);

                        List<PostCoordinationCompositeAxis> compositeAxis = new ArrayList<>(result.getTableConfiguration().getCompositePostCoordinationAxes());

                        compositeAxis.forEach(axis ->
                                axis.getSubAxis()
                                        .forEach(subAxis -> {
                                                    PostCoordinationTableAxisLabel existingLabel = result.getLabels().stream()
                                                            .filter(label -> label.getPostCoordinationAxis().equalsIgnoreCase(subAxis))
                                                            .findFirst()
                                                            .orElseGet(() -> new PostCoordinationTableAxisLabel(subAxis, "hardCodedTableName", "hardCodedTableName"));
                                                    scaleLabels.put(subAxis, existingLabel);
                                                    scaleLabels.remove(axis.getPostCoordinationAxis());
                                                }
                                        )
                        );

                        List<String> orderedAxisListWithSubAxis = this.createOrderAxisListWithSubAxis(result.getTableConfiguration().getPostCoordinationAxes(),
                                result.getTableConfiguration().getCompositePostCoordinationAxes());

                        scaleCardsOrder.addAll(orderedAxisListWithSubAxis);
                        dispatch.execute(GetContextAwareLinearizationDefinitionAction.create(selectedEntity.get().getIRI(),
                                        Arrays.asList(LinearizationCapabilities.EDIT_POSTCOORDINATION_LINEARIZATION_ROW, LinearizationCapabilities.VIEW_POSTCOORDINATION_LINEARIZATION_ROW),
                                        projectId)
                                , definitionsResult -> {
                                    Map<String, LinearizationDefinition> definitionMap = new HashMap<>();
                                    for (LinearizationDefinition definition : definitionsResult.getDefinitionList()) {
                                        definitionMap.put(definition.getLinearizationUri(), definition);
                                    }
                                    if (tableNeedsToBeReset(tableLabels, scaleLabels, compositeAxis, scaleCardsOrder, definitionMap)) {
                                        populateAndResetTable(tableLabels, scaleCardsOrder, scaleLabels, compositeAxis,definitionMap);
                                    }
                                });

                        navigateToEntity(this.selectedEntity.get());
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error ", e);
                    }
                } else {
                    logger.log(Level.INFO, "No postcoordination axes to display for the entity type");
                }

            });
        }

    }

    private void populateAndResetTable(Map<String, PostCoordinationTableAxisLabel> tableLabels,
                                       List<String> scaleCardsOrder,
                                       Map<String, PostCoordinationTableAxisLabel> scaleLabels,
                                       List<PostCoordinationCompositeAxis> compositeAxis,
                                       Map<String, LinearizationDefinition> definitionMap) {
        view.resetTable();
        tableLabelsForAxes = tableLabels;
        scaleLabelsForAxes = scaleLabels;
        compositeAxisList = compositeAxis;
        this.definitionMap = definitionMap;
        scaleCardsOrderByAxis = scaleCardsOrder;
        view.setLabels(tableLabelsForAxes);
        dispatch.execute(GetPostcoordinationAxisToGenericScaleAction.create(), axisToGenericScaleResult -> {
                    axisToGenericScaleResult.getPostcoordinationAxisToGenericScales()
                            .forEach(axisToGenericScale ->
                                    genericScale.put(axisToGenericScale.getPostcoordinationAxis(), axisToGenericScale)
                            );
                    view.setLinearizationDefinitonMap(definitionMap);
                    view.initializeTable();
                }
        );
    }

    private boolean tableNeedsToBeReset(Map<String, PostCoordinationTableAxisLabel> tableLabels,
                                        Map<String, PostCoordinationTableAxisLabel> scaleLabels,
                                        List<PostCoordinationCompositeAxis> compositeAxis,
                                        List<String> scaleCardsOrder,
                                        Map<String, LinearizationDefinition> definitionMap) {
        boolean scaleCardsAreTheSame = scaleCardsOrder.stream().sorted().collect(Collectors.toList())
                .equals(this.scaleCardsOrderByAxis.stream().sorted().collect(Collectors.toList()));

        boolean compositeAxisAreTheSame = compositeAxis.stream().map(PostCoordinationCompositeAxis::getPostCoordinationAxis).sorted().collect(Collectors.toList()).equals(
                this.compositeAxisList.stream().map(PostCoordinationCompositeAxis::getPostCoordinationAxis).sorted().collect(Collectors.toList()));

        boolean scaleLabelsAreTheSame = scaleLabels.equals(this.scaleLabelsForAxes);
        boolean tableLabelsAreTheSame = tableLabels.equals(this.tableLabelsForAxes);

        boolean definitionMapKeysAreTheSame = this.definitionMap.keySet().equals(definitionMap.keySet());

        boolean definitionsAreTheSame = this.definitionMap.values().equals(definitionMap.values());

        return !scaleCardsAreTheSame || !compositeAxisAreTheSame || !scaleLabelsAreTheSame || !tableLabelsAreTheSame || !definitionMapKeysAreTheSame || !definitionsAreTheSame;
    }

    private void navigateToEntity(OWLEntity entityData) {
        dispatch.execute(GetEntityCustomScalesAction.create(entityData.getIRI().toString(), projectId),
                (result) -> {
                    clearScaleValueCards();
                    postCoordinationCustomScalesList.addAll(result.getWhoficCustomScaleValues().getScaleCustomizations());
                });

        dispatch.execute(GetEntityPostCoordinationAction.create(entityData.getIRI().toString(), projectId),
                (result) -> {
                    view.setTableData(result.getPostCoordinationSpecification());
                    setReadOnlyMode();
                });
    }

    private void clearScaleValueCards() {
        scaleValueCardPresenters.clear();
        postCoordinationCustomScalesList.clear();
        view.getScaleValueCardsView().clear();
    }

    private void clearEverything() {
        clearScaleValueCards();
        tableLabelsForAxes.clear();
        compositeAxisList.clear();
        scaleLabelsForAxes.clear();
        genericScale.clear();
    }

    public void removeScaleValueCardPresenter(String axisIri) {
        ScaleValueCardPresenter presenter = scaleValueCardPresenters.get(axisIri);
        if (presenter != null) {
            view.getScaleValueCardsView().remove(presenter.getView().asWidget());
        }
        scaleValueCardPresenters.remove(axisIri);
    }

    private void addScaleValueCardPresenter(String axisIri) {
        PostCoordinationTableAxisLabel currentAxisLabels = scaleLabelsForAxes.get(axisIri);
        PostcoordinationAxisToGenericScale genericScale1 = genericScale.getOrDefault(
                axisIri,
                /*
                ToDo:
                    return an error here if we don't have a value in genericScale
                 */
                PostcoordinationAxisToGenericScale.create(axisIri, "", "NotAllowed")
        );
        List<ScaleValueIriAndName> existingScaleValueForAxis = postCoordinationCustomScalesList.stream().filter(customScaleValue -> customScaleValue.getPostcoordinationAxis().equals(axisIri))
                .flatMap(customScaleValue -> customScaleValue.getPostcoordinationScaleValues().stream())
                .map(ScaleValueIriAndName::create)
                .collect(Collectors.toList());
        ScaleValueCardPresenter newPresenter = createScaleValueCardPresenter(
                currentAxisLabels,
                PostcoordinationScaleValue.create(axisIri, currentAxisLabels.getScaleLabel(), existingScaleValueForAxis, genericScale1)
        );
        scaleValueCardPresenters.put(axisIri, newPresenter);

        newPresenter.start(eventBus, canEditScaleValues && editMode);
        updateScaleValueCards();
    }

    private void updateScaleValueCards() {
        Map<String, ScaleValueCardPresenter> orderedScaleValueCardPresenters = getOrderedScaleValueCardPresenters();
        scaleValueCardPresenters.clear();
        scaleValueCardPresenters.putAll(orderedScaleValueCardPresenters);

        scaleValueCardPresenters.values()
                .forEach(
                        scaleValueCardPresenter -> view.getScaleValueCardsView().add(scaleValueCardPresenter.getView().asWidget())
                );
    }

    private Map<String, ScaleValueCardPresenter> getOrderedScaleValueCardPresenters() {
        Map<String, ScaleValueCardPresenter> orderedScaleValueCardPresenters = new LinkedHashMap<>();

        for (String key : scaleCardsOrderByAxis) {
            if (scaleValueCardPresenters.containsKey(key)) {
                orderedScaleValueCardPresenters.put(key, scaleValueCardPresenters.get(key));
            }
        }

        return orderedScaleValueCardPresenters;
    }

    private TableCellChangedHandler handleTableCellChanged() {
        return (isAxisEnabledOnAnyRow, checkboxValue, tableAxisIri) -> {
            if(!canViewScaleValues){
                return;
            }
            boolean presenterExists = isScaleValuePresenterCreated(tableAxisIri);
            if ((checkboxValue.getValue().equals("ALLOWED") ||
                    checkboxValue.getValue().equals("REQUIRED")) &&
                    !presenterExists
            ) {
                if (isCompositeAxis(tableAxisIri)) {
                    List<PostCoordinationCompositeAxis> compositeAxes = compositeAxisList.stream()
                            .filter(compositeAxis -> compositeAxis.getPostCoordinationAxis().equals(tableAxisIri))
                            .collect(Collectors.toList());

                    compositeAxes.forEach(compositeAxis ->
                            compositeAxis.getSubAxis()
                                    .forEach(this::addScaleValueCardPresenter)
                    );
                } else {
                    addScaleValueCardPresenter(tableAxisIri);
                }
            } else if (!isAxisEnabledOnAnyRow &&
                    (!checkboxValue.getValue().equals("ALLOWED") ||
                            !checkboxValue.getValue().equals("REQUIRED"))) {
                if (isCompositeAxis(tableAxisIri)) {
                    List<PostCoordinationCompositeAxis> compositeAxes = compositeAxisList.stream()
                            .filter(compositeAxis -> compositeAxis.getPostCoordinationAxis().equals(tableAxisIri))
                            .collect(Collectors.toList());

                    compositeAxes.forEach(compositeAxis ->
                            compositeAxis.getSubAxis()
                                    .forEach(this::removeScaleValueCardPresenter));
                } else {
                    removeScaleValueCardPresenter(tableAxisIri);
                }
            }
            handlerManager.fireEvent(new DirtyChangedEvent());
        };
    }

    private boolean isCompositeAxis(String tableAxisIri) {
        return compositeAxisList.stream()
                .anyMatch(compositeAxis -> compositeAxis.getPostCoordinationAxis().equals(tableAxisIri));
    }

    private boolean isScaleValuePresenterCreated(String axisIri) {
        Optional<PostCoordinationCompositeAxis> compositeAxesOptional = compositeAxisList.stream()
                .filter(compositeAxis -> compositeAxis.getPostCoordinationAxis().equals(axisIri))
                .findFirst();

        return compositeAxesOptional.map(
                        postCoordinationCompositeAxis -> postCoordinationCompositeAxis
                                .getSubAxis()
                                .stream()
                                .anyMatch(subAxis -> scaleValueCardPresenters.get(subAxis) != null))
                .orElseGet(() -> scaleValueCardPresenters.get(axisIri) != null);
    }

    public void setEditableMode() {
        this.editMode = true;
        if (canEditScaleValues) {
            scaleValueCardPresenters.values().forEach(presenter -> presenter.setEditMode(true));
        }
        view.setEditableState();
    }

    public void setReadOnlyMode() {
        this.editMode = false;
        scaleValueCardPresenters.values().forEach(presenter -> presenter.setEditMode(false));
        view.setReadOnlyState();
    }

    @Override
    public DisplayContextBuilder fillDisplayContextBuilder() {
        return displayContextManager.fillDisplayContextBuilder();
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        displayContextManager.setParentDisplayContextBuilder(parent);
    }
}
