package edu.stanford.bmir.protege.web.client.obo;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermSynonymsAction;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermSynonymsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.EDIT_ONTOLOGY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
@Portlet(id = "portlets.obo.TermSynonyms", title = "OBO Term Synonyms")
public class OBOTermSynonymsPortletPresenter extends AbstractOBOTermPortletPresenter {

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final OBOTermSynonymListEditor editor;

    @Nonnull
    private final IsWidget editorHolder;

    @Nonnull
    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    @Inject
    public OBOTermSynonymsPortletPresenter(@Nonnull SelectionModel selectionModel,
                                           @Nonnull SelectedPathsModel selectedPathsModel,
                                           @Nonnull ProjectId projectId,
                                           @Nonnull DispatchServiceManager dispatch,
                                           @Nonnull OBOTermSynonymListEditor editor,
                                           @Nonnull LoggedInUserProjectCapabilityChecker capabilityChecker, DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, selectedPathsModel, projectId, displayNameRenderer, dispatch);
        this.dispatch = dispatch;
        this.editor = editor;
        this.editorHolder = new SimplePanel(editor);
        this.capabilityChecker = capabilityChecker;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editorHolder);
        editor.setEnabled(false);
        capabilityChecker.hasCapability(EDIT_ONTOLOGY, perm -> editor.setEnabled(perm));
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        dispatch.execute(GetOboTermSynonymsAction.create(getProjectId(), entity),
                         this,
                         result -> editor.setValue(result.getSynonyms()));
    }

    @Override
    protected boolean isDirty() {
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        editor.getValue().ifPresent(synonyms -> {
            dispatch.execute(SetOboTermSynonymsAction.create(getProjectId(), entity, synonyms),
                             result -> {});
        });
    }

    @Override
    protected String getTitlePrefix() {
        return "Synonyms";
    }

}
