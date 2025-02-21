package edu.stanford.bmir.protege.web.client.ontology.id;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
@Portlet(id = "portlets.OntologyId", title = "Ontology Id")
public class OntologyIdPortletPresenter extends AbstractWebProtegePortletPresenter implements HasFixedPrimaryAxisSize {

    private final DispatchServiceManager dispatchServiceManager;

    private OntologyIdView editor;

    @Inject
    public OntologyIdPortletPresenter(SelectionModel selectionModel,
                                      @Nonnull SelectedPathsModel selectedPathsModel,
                                      DispatchServiceManager dispatchServiceManager,
                                      ProjectId projectId,
                                      DisplayNameRenderer displayNameRenderer,
                                      DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.dispatchServiceManager = dispatchServiceManager;
        editor = new OntologyIdViewImpl();
        editor.setEnabled(false);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editor.asWidget());
        updateDisplay();
    }

    @Override
    protected void handleReloadRequest() {
        updateDisplay();
    }

    private void updateDisplay() {
        dispatchServiceManager.execute(GetRootOntologyIdAction.create(getProjectId()),
                                       result -> editor.setValue(result.getOntologyId()));
    }

    @Override
    public int getFixedPrimaryAxisSize() {
        return 80;
    }
}
