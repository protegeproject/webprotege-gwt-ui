package edu.stanford.bmir.protege.web.client.linearization;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageStyle;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.*;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;


@SuppressWarnings("Convert2MethodRef")
@Portlet(id = "portlets.Linearization",
        title = "iCat-X Linearizations",
        tooltip = "Displays the linearizations on the current entity.")
public class LinearizationPortletPresenter extends AbstractWebProtegePortletPresenter {


    private final LinearizationPortletView view;

    private Map<String, LinearizationDefinition> definitionMap = new HashMap<>();

    private Map<String, EntityNode> parentsMap = new HashMap<>();

    private DispatchServiceManager dispatch;
    private final EventBus eventBus;

    private final LoggedInUserManager loggedInUserManager;


    private MessageBox messageBox;

    @Inject
    public LinearizationPortletPresenter(@Nonnull SelectionModel selectionModel,
                                         @Nonnull ProjectId projectId,
                                         @Nonnull DisplayNameRenderer displayNameRenderer,
                                         @Nonnull DispatchServiceManager dispatch,
                                         @Nonnull LinearizationPortletView view,
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
        view.setLinearizationChangeEventHandler(() -> triggerLinearizationChangeEvent());

        dispatch.execute(GetLinearizationDefinitionsAction.create(), result -> {
            for (LinearizationDefinition definition : result.getDefinitionList()) {
                this.definitionMap.put(definition.getWhoficEntityIri(), definition);
            }
            view.setLinearizationDefinitonMap(this.definitionMap);
            handleSetEntity(getSelectedEntity());
        });

    }

    /*
    ToDo:
        Need to rethink arhitecture of linearization view and history so we no longer do the complete hack that is below for the history refresh button.
     */
    private void triggerLinearizationChangeEvent() {
        eventBus.fireEvent(
                new ProjectChangedEvent(
                        getProjectId(),
                        new RevisionSummary(
                                RevisionNumber.valueOf("0"),
                                loggedInUserManager.getLoggedInUserId(),
                                new Date().getTime(),
                                0,
                                ""),
                        Collections.emptySet()
                ).asGWTEvent()
        );
    }

    @Override
    protected void handleReloadRequest() {

    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        if(!view.isReadOnly()){
            messageBox.showConfirmBox(MessageStyle.ALERT,
                    "Save edits before switching?",
                    "Do you want to save your edits before changing selection?",
                    DialogButton.NO,
                    () -> navigateToEntity(entityData),
                    DialogButton.YES,
                    () -> {
                        view.saveValues();
                        navigateToEntity(entityData);
                    },
                    DialogButton.YES);
        } else {
            navigateToEntity(entityData);
        }

    }

    private void navigateToEntity(Optional<OWLEntity> entityData) {
        if (entityData.isPresent()) {
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
                            view.dispose();
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
