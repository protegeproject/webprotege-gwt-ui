package edu.stanford.bmir.protege.web.client.obo;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.obo.GetOboTermIdAction;
import edu.stanford.bmir.protege.web.shared.obo.OBOTermId;
import edu.stanford.bmir.protege.web.shared.obo.SetOboTermIdAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.EDIT_ONTOLOGY;
import static edu.stanford.bmir.protege.web.shared.obo.GetOboNamespacesAction.getOboNamespaces;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
@Portlet(id = "portlets.obo.TermId", title = "OBO Term Id")
public class OBOTermIdEditorPortletPresenter extends AbstractOBOTermPortletPresenter {

    @Nonnull
    private final OBOTermIdEditor editor;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    @Inject
    public OBOTermIdEditorPortletPresenter(@Nonnull SelectionModel selectionModel,
                                           @Nonnull SelectedPathsModel selectedPathsModel,
                                           @Nonnull ProjectId projectId,
                                           @Nonnull OBOTermIdEditor editor,
                                           @Nonnull DispatchServiceManager dispatchServiceManager,
                                           @Nonnull LoggedInUserProjectCapabilityChecker capabilityChecker,
                                           DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, selectedPathsModel, projectId, displayNameRenderer, dispatchServiceManager);
        this.editor = editor;
        this.dispatch = dispatchServiceManager;
        this.capabilityChecker = capabilityChecker;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        dispatch.execute(getOboNamespaces(getProjectId()),
                         this,
                         result -> {
                             editor.setAvailableNamespaces(result.getNamespaces());
                             portletUi.setWidget(editor);
                             editor.setEnabled(false);
                             capabilityChecker.hasCapability(EDIT_ONTOLOGY, editor::setEnabled);
                         });
    }

    @Override
    protected void clearDisplay() {
        editor.clearValue();
    }

    @Override
    protected void displayEntity(OWLEntity entity) {
        dispatch.execute(GetOboTermIdAction.create(getProjectId(), entity),
                         this,
                         result -> editor.setValue(result.getTermId()));

    }

    @Override
    protected boolean isDirty() {
        GWT.log("Is dirty: " + editor.isDirty());
        return editor.isDirty();
    }

    @Override
    protected void commitChangesForEntity(OWLEntity entity) {
        Optional<OBOTermId> editedTermId = editor.getValue();
        if (editedTermId.isPresent()) {
            dispatch.execute(SetOboTermIdAction.create(getProjectId(), entity, editedTermId.get()),
                             result -> {
                             });
        }
    }

    @Override
    protected String getTitlePrefix() {
        return "OBO Term Id";
    }
}
