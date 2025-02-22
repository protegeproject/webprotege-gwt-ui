package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectedPathsModel;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.client.tab.SelectedTabIdStash;
import edu.stanford.bmir.protege.web.client.ui.HasDisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@Portlet(id = "portlets.form", title = "Form", tooltip = "Displays forms for the selected entity")
public class FormPortletPresenter extends AbstractWebProtegePortletPresenter implements HasDisplayContextBuilder {

    @Nonnull
    private final EntityFormStackPresenter entityFormStackPresenter;

    @Inject
    public FormPortletPresenter(@Nonnull SelectionModel selectionModel,
                                @Nonnull SelectedPathsModel selectedPathsModel,
                                @Nonnull ProjectId projectId,
                                @Nonnull DisplayNameRenderer displayNameRenderer,
                                @Nonnull EntityFormStackPresenter entityFormStackPresenter,
                                DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer, dispatch, selectedPathsModel);
        this.entityFormStackPresenter = checkNotNull(entityFormStackPresenter);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        updateForms();
    }

    @Override
    protected void handlePlaceChangeFromNonProjectViewPlace() {
        updateForms();
    }

    private void updateForms() {
        Optional<OWLEntity> entity = getSelectedEntity();
        if(entity.isPresent()) {
            setNothingSelectedVisible(false);
            entityFormStackPresenter.setEntity(entity.get());
        }
        else {
            setNothingSelectedVisible(true);
            setDisplayedEntity(Optional.empty());
        }
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.addAction(new PortletAction("Expand all", "wp-btn-g--expand-all", entityFormStackPresenter::expandAllFields));
        portletUi.addAction(new PortletAction("Collapse all", "wp-btn-g--collapse-all", entityFormStackPresenter::collapseAllFields));
        setDisplaySelectedEntityNameAsSubtitle(true);
        entityFormStackPresenter.start(portletUi);
        entityFormStackPresenter.setHasBusy(portletUi);
        entityFormStackPresenter.setSelectedFormIdStash(new SelectedTabIdStash<>(portletUi, new FormIdTabKeySerializer()));
        entityFormStackPresenter.setLanguageFilterStash(new FormLanguageFilterStash(portletUi));
        entityFormStackPresenter.setEntityDisplay(this);
        handleAfterSetEntity(getSelectedEntity());

        entityFormStackPresenter.setParentDisplayContextBuilder(this);
    }

    @Override
    public void dispose() {
        super.dispose();

    }

    @Override
    protected void handleReloadRequest() {
        updateForms();
    }

    @Override
    public void fillDisplayContext(DisplayContextBuilder displayContextBuilder) {
        super.fillDisplayContext(displayContextBuilder);
        entityFormStackPresenter.fillDisplayContext(displayContextBuilder);
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        super.setParentDisplayContextBuilder(parent);
    }
}
