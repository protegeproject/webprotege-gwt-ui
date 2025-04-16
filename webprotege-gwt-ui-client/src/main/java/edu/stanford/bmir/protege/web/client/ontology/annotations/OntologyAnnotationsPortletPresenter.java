package edu.stanford.bmir.protege.web.client.ontology.annotations;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.access.BuiltInCapability;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.SetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.shared.event.OntologyFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.EDIT_ONTOLOGY_ANNOTATIONS;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 05/07/2013
 */
@Portlet(id = "portlets.OntologyAnnotations", title = "Ontology Annotations")
public class OntologyAnnotationsPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final AnnotationsView annotationsView;

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    private Optional<List<PropertyAnnotationValue>> lastSet = Optional.empty();

    private Optional<OWLOntologyID> currentOntologyId = Optional.empty();

    @Inject
    public OntologyAnnotationsPortletPresenter(AnnotationsView annotationsView,
                                               SelectionModel selectionModel,
                                               @Nonnull SelectedPathsModel selectedPathsModel,
                                               DispatchServiceManager dispatchServiceManager,
                                               ProjectId projectId,
                                               LoggedInUserProjectCapabilityChecker capabilityChecker,
                                               DisplayNameRenderer displayNameRenderer,
                                               DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.annotationsView = annotationsView;
        this.dispatchServiceManager = dispatchServiceManager;
        this.capabilityChecker = capabilityChecker;
        annotationsView.addValueChangeHandler(event -> handleOntologyAnnotationsChanged());
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(new ScrollPanel(annotationsView.asWidget()));

        eventBus.addProjectEventHandler(getProjectId(),
                OntologyFrameChangedEvent.TYPE, event -> updateView());
        eventBus.addProjectEventHandler(getProjectId(),
                PermissionsChangedEvent.ON_CAPABILITIES_CHANGED,
                event -> updateState());
        capabilityChecker.hasCapability(BuiltInCapability.VIEW_PROJECT, permission -> {
            portletUi.setForbiddenVisible(!permission);
            if (permission) {
                updateState();
                updateView();
            }
        });
    }

    @Override
    protected void handleReloadRequest() {

    }

    private void updateView() {
        dispatchServiceManager.execute(GetOntologyAnnotationsAction.create(getProjectId(), Optional.empty()),
                result -> {
                    LinkedHashSet<PropertyAnnotationValue> object = new LinkedHashSet<>(result.getAnnotations());
                    if (!lastSet.isPresent() || !annotationsView.getValue().equals(Optional.of(object))) {
                        lastSet = Optional.of(new ArrayList<>(object));
                        annotationsView.setValue(object);
                    }
                    currentOntologyId = Optional.of(result.getOntologyId());
                    GWT.log("[OntologyAnnotationsPortletPresenter] Current ontology: " + currentOntologyId);
                });
    }


    private void updateState() {
        annotationsView.setEnabled(false);
        capabilityChecker.hasCapability(EDIT_ONTOLOGY_ANNOTATIONS, annotationsView::setEnabled);
    }

    private void handleOntologyAnnotationsChanged() {
        GWT.log("[OntologyAnnotationsPortletPresenter] Ontology annotations changed");
        if (!annotationsView.isDirty()) {
            GWT.log("[OntologyAnnotationsPortletPresenter] Ontology annotations are not dirty");
            return;
        }
        if (!annotationsView.isWellFormed()) {
            GWT.log("[OntologyAnnotationsPortletPresenter] Ontology annotations are not dirty");
            return;
        }
        Optional<Set<PropertyAnnotationValue>> annotations = annotationsView.getValue();
        if (annotations.isPresent() && lastSet.isPresent() && currentOntologyId.isPresent()) {
            dispatchServiceManager.execute(SetOntologyAnnotationsAction.create(getProjectId(), currentOntologyId.get(), new HashSet<>(lastSet.get()), annotations.get()),
                    result -> {
                    });
        }
    }
}
