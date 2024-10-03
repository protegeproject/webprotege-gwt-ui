package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.*;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.*;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
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
    private final EventBus eventBus;

    private final LoggedInUserManager loggedInUserManager;
    private final MessageBox messageBox;

    private final Map<String, ScaleValueCardPresenter> scaleValueCardPresenters = new LinkedHashMap<>();

    private final Map<String, PostCoordinationTableAxisLabel> tableLabelsForAxes = new HashMap<>();
    private final Map<String, PostCoordinationTableAxisLabel> scaleLabelsForAxes = new HashMap<>();
    private final List<PostCoordinationCompositeAxis> compositeAxisList = new ArrayList<>();
    private final Map<String, PostcoordinationAxisToGenericScale> genericScale = new HashMap<>();

    private final List<PostCoordinationCustomScales> postCoordinationCustomScalesList = new ArrayList<>();

    private final List<String> scaleCardsOrderByAxis = new LinkedList<>();

    private boolean editMode = false;

    @Inject
    public PostCoordinationPortletPresenter(@Nonnull SelectionModel selectionModel,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull DisplayNameRenderer displayNameRenderer,
                                            @Nonnull DispatchServiceManager dispatch,
                                            @Nonnull PostCoordinationPortletView view,
                                            @Nonnull EventBus eventBus,
                                            @Nonnull MessageBox messageBox,
                                            @Nonnull LoggedInUserManager loggedInUserManager) {
        super(selectionModel, projectId, displayNameRenderer, dispatch);
        this.view = view;
        this.messageBox = messageBox;
        this.dispatch = dispatch;
        this.eventBus = eventBus;
        this.loggedInUserManager = loggedInUserManager;
        this.view.setProjectId(projectId);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(view.asWidget());
        setDisplaySelectedEntityNameAsSubtitle(true);

        clearAllDate();

        dispatch.beginBatch();
        dispatch.execute(GetPostCoordinationTableConfigurationAction.create("ICD"), result -> {
            for (String availableAxis : result.getTableConfiguration().getPostCoordinationAxes()) {
                PostCoordinationTableAxisLabel existingLabel = result.getLabels().stream()
                        .filter(label -> label.getPostCoordinationAxis().equalsIgnoreCase(availableAxis))
                        .findFirst()
                        .orElseThrow(() -> {
                            logger.info("Couldn't find label for " + availableAxis);
                            return new RuntimeException("Couldn't find label for " + availableAxis);
                        });
                tableLabelsForAxes.put(availableAxis, existingLabel);
                scaleCardsOrderByAxis.add(availableAxis);
            }

            scaleLabelsForAxes.putAll(tableLabelsForAxes);

            view.setLabels(tableLabelsForAxes);

            dispatch.execute(GetPostcoordinationAxisToGenericScaleAction.create(), axisToGenericScaleResult ->
                    axisToGenericScaleResult.getPostcoordinationAxisToGenericScales()
                            .forEach(axisToGenericScale ->
                                    genericScale.put(axisToGenericScale.getPostcoordinationAxis(), axisToGenericScale)
                            )
            );


            compositeAxisList.addAll(result.getTableConfiguration().getCompositePostCoordinationAxes());

            compositeAxisList.forEach(compositeAxis ->
                            compositeAxis.getSubAxis()
                                    .forEach(subAxis -> {
                                                PostCoordinationTableAxisLabel existingLabel = result.getLabels().stream()
                                                        .filter(label -> label.getPostCoordinationAxis().equalsIgnoreCase(subAxis))
                                                        .findFirst()
//                                                .orElseThrow(() -> {
//                                                    logger.log(Level.SEVERE, "Couldn't find label for " + subAxis);
//                                                    return new RuntimeException("Couldn't find label for " + subAxis);
//                                                });
                                                        /*
                        ToDo:
                            remove the orElseGet() and add back the orElseThrow() when we have proper labels
                         */
                                                        .orElseGet(() -> new PostCoordinationTableAxisLabel(subAxis, "hardCodedTableName", "hardCodedTableName"));
                                                scaleLabelsForAxes.put(subAxis, existingLabel);
                                                scaleLabelsForAxes.remove(compositeAxis.getPostCoordinationAxis());
                                            }
                                    )
            );

            List<String> orderedAxisListWithSubAxis = this.createOrderAxisListWithSubAxis(result.getTableConfiguration().getPostCoordinationAxes(), result.getTableConfiguration().getCompositePostCoordinationAxes());

            scaleCardsOrderByAxis.addAll(orderedAxisListWithSubAxis);

            view.setTableCellChangedHandler(handleTableCellChanged());

            dispatch.execute(GetLinearizationDefinitionsAction.create(), definitionsResult -> {
                Map<String, LinearizationDefinition> definitionMap = new HashMap<>();
                for (LinearizationDefinition definition : definitionsResult.getDefinitionList()) {
                    definitionMap.put(definition.getWhoficEntityIri(), definition);
                }
                view.setLinearizationDefinitonMap(definitionMap);
                view.initializeTable();
                handleAfterSetEntity(getSelectedEntity());
            });
        });

        view.setEditButtonHandler(() -> this.setEditMode(true));

        view.setCancelButtonHandler(() -> {
            this.setEditMode(false);
            handleAfterSetEntity(getSelectedEntity());
        });

        view.setSaveButtonHandler(this::saveEntity);

        this.setEditMode(false);

        dispatch.executeCurrentBatch();
    }

    //The corect order is determined by the order of the values that are stored in the database
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

        specification.ifPresent(whoficEntityPostCoordinationSpecification ->
                dispatch.execute(SaveEntityPostCoordinationAction.create(getProjectId(), whoficEntityPostCoordinationSpecification),
                        (result) -> {
                        }
                )

        );
    }

    private ScaleValueCardPresenter createScaleValueCardPresenter(PostCoordinationTableAxisLabel axis, PostcoordinationScaleValue scaleValue) {
        ScaleValueCardView view = new ScaleValueCardViewImpl();
        return new ScaleValueCardPresenter(axis, scaleValue, view, dispatch, getProjectId());
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


    }

    private void navigateToEntity(OWLEntity entityData) {
        dispatch.execute(GetEntityCustomScalesAction.create(entityData.getIRI().toString(), getProjectId()),
                (result) -> postCoordinationCustomScalesList.addAll(result.getWhoficCustomScaleValues().getScaleCustomizations()));

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
        List<String> existingScaleValueForAxis = postCoordinationCustomScalesList.stream().filter(customScaleValue -> customScaleValue.getPostcoordinationAxis().equals(axisIri))
                .flatMap(customScaleValue -> customScaleValue.getPostcoordinationScaleValues().stream())
                .collect(Collectors.toList());
        ScaleValueCardPresenter newPresenter = createScaleValueCardPresenter(
                currentAxisLabels,
                PostcoordinationScaleValue.create(axisIri, currentAxisLabels.getScaleLabel(), existingScaleValueForAxis, genericScale1)
        );
        scaleValueCardPresenters.put(axisIri, newPresenter);
        newPresenter.start(editMode);
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
