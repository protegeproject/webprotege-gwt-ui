package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetClassHierarchyParentsByAxiomTypeAction;
import edu.stanford.bmir.protege.web.shared.linearization.GetEntityLinearizationAction;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationSpecification;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

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

    private ParentSelectionHandler selectionHandler = entity -> {};

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
            //TODO ALEX replace this with getByEntityType , join them by a set, and mark the ones that are from logical definitions as grey
            dispatch.execute(
                    GetClassHierarchyParentsByAxiomTypeAction.create(projectId, e.asOWLClass(), ClassHierarchyDescriptor.get()),
                    hasBusy,
                    result -> {
                        Set<EntityNode> allParents = Objects.requireNonNull(result.getParentsBySubclassOf()).stream()
                                .map(EntityNode::getFromEntityData)
                                .collect(Collectors.toSet());

                        allParents.addAll(Objects.requireNonNull(result.getParentsByEquivalentClass()).stream()
                                .map(EntityNode::getFromEntityData)
                                .collect(Collectors.toSet()));

                        Set<OWLEntityData> equivalentOnlyParents = Objects.requireNonNull(result.getParentsByEquivalentClass()).stream()
                                .filter(equivalentParent -> Objects.requireNonNull(result.getParentsBySubclassOf()).stream()
                                        .noneMatch(subclassParent -> subclassParent.getEntity().equals(equivalentParent.getEntity())))
                                .collect(Collectors.toSet());

                        entityDisplay.setDisplayedEntity(Optional.of(result.getEntity()));
                        displayParents(allParents, equivalentOnlyParents);
                    }
            );
            logger.info("Marking direct parent");
            dispatch.execute(GetEntityLinearizationAction.create(displayedEntity.get().getIRI().toString(), projectId), (result) -> {
                Optional<String> mmsParent = result.getWhoficEntityLinearizationSpecification().getLinearizationSpecifications().stream()
                        .filter(linearizationSpecification -> linearizationSpecification.getLinearizationView().equals("http://id.who.int/icd/release/11/mms"))
                        .findFirst()
                        .map(LinearizationSpecification::getLinearizationParent);

                mmsParent.ifPresent(view::setMainParent);
            });
        });
    }

    private void displayParents(Set<EntityNode> parents, Set<OWLEntityData> equivalentOnlyParents) {
        logger.info("Direct parents count: " + parents.size());
        List<DirectParentPresenter> presenters = parents.stream()
                .map(parent -> directParentFactory.create(parent, selectionHandler))
                .collect(Collectors.toList());
        redisplayParents(presenters, equivalentOnlyParents);
    }

    private void redisplayParents(List<DirectParentPresenter> presenters,  Set<OWLEntityData> equivalentOnlyParents) {
        logger.info("Displaying " + presenters.size() + " parent entities.");
        view.clearViews();
        stopDirectParentPresenters();
        directParentPresenters.addAll(presenters);
        directParentPresenters.forEach(DirectParentPresenter::start);
        List<DirectParentView> directParents = directParentPresenters.stream()
                .map(DirectParentPresenter::getView)
                .collect(Collectors.toList());

        view.setDirectParentView(directParents);
        view.setEquivalentOnlyParents(equivalentOnlyParents);
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

    public void setDirectParentSelectionHandler(ParentSelectionHandler handler) {
        this.selectionHandler = handler;
    }
}
