package edu.stanford.bmir.protege.web.client.logicaldefinition;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

@Portlet(id = "portlets.LogicalDefinition",
        title = "iCat-X Logical Definition",
        tooltip = "Displays the existing logical definitions on the current entity.")
public class LogicalDefinitionPortletPresenter extends AbstractWebProtegePortletPresenter {


    private LogicalDefinitionPortletView view;

    @Inject
    public LogicalDefinitionPortletPresenter(@Nonnull SelectionModel selectionModel,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull DisplayNameRenderer displayNameRenderer,
                                             @Nonnull DispatchServiceManager dispatch,
                                             @Nonnull LogicalDefinitionPortletView view) {
        super(selectionModel, projectId, displayNameRenderer, dispatch);
        this.view = view;
    }

    @Override
    public void startPortlet(PortletUi portletUi,
                             WebProtegeEventBus eventBus) {
        portletUi.setWidget(view.asWidget());

    }

    @Override
    protected void handleReloadRequest() {

    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        entityData.ifPresent(owlEntity -> view.setEntity(owlEntity, getProjectId()));
    }
}