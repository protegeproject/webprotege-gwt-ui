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

    private final Map<String, PostCoordinationTableAxisLabel> labels = new HashMap<>();


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
        labels.clear();


        dispatch.execute(GetPostCoordinationTableConfigurationAction.create("ICD"), result -> {
            for (String availableAxis : result.getTableConfiguration().getPostCoordinationAxes()) {
                PostCoordinationTableAxisLabel existingLabel = result.getLabels().stream()
                        .filter(label -> label.getPostCoordinationAxis().equalsIgnoreCase(availableAxis))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Couldn't find label for " + availableAxis));
                labels.put(availableAxis, existingLabel);
            }
            view.setLabels(labels);

            Map<String, ScaleValueCardPresenter> axisMapWithValues = labels.values()
                    .stream()
                    .collect(Collectors.toMap(
                            PostCoordinationTableAxisLabel::getPostCoordinationAxis, // Key mapper
                            tabelAxisLabel -> createScaleValueCardPresenter(            // Value mapper
                                    tabelAxisLabel,
                                    new PostCoordinationScaleValue(
                                            tabelAxisLabel.getPostCoordinationAxis(),
                                            tabelAxisLabel.getScaleLabel(),
                                            new ArrayList<>(Arrays.asList("iri1.1", "iri1.2", "iri1.3"))
                                    )
                            )
                    ));

            scaleValueCardPresenters.putAll(axisMapWithValues);
            //Do batch action for all scaleValues
            dispatch.beginBatch();
            scaleValueCardPresenters.values().forEach(presenter -> presenter.start(view.getScaleValueCardsView()));
            dispatch.executeCurrentBatch();


            dispatch.execute(GetLinearizationDefinitionsAction.create(), definitionsResult -> {
                Map<String, LinearizationDefinition> definitionMap = new HashMap<>();
                for (LinearizationDefinition definition : definitionsResult.getDefinitionList()) {
                    definitionMap.put(definition.getWhoficEntityIri(), definition);
                }
                view.setLinearizationDefinitonMap(definitionMap);
                view.setPostCoordinationEntity();
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
    }

    public void removeScaleValueCardPresenter(String axisIri) {
        ScaleValueCardPresenter presenter = scaleValueCardPresenters.get(axisIri);
        view.getScaleValueCardsView().remove(presenter.getView().asWidget());
        scaleValueCardPresenters.remove(axisIri);
    }

    private void addScaleValueCardPresenter(String axisIri) {
        PostCoordinationTableAxisLabel currentAxisLabels = labels.get(axisIri);
        ScaleValueCardPresenter newPresenter = createScaleValueCardPresenter(currentAxisLabels, PostCoordinationScaleValue.createEmpty(axisIri, currentAxisLabels.getScaleLabel()));
        scaleValueCardPresenters.put(axisIri, newPresenter);
        newPresenter.start(view.getScaleValueCardsView());
    }
}
