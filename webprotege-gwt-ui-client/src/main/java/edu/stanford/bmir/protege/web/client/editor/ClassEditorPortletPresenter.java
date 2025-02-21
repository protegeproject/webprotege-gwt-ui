package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.model.EntityType.CLASS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2017
 */
@Portlet(
        id = "portlets.ClassEditor",
        title = "Class",
        tooltip = "Provides an editor that allows class descriptions to be edited"
)
public class ClassEditorPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final EditorPortletPresenter editorPresenter;

    @Inject
    public ClassEditorPortletPresenter(@Nonnull SelectionModel selectionModel,
                                       @Nonnull SelectedPathsModel selectedPathsModel,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull EditorPortletPresenter editorPresenter,
                                       DisplayNameRenderer displayNameRenderer,
                                       DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.editorPresenter = checkNotNull(editorPresenter);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        editorPresenter.setDisplayedTypes(CLASS);
        editorPresenter.start(portletUi, eventBus);
    }

    @Override
    protected void handleReloadRequest() {
        editorPresenter.handleReloadRequest();
    }

    @Override
    public void dispose() {
        editorPresenter.dispose();
        super.dispose();
    }
}
