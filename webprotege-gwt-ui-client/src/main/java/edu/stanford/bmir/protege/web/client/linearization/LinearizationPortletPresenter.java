package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldView;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.GetRenderedOwlEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.linearization.GetEntityLinearizationAction;
import edu.stanford.bmir.protege.web.shared.linearization.GetLinearizationDefinitionsAction;
import edu.stanford.bmir.protege.web.shared.linearization.GetLinearizationDefinitionsResult;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_OBJECT_COMMENT;


@SuppressWarnings("Convert2MethodRef")
@Portlet(id = "portlets.Linearization",
        title = "Linearization",
        tooltip = "Displays the linearization on the current entity.")
public class LinearizationPortletPresenter extends AbstractWebProtegePortletPresenter {

    Logger logger = java.util.logging.Logger.getLogger("LinearizationPortletPresenter");


    private final LinearizationPortletView view;
    private Optional<PortletUi> portletUi = Optional.empty();
    private Optional<OWLEntity> displayedEntity = Optional.empty();

    private Map<String, LinearizationDefinition> definitionMap = new HashMap<>();

    private Map<String, EntityNode> parentsMap = new HashMap<>();

    private DispatchServiceManager dispatch;

    @Nonnull
    private final HierarchyFieldView hierarchyFieldView;

    @Inject
    public LinearizationPortletPresenter(@Nonnull SelectionModel selectionModel,
                                         @Nonnull ProjectId projectId,
                                         @Nonnull DisplayNameRenderer displayNameRenderer,
                                         @Nonnull DispatchServiceManager dispatch,
                                         @Nonnull HierarchyFieldView hierarchyFieldView,
                                         @Nonnull LinearizationPortletView view
    ) {
        super(selectionModel, projectId, displayNameRenderer, dispatch);
        this.view = view;
        this.hierarchyFieldView = hierarchyFieldView;
        this.dispatch = dispatch;

    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {

        portletUi.setWidget(view.asWidget());
        setDisplaySelectedEntityNameAsSubtitle(true);

        dispatch.execute(GetLinearizationDefinitionsAction.create(), result -> {
            for (LinearizationDefinition definition : result.getDefinitionList()) {
                this.definitionMap.put(definition.getWhoficEntityIri(), definition);
            }
            view.setLinearizationDefinitonMap(this.definitionMap);
            handleSetEntity(getSelectedEntity());
        });

    }

    @Override
    protected void handleReloadRequest() {

    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {

        if (entityData.isPresent()) {
            displayedEntity = entityData;
            dispatch.execute(GetEntityLinearizationAction.create(entityData.get().getIRI().toString(), this.getProjectId()), response -> {

                this.view.dispose();
                if (response.getWhoficEntityLinearizationSpecification() != null &&
                        response.getWhoficEntityLinearizationSpecification().getLinearizationSpecifications() != null) {
                    Set<String> parentsIris = response.getWhoficEntityLinearizationSpecification().getLinearizationSpecifications()
                            .stream()
                            .map(specification -> specification.getLinearizationParent())
                            .filter(iri -> iri != null && !iri.isEmpty())
                            .collect(Collectors.toSet());

                    if (!parentsIris.isEmpty()) {
                        dispatch.execute(GetRenderedOwlEntitiesAction.create(getProjectId(), parentsIris), renderedEntitiesResponse -> {
                            for (EntityNode data : renderedEntitiesResponse.getRenderedEntities()) {
                                this.parentsMap.put(data.getEntity().getIRI().toString(), data);
                            }
                            view.setLinearizationParentsMap(this.parentsMap);
                            view.setWhoFicEntity(response.getWhoficEntityLinearizationSpecification());

                        });
                    } else {
                        view.setWhoFicEntity(response.getWhoficEntityLinearizationSpecification());
                    }
                }


            });
        } else {
            setDisplayedEntity(Optional.empty());
        }
    }

    private void handleSetEntity(Optional<OWLEntity> entity) {
        handleAfterSetEntity(entity);

    }
}
