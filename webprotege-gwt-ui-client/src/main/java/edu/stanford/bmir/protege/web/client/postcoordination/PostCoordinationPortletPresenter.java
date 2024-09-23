package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.*;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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

    private final Map<String, ScaleValueCardPresenter> scaleValueCardPresenters = new HashMap<>();

    private final Map<String, PostCoordinationTableAxisLabel> tableLabelsForAxes = new HashMap<>();
    private final Map<String, PostCoordinationTableAxisLabel> scaleLabelsForAxes = new HashMap<>();
    private final List<PostCoordinationCompositeAxis> compositeAxisList = new ArrayList<>();
    private final Map<String, PostCoordinationAxisToGenericScale> genericScale = new HashMap<>();


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

        scaleValueCardPresenters.clear();
        tableLabelsForAxes.clear();
        compositeAxisList.clear();
        scaleLabelsForAxes.clear();
        genericScale.clear();


        dispatch.execute(GetPostCoordinationTableConfigurationAction.create("ICD"), result -> {
            for (String availableAxis : result.getTableConfiguration().getPostCoordinationAxes()) {
                PostCoordinationTableAxisLabel existingLabel = result.getLabels().stream()
                        .filter(label -> label.getPostCoordinationAxis().equalsIgnoreCase(availableAxis))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Couldn't find label for " + availableAxis));
                tableLabelsForAxes.put(availableAxis, existingLabel);
            }

            scaleLabelsForAxes.putAll(tableLabelsForAxes);

            view.setLabels(tableLabelsForAxes);


            compositeAxisList.addAll(result.getTableConfiguration().getCompositePostCoordinationAxes());

            compositeAxisList.forEach(compositeAxis ->
                            compositeAxis.getSubAxis()
                                    .forEach(subAxis -> {
                                                PostCoordinationTableAxisLabel existingLabel = result.getLabels().stream()
                                                        .filter(label -> label.getPostCoordinationAxis().equalsIgnoreCase(subAxis))
                                                        .findFirst()
                                                        /*
                                                        ToDo:
                                                            remove the orElseGet() and add back the orElseThrow() when we have proper labels
                                                         */
                                                        .orElseGet(() -> new PostCoordinationTableAxisLabel(subAxis, "hardCodedTableName", "hardCodedTableName"));
//                            .orElseThrow(() -> new RuntimeException("Couldn't find label for " + subAxis));
                                                scaleLabelsForAxes.put(subAxis, existingLabel);
                                                scaleLabelsForAxes.remove(compositeAxis.getPostCoordinationAxis());
                                            }
                                    )
            );

            /*
                ToDo:
                    1. populate genericScale using a dispatch request.
                    2. populate a list of scaleCustomizations from BE with the real scale values for each axis/subaxis
             */
//            Map<String, ScaleValueCardPresenter> axisMapWithValues = tableLabelsForAxes.values()
//                    .stream()
//                    .collect(Collectors.toMap(
//                            PostCoordinationTableAxisLabel::getPostCoordinationAxis,
//                            tabelAxisLabel -> {
//                                PostCoordinationAxisToGenericScale genericScale1 = genericScale.getOrDefault(
//                                        tabelAxisLabel.getPostCoordinationAxis(),
//                                        new PostCoordinationAxisToGenericScale(tabelAxisLabel.getPostCoordinationAxis(), "", ScaleAllowMultiValue.NotAllowed)
//                                );
//                                return createScaleValueCardPresenter(
//                                        tabelAxisLabel,
//                                        new PostCoordinationScaleValue(
//                                                tabelAxisLabel.getPostCoordinationAxis(),
//                                                tabelAxisLabel.getScaleLabel(),
//                                                new ArrayList<>(Arrays.asList("iri1.1", "iri1.2", "iri1.3")),
//                                                genericScale1)
//                                );
//                            }
//                    ));
//
//            scaleValueCardPresenters.putAll(axisMapWithValues);
            //Do batch action for all scaleValues
            dispatch.beginBatch();
            scaleValueCardPresenters.values().forEach(presenter -> presenter.start(view.getScaleValueCardsView()));
            dispatch.executeCurrentBatch();


            view.setTableCellChangedHandler(handleTableCellChanged());

            dispatch.execute(GetLinearizationDefinitionsAction.create(), definitionsResult -> {
                Map<String, LinearizationDefinition> definitionMap = new HashMap<>();
                for (LinearizationDefinition definition : definitionsResult.getDefinitionList()) {
                    definitionMap.put(definition.getWhoficEntityIri(), definition);
                }
                view.setLinearizationDefinitonMap(definitionMap);
                view.initializeTable();
            });
        });
    }

    private ScaleValueCardPresenter createScaleValueCardPresenter(PostCoordinationTableAxisLabel axis, PostCoordinationScaleValue scaleValue) {
        ScaleValueCardView view = new ScaleValueCardViewImpl();
        return new ScaleValueCardPresenter(axis, scaleValue, view, dispatch, getProjectId());
    }

    @Override
    protected void handleReloadRequest() {
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        logger.info("Fac fetch la entity " + entityData);
        entityData.ifPresent(owlEntity -> dispatch.execute(GetEntityPostCoordinationAction.create(owlEntity.getIRI().toString(), getProjectId()),
                (result) -> view.setTableData(result.getPostCoordinationSpecification())));

        entityData.ifPresent(owlEntity -> dispatch.execute(GetEntityCustomScalesAction.create(owlEntity.getIRI().toString(), getProjectId()),
                (result)-> logger.info("ALEX a venit si scales " + result)));
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
        PostCoordinationAxisToGenericScale genericScale1 = genericScale.getOrDefault(
                axisIri,
                /*
                ToDo:
                    return an error here if we don't have a value in genericScale
                 */
                new PostCoordinationAxisToGenericScale(axisIri, "", ScaleAllowMultiValue.NotAllowed)
        );
        ScaleValueCardPresenter newPresenter = createScaleValueCardPresenter(
                currentAxisLabels,
                /*
                ToDo:
                    create a post  PostCoordinationScaleValue also using the scale value from BE if it exists. if not then use createEmpty()
                 */
                PostCoordinationScaleValue.createEmpty(axisIri, currentAxisLabels.getScaleLabel(), genericScale1)
        );
        scaleValueCardPresenters.put(axisIri, newPresenter);
        newPresenter.start(view.getScaleValueCardsView());
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

        if (compositeAxesOptional.isPresent()) {
            return compositeAxesOptional.get()
                    .getSubAxis()
                    .stream()
                    .anyMatch(subAxis -> scaleValueCardPresenters.get(subAxis) != null);
        }
        return scaleValueCardPresenters.get(axisIri) != null;
    }

}
