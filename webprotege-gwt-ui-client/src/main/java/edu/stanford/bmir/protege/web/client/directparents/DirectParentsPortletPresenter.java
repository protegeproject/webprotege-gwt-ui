package edu.stanford.bmir.protege.web.client.directparents;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.client.selection.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@Portlet(id = "portlets.DirectParents", title = "Direct Parents", tooltip = "Displays the direct parents of the entity")
public class DirectParentsPortletPresenter extends AbstractWebProtegePortletPresenter {



    @Nonnull
    private final DirectParentsListPresenter presenter;

    @Nonnull
    private final Messages messages;

    private Optional<PortletUi> portletUi = Optional.empty();



    @Inject
    public DirectParentsPortletPresenter(@Nonnull SelectionModel selectionModel,
                                         @Nonnull SelectedPathsModel selectedPathsModel,
                                         @Nonnull Messages messages,
                                         @Nonnull ProjectId projectId,
                                         DisplayNameRenderer displayNameRenderer,
                                         DispatchServiceManager dispatch,
                                         DirectParentsListPresenter presenter) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.messages = messages;
        this.presenter = presenter;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.portletUi = Optional.of(portletUi);
        portletUi.setWidget(presenter.getView().asWidget());
        presenter.setHasBusy(portletUi);
        presenter.start(eventBus);
        presenter.setEntityDisplay(this);
        handleSetEntity(getSelectedEntity());
        setDisplaySelectedEntityNameAsSubtitle(true);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        handleSetEntity(entity);
    }

    @Override
    protected void handleReloadRequest() {
        handleAfterSetEntity(getSelectedEntity());
    }

    private void handleSetEntity(Optional<OWLEntity> entity) {

                    if(entity.isPresent()) {
                        presenter.setEntity(entity.get());
                    }
                    else {
                        presenter.clear();
                        setDisplayedEntity(Optional.empty());
                    }
    }
}
