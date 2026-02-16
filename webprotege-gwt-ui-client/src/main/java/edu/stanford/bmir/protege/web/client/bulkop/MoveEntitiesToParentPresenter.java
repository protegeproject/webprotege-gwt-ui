package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldPresenter;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.HasBrowserText;
import edu.stanford.bmir.protege.web.shared.bulkop.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.perspective.ChangeRequestId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveEntitiesToParentPresenter implements BulkEditOperationPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final MoveToParentView view;

    @Nonnull
    private final HierarchyFieldPresenter hierarchyFieldPresenter;

    private final UuidV4Provider uuidV4Provider;


    @Nonnull
    private EntityType<?> entityType = EntityType.CLASS;

    @Inject
    public MoveEntitiesToParentPresenter(@Nonnull ProjectId projectId, @Nonnull MoveToParentView view,
                                         @Nonnull HierarchyFieldPresenter presenter, UuidV4Provider uuidV4Provider) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.hierarchyFieldPresenter = checkNotNull(presenter);
        this.uuidV4Provider = uuidV4Provider;
    }

    public void setEntityType(@Nonnull EntityType<?> entityType) {
        this.entityType = checkNotNull(entityType);
    }

    @Override
    public String getTitle() {
        return "Move";
    }

    @Override
    public String getExecuteButtonText() {
        return getTitle();
    }

    @Override
    public String getHelpMessage() {
        return "Moves the selected " + entityType.getPluralPrintName().toLowerCase() + " from their current location to another location in the " + entityType.getPrintName().toLowerCase() + " hierarchy.";
    }

    @Nonnull
    @Override
    public String getDefaultCommitMessage(ImmutableSet<? extends OWLEntityData> entities) {
        return "Moved "
                + BulkOpMessageFormatter.sortAndFormat(entities)
                + " to "
                + hierarchyFieldPresenter
                .getEntity()
                .map(HasBrowserText::getBrowserText)
                .orElse("new parent");
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        hierarchyFieldPresenter.setSyncWithCurrentSelectionVisible(false);
        hierarchyFieldPresenter.start(view.getHierarchyFieldContainer(),
                                      eventBus);
        container.setWidget(view);
    }

    @Override
    public boolean isDataWellFormed() {
        return hierarchyFieldPresenter.getEntity().isPresent();
    }

    @Nonnull
    @Override
    public Optional<MoveEntitiesToParentIcdAction> createAction(@Nonnull ImmutableSet<OWLEntity> entities,
                                                                       @Nonnull String commitMessage) {
        ImmutableSet<OWLClass> clses = entities.stream()
                .filter(OWLEntity::isOWLClass)
                .map(OWLEntity::asOWLClass)
                .collect(toImmutableSet());
        return hierarchyFieldPresenter.getEntity()
                .map(OWLEntityData::getEntity)
                .map(entity -> MoveEntitiesToParentIcdAction.create(ChangeRequestId.get(uuidV4Provider.get()),
                                                                 projectId,
                                                                 clses,
                                                                 entity.asOWLClass(),
                                                                 commitMessage));
    }

    @Override
    public void displayErrorMessage() {

    }

    public void setHierarchyDescriptor(@Nonnull HierarchyDescriptor hierarchyDescriptor) {
        hierarchyFieldPresenter.setHierarchyDescriptor(hierarchyDescriptor);
    }
}
