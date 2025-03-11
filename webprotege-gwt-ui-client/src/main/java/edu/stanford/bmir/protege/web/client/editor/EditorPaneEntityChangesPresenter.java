package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.change.ChangeListPresenter;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.ui.ElementalUtil;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.access.BuiltInCapability;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2018
 */
public class EditorPaneEntityChangesPresenter implements EditorPanePresenter {

    @Nonnull
    private final ChangeListPresenter changesPresenter;

    @Nonnull
    private WebProtegeClientBundle clientBundle;

    @Nonnull
    private Optional<OWLEntity> currentEntity = Optional.empty();

    @Inject
    public EditorPaneEntityChangesPresenter(@Nonnull ChangeListPresenter changesPresenter,
                                            @Nonnull WebProtegeClientBundle clientBundle) {
        this.changesPresenter = checkNotNull(changesPresenter);
        this.clientBundle = checkNotNull(clientBundle);
    }

    @Nonnull
    @Override
    public String getCaption() {
        return "Changes";
    }

    @Nonnull
    @Override
    public String getAdditionalStyles() {
        return clientBundle.buttons().changes();
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        container.setWidget(changesPresenter.getView());
    }

    @Nonnull
    @Override
    public BuiltInCapability getRequiredAction() {
        return BuiltInCapability.VIEW_CHANGES;
    }

    @Override
    public void setEntity(@Nonnull OWLEntity entity) {
        this.currentEntity = Optional.of(checkNotNull(entity));
        changesPresenter.displayChangesForEntity(entity);
    }

    @Override
    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        changesPresenter.setHasBusy(hasBusy);
    }

    @Override
    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        changesPresenter.setEntityDisplay(entityDisplay);
    }

    @Override
    public void dispose() {
        // Nothing to do
    }

    @Override
    public boolean isActive() {
        return ElementalUtil.isWidgetOrDescendantWidgetActive(changesPresenter.getView());
    }

    @Override
    public void reload() {
        currentEntity.ifPresent(changesPresenter::displayChangesForEntity);
    }
}
