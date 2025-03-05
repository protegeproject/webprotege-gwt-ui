package edu.stanford.bmir.protege.web.client.renderer;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingResult;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent.CLASS_FRAME_CHANGED;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */

@Portlet(id = "portlets.owl.EntityDescriptionBrowser", title = "OWL Entity Description Browser")
public class OWLEntityDescriptionBrowserPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final HTML html;

    private final DispatchServiceManager dsm;

    @Inject
    public OWLEntityDescriptionBrowserPortletPresenter(SelectionModel selectionModel,
                                                       @Nonnull SelectedPathsModel selectedPathsModel,
                                                       EventBus eventBus,
                                                       DispatchServiceManager dsm,
                                                       ProjectId projectId,
                                                       DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer, dsm, selectedPathsModel);
        this.dsm = dsm;
        html = new HTML();
    }

    @Override
    public void startPortlet(PortletUi portletUi,
                             WebProtegeEventBus eventBus) {
        portletUi.setWidget(new ScrollPanel(html));
        eventBus.addProjectEventHandler(getProjectId(), CLASS_FRAME_CHANGED, event -> handleEntityChange(event.getEntity()));
        eventBus.addProjectEventHandler(getProjectId(), ObjectPropertyFrameChangedEvent.TYPE, event -> handleEntityChange(event.getEntity()));
        eventBus.addProjectEventHandler(getProjectId(), DataPropertyFrameChangedEvent.TYPE, (DataPropertyFrameChangedEventHandler) event -> handleEntityChange(event.getEntity()));
        eventBus.addProjectEventHandler(getProjectId(), AnnotationPropertyFrameChangedEvent.TYPE, event -> handleEntityChange(event.getEntity()));
        eventBus.addProjectEventHandler(getProjectId(), NamedIndividualFrameChangedEvent.NAMED_INDIVIDUAL_CHANGED, event -> handleEntityChange(event.getEntity()));
        eventBus.addProjectEventHandler(getProjectId(), AnnotationPropertyFrameChangedEvent.TYPE, event -> handleEntityChange(event.getEntity()));
        setDisplaySelectedEntityNameAsSubtitle(true);
        handleAfterSetEntity(getSelectedEntity());
    }

    private void handleEntityChange(OWLEntity entity) {
        if(Optional.of(entity).equals(getSelectedEntity())) {
            handleAfterSetEntity(getSelectedEntity());
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        entity.ifPresent(owlEntity -> dsm.execute(GetEntityHtmlRenderingAction.create(getProjectId(),
                                                                                      owlEntity),
                                                  this,
                                                  this::handleRenderingResult));
    }

    @Override
    protected void handleReloadRequest() {
        handleAfterSetEntity(getSelectedEntity());
    }

    private void handleRenderingResult(GetEntityHtmlRenderingResult result) {
        setDisplayedEntity(Optional.of(result.getEntityData()));
        html.setHTML(result.getRendering());
    }

}
