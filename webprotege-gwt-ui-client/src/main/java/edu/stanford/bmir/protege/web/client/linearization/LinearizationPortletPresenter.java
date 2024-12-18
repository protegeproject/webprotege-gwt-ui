package edu.stanford.bmir.protege.web.client.linearization;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.*;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;


@SuppressWarnings("Convert2MethodRef")
@Portlet(id = "portlets.Linearization",
        title = "iCat-X Linearizations",
        tooltip = "Displays the linearizations on the current entity.")
public class LinearizationPortletPresenter extends AbstractWebProtegePortletPresenter {


    private final LinearizationPortletView view;

    private Map<String, LinearizationDefinition> definitionMap = new HashMap<>();

    private Map<String, String> entityParentsMap = new HashMap<>();

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
        if (!view.isReadOnly()) {
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
            dispatch.execute(GetEntityRenderingAction.create(getProjectId(), entityData.get()),
                    (result) -> setDisplayedEntity(Optional.of(result.getEntityData())));
            this.entityParentsMap.clear();
            dispatch.execute(GetEntityLinearizationAction.create(entityData.get().getIRI().toString(), this.getProjectId()), response -> {

                this.view.dispose();
                if (response.getWhoficEntityLinearizationSpecification() != null &&
                        response.getWhoficEntityLinearizationSpecification().getLinearizationSpecifications() != null) {

                    dispatch.execute(GetHierarchyParentsAction.create(getProjectId(), entityData.get(), ClassHierarchyDescriptor.get()), result -> {
                        if (result.getParents() != null) {
                            result.getParents().forEach(parent -> this.entityParentsMap.put(parent.getEntity().toStringID(), parent.getBrowserText()));
                        }
                        view.dispose();
                        view.setEntityParentsMap(this.entityParentsMap);
                        view.setWhoFicEntity(response.getWhoficEntityLinearizationSpecification());
                    });
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
