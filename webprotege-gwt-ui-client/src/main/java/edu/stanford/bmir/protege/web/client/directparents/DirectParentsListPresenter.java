package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.directparents.GetEntityDirectParentsAction.create;

public class DirectParentsListPresenter implements HasDispose {

    private final static java.util.logging.Logger logger = Logger.getLogger(DirectParentsListPresenter.class.getName());

    @Nonnull
    private final List<DirectParentPresenter> directParentPresenters = new ArrayList<>();

    @Nonnull
    private final DirectParentsListView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;
    private EntityDisplay entityDisplay;
    private Optional<OWLEntity> displayedEntity = Optional.empty();

    private final DirectParentPresenterFactory directParentFactory;

    @Nonnull
    private HasBusy hasBusy = busy -> {
    };

    @Inject
    public DirectParentsListPresenter(@Nonnull DirectParentsListView view,
                                      @Nonnull DispatchServiceManager dispatch,
                                      @Nonnull ProjectId projectId,
                                      DirectParentPresenterFactory directParentFactory) {
        this.view = view;
        this.dispatch = dispatch;
        this.projectId = projectId;
        this.directParentFactory = directParentFactory;
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    @Nonnull
    public IsWidget getView() {
        return view;
    }

    public void start(WebProtegeEventBus eventBus) {
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    public void setEntity(OWLEntity entity) {
        if (!this.displayedEntity.equals(Optional.of(entity))) {
            this.displayedEntity = Optional.of(entity);
            reload();
        }
    }

    public void clear() {
        view.clearViews();
    }

    public void reload() {
        displayedEntity.ifPresent(e -> {
            logger.info("Fetching direct parents for: " + e);
            dispatch.execute(
                    create(projectId, e),
                    hasBusy,
                    result -> {
                        List<EntityNode> directParents = result.getDirectParents();
                        logger.info("Received direct parents: " + directParents);
                        entityDisplay.setDisplayedEntity(Optional.of(result.getEntity()));
                        displayParents(directParents);
                    }
            );
        });
    }

    private void displayParents(List<EntityNode> parents) {
        logger.info("Direct parents count: " + parents.size());
        List<DirectParentPresenter> presenters = parents.stream()
                .map(directParentFactory::create)
                .collect(Collectors.toList());
        redisplayParents(presenters);
    }

    private void redisplayParents(List<DirectParentPresenter> presenters) {
        logger.info("Displaying " + presenters.size() + " parent entities.");
        view.clearViews();
        stopDirectParentPresenters();
        directParentPresenters.addAll(presenters);
        directParentPresenters.forEach(DirectParentPresenter::start);
        List<DirectParentView> directParents = directParentPresenters.stream()
                        .map(DirectParentPresenter::getView)
                                .collect(Collectors.toList());

        view.setDirectParentView(directParents);
    }

    private void stopDirectParentPresenters() {
        logger.info("Stopping presenters directParentPresenters");
        directParentPresenters.forEach(DirectParentPresenter::dispose);
        directParentPresenters.clear();
    }

    @Override
    public void dispose() {
        logger.info("disposing list presenters ");
        stopDirectParentPresenters();
    }
}
