package edu.stanford.bmir.protege.web.client.postcoordination;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.selectionModal.HierarchySelectionModalManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.*;
import edu.stanford.bmir.protege.web.client.linearization.LinearizationCapabilities;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.*;
import edu.stanford.bmir.protege.web.client.selection.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Portlet(id = "portlets.PostCoordination",
        title = "iCat-X Post-Coordinations",
        tooltip = "Displays the Post-Coordination configuration on the current entity.")
public class PostCoordinationPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final PostCoordinationPortletView view;
    private final Logger logger = Logger.getLogger("PostCoordinationPortletPresenter");

    private final DispatchServiceManager dispatch;

    private final MessageBox messageBox;

    private Map<String, ScaleValueCardPresenter> scaleValueCardPresenters = new LinkedHashMap<>();

    private Map<String, PostCoordinationTableAxisLabel> tableLabelsForAxes = new HashMap<>();
    private Map<String, PostCoordinationTableAxisLabel> scaleLabelsForAxes = new HashMap<>();
    private List<PostCoordinationCompositeAxis> compositeAxisList = new ArrayList<>();
    private Map<String, PostcoordinationAxisToGenericScale> genericScale = new HashMap<>();

    private final List<PostCoordinationCustomScales> postCoordinationCustomScalesList = new ArrayList<>();

    private List<String> scaleCardsOrderByAxis = new LinkedList<>();

    private boolean editMode = false;

    private WebProtegeEventBus eventBus;

    private final HierarchySelectionModalManager hierarchySelectionManager;

    @Inject
    public PostCoordinationPortletPresenter(@Nonnull SelectionModel selectionModel,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull DisplayNameRenderer displayNameRenderer,
                                            @Nonnull DispatchServiceManager dispatch,
                                            @Nonnull PostCoordinationPortletView view,
                                            @Nonnull MessageBox messageBox,
                                            SelectedPathsModel selectedPathsModel,
                                            HierarchySelectionModalManager hierarchySelectionManager) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.view = view;
        this.messageBox = messageBox;
        this.dispatch = dispatch;
        this.hierarchySelectionManager = hierarchySelectionManager;
        this.view.setProjectId(projectId);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.eventBus = eventBus;
        portletUi.setWidget(view.asWidget());
        setDisplaySelectedEntityNameAsSubtitle(true);
        clearAllDate();
        dispatch.beginBatch();

        dispatch.execute(GetPostcoordinationAxisToGenericScaleAction.create(), axisToGenericScaleResult ->
                axisToGenericScaleResult.getPostcoordinationAxisToGenericScales()
                        .forEach(axisToGenericScale ->
                                genericScale.put(axisToGenericScale.getPostcoordinationAxis(), axisToGenericScale)
                        )
        );




        view.setEditButtonHandler(() -> this.setEditMode(true));

        view.setCancelButtonHandler(() -> {
            this.setEditMode(false);
            handleAfterSetEntity(getSelectedEntity());
        });

        view.setSaveButtonHandler(this::saveEntity);
        view.setTableCellChangedHandler(handleTableCellChanged());

        this.setEditMode(false);

        dispatch.executeCurrentBatch();
        handleAfterSetEntity(getSelectedEntity());
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

    private void saveEntity(Optional<WhoficEntityPostCoordinationSpecification> specification) {
        this.setEditMode(false);

        List<PostCoordinationCustomScales> newCustomScales = getUpdateCustomScaleValues();

        if (!newCustomScales.equals(postCoordinationCustomScalesList)) {
            dispatch.execute(SaveEntityCustomScaleAction.create(getProjectId(), WhoficCustomScalesValues.create(getSelectedEntity().get().toStringID(), newCustomScales)), (result) -> {
            });
        }

        specification.ifPresent(whoficEntityPostCoordinationSpecification ->
                dispatch.execute(SaveEntityPostCoordinationAction.create(getProjectId(), whoficEntityPostCoordinationSpecification),
                        (result) -> {
                        }
                )

        );
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
        ScaleValueCardPresenter cardPresenter = new ScaleValueCardPresenter(dispatch, getProjectId(), hierarchySelectionManager);
        cardPresenter.setScaleValue(scaleValue);
        cardPresenter.setPostCoordinationAxis(axis);
        return cardPresenter;
    }

    @Override
    protected void handleReloadRequest() {
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        if (!entityData.isPresent()) {
            setNothingSelectedVisible(true);
            setDisplayedEntity(Optional.empty());
        } else {
            dispatch.execute(GetContextAwareLinearizationDefinitionAction.create(entityData.get().getIRI(),
                    Arrays.asList(LinearizationCapabilities.EDIT_POSTCOORDINATION_LINEARIZATION_ROW,
                    LinearizationCapabilities.VIEW_POSTCOORDINATION_LINEARIZATION_ROW), getProjectId()), definitionsResult -> {
                Map<String, LinearizationDefinition> definitionMap = new HashMap<>();
                for (LinearizationDefinition definition : definitionsResult.getDefinitionList()) {
                    definitionMap.put(definition.getLinearizationUri(), definition);
                }
                dispatch.execute(GetPostCoordinationTableConfigurationAction.create(entityData.get().getIRI(), getProjectId()), result -> {
                    clearAllDate();
                    view.resetTable();
                    view.setLinearizationDefinitonMap(definitionMap);

                    if(result.getTableConfiguration().getPostCoordinationAxes() == null || result.getTableConfiguration().getPostCoordinationAxes().isEmpty()) {
                        setNothingSelectedVisible(true);
                        setDisplayedEntity(Optional.empty());
                    } else {
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
                        if (tableNeedsToBeReset(tableLabels, scaleLabels, compositeAxis, scaleCardsOrder)) {
                            populateAndResetTable(tableLabels, scaleCardsOrder, scaleLabels, compositeAxis, definitionMap);
                        }

                        fetchEntityPostCoordinationAndNavigate(entityData);
                    }

                });

            });
        }

    }

    private void populateAndResetTable(Map<String, PostCoordinationTableAxisLabel> tableLabels,
                                       List<String> scaleCardsOrder,
                                       Map<String, PostCoordinationTableAxisLabel> scaleLabels,
                                       List<PostCoordinationCompositeAxis> compositeAxis,
                                       Map<String, LinearizationDefinition> definitionMap) {
        view.resetTable();
        view.setLinearizationDefinitonMap(definitionMap);
        tableLabelsForAxes = tableLabels;
        scaleLabelsForAxes = scaleLabels;
        compositeAxisList = compositeAxis;
        scaleCardsOrderByAxis = scaleCardsOrder;
        view.setLabels(tableLabelsForAxes);
        view.initializeTable();
    }

    private boolean tableNeedsToBeReset(Map<String, PostCoordinationTableAxisLabel> tableLabels,
                                        Map<String, PostCoordinationTableAxisLabel> scaleLabels,
                                        List<PostCoordinationCompositeAxis> compositeAxis,
                                        List<String> scaleCardsOrder) {
        boolean scaleCardsAreTheSame = scaleCardsOrder.stream().sorted().collect(Collectors.toList())
                .equals(this.scaleCardsOrderByAxis.stream().sorted().collect(Collectors.toList()));

        boolean compositeAxisAreTheSame = compositeAxis.stream().map(PostCoordinationCompositeAxis::getPostCoordinationAxis).sorted().collect(Collectors.toList()).equals(
                this.compositeAxisList.stream().map(PostCoordinationCompositeAxis::getPostCoordinationAxis).sorted().collect(Collectors.toList()));

        boolean scaleLabelsAreTheSame = scaleLabels.equals(this.scaleLabelsForAxes);
        boolean tableLabelsAreTheSame = tableLabels.equals(this.tableLabelsForAxes);
        return !scaleCardsAreTheSame || !compositeAxisAreTheSame || !scaleLabelsAreTheSame || !tableLabelsAreTheSame;
    }


    private void fetchEntityPostCoordinationAndNavigate(Optional<OWLEntity> entityData) {
        dispatch.execute(GetEntityRenderingAction.create(getProjectId(), entityData.get()),
                (result) -> setDisplayedEntity(Optional.of(result.getEntityData())));
        setNothingSelectedVisible(false);
        if (this.editMode) {
            messageBox.showConfirmBox(MessageStyle.ALERT,
                    "Save edits before switching?",
                    "Do you want to save your edits before changing selection?",
                    DialogButton.YES,
                    () -> {
                        saveEntity(view.getTableData());
                        navigateToEntity(entityData.get());
                    },
                    DialogButton.NO,
                    () -> navigateToEntity(entityData.get()),
                    DialogButton.YES);
        } else {
            navigateToEntity(entityData.get());
        }
    }

    private void navigateToEntity(OWLEntity entityData) {
        dispatch.execute(GetEntityCustomScalesAction.create(entityData.getIRI().toString(), getProjectId()),
                (result) -> {
                    logger.info(result.toString());
                    clearScaleValueCards();
                    postCoordinationCustomScalesList.addAll(result.getWhoficCustomScaleValues().getScaleCustomizations());
                });

        dispatch.execute(GetEntityPostCoordinationAction.create(entityData.getIRI().toString(), getProjectId()),
                (result) -> {
                    view.setTableData(result.getPostCoordinationSpecification());
                    setEditMode(false);
                });
    }

    private void clearScaleValueCards() {
        scaleValueCardPresenters.clear();
        postCoordinationCustomScalesList.clear();
        view.getScaleValueCardsView().clear();
    }

    private void clearAllDate() {
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
        newPresenter.start(eventBus, editMode);
        updateScaleValueCards();
    }

    private void updateScaleValueCards() {
        Map<String, ScaleValueCardPresenter> orderedScaleValueCardPresenters = getOrderedScaleValueCardPresenters();
        scaleValueCardPresenters.clear();
        scaleValueCardPresenters.putAll(orderedScaleValueCardPresenters);

        scaleValueCardPresenters.values().forEach(scaleValueCardPresenter -> view.getScaleValueCardsView().add(scaleValueCardPresenter.getView().asWidget()));
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

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        scaleValueCardPresenters.values().forEach(presenter -> presenter.setEditMode(editMode));
        view.setEditMode(editMode);
    }

}
