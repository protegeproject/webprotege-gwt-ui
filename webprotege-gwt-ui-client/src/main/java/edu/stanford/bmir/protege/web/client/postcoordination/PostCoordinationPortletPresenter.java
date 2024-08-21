package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserManager;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.postcoordination.GetPostCoordinationTableConfigurationAction;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;


@Portlet(id = "portlets.PostCoordination",
        title = "iCat-X Post-Coordinations",
        tooltip = "Displays the Post-Coordination configuration on the current entity.")
public class PostCoordinationPortletPresenter extends AbstractWebProtegePortletPresenter  {

        private final PostCoordinationPortletView view;
        Logger logger = java.util.logging.Logger.getLogger("PostCoordinationPortletPresenter");

        private DispatchServiceManager dispatch;
        private final EventBus eventBus;

        private final LoggedInUserManager loggedInUserManager;


        private MessageBox messageBox;

        @Inject
        public PostCoordinationPortletPresenter(@Nonnull SelectionModel selectionModel,
                                                @Nonnull ProjectId projectId,
                                                @Nonnull DisplayNameRenderer displayNameRenderer,
                                                @Nonnull DispatchServiceManager dispatch,
                                                @Nonnull PostCoordinationPortletView view,
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
            portletUi.setWidget(view.asWidget());
            setDisplaySelectedEntityNameAsSubtitle(true);

            dispatch.execute(GetPostCoordinationTableConfigurationAction.create("ICD"), result -> {
                Map<String, PostCoordinationTableAxisLabel> labels = new HashMap<>();
                for(String availableAxis : result.getTableConfiguration().getPostCoordinationAxes()) {
                    PostCoordinationTableAxisLabel existingLabel = result.getLabels().stream()
                            .filter(label -> label.getPostCoordinationAxis().equalsIgnoreCase(availableAxis))
                            .findFirst().orElseThrow( () -> new RuntimeException("Couldn't find label for " + availableAxis));
                    labels.put(availableAxis, existingLabel);
                }
                view.setLabels(labels);
            });
        }



        @Override
        protected void handleReloadRequest() {

        }

        @Override
        protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {


        }

}
