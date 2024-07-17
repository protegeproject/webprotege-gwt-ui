package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldView;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
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
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private Optional<List<LinearizationDefinition>> definitionList = Optional.empty();

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
            this.definitionList = Optional.of(result.getDefinitionList());
        });

        logger.info("ALEX din start portlet " + getSelectedEntity());
        handleSetEntity(getSelectedEntity());

    }
    @Override
    protected void handleReloadRequest() {

    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        logger.info("ALEX incerc entitatea " + entityData);

        if(entityData.isPresent()) {
            displayedEntity = entityData;
            logger.info("ALEX afisez entitatea " + entityData);
            dispatch.execute(GetEntityLinearizationAction.create(entityData.get().getIRI(), this.getProjectId()), response -> {
                logger.info("ALEX " + response);
                this.view.dispose();
                this.view.setWhoFicEntity(response.getWhoficEntityLinearizationSpecification());
            });
        }
        else {
            setDisplayedEntity(Optional.empty());
        }
    }
    private void handleSetEntity(Optional<OWLEntity> entity) {
        handleAfterSetEntity(entity);

    }
}
