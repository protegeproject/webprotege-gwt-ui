package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveToParentUiAction extends AbstractUiAction {

    @Nonnull
    private final MoveEntitiesToParentPresenter parentPresenter;

    @Nonnull
    private final BulkEditOperationWorkflowFactory workflowFactory;

    @Nonnull
    private Supplier<ImmutableSet<OWLEntityData>> selectionSupplier = ImmutableSet::of;

    @Inject
    public MoveToParentUiAction(@Nonnull MoveEntitiesToParentPresenter presenter,
                                @Nonnull BulkEditOperationWorkflowFactory workflowFactory) {
        super(presenter.getTitle() + "...");
        this.parentPresenter = checkNotNull(presenter);
        this.workflowFactory = checkNotNull(workflowFactory);
    }

    public void setEntityType(@Nonnull EntityType<?> entityType) {
        parentPresenter.setEntityType(entityType);
    }

    public void setSelectionSupplier(@Nonnull Supplier<ImmutableSet<OWLEntityData>> selectionSupplier) {
        this.selectionSupplier = checkNotNull(selectionSupplier);
    }

    @Override
    public void execute() {
        ImmutableSet<OWLEntityData> entities = selectionSupplier.get();
        if(entities.isEmpty()) {
            return;
        }
        BulkEditOperationWorkflow workflow = workflowFactory.create(parentPresenter, entities);
        workflow.start();
    }

    public void setHierarchyDescriptor(@Nonnull HierarchyDescriptor hierarchyDescriptor) {
        parentPresenter.setHierarchyDescriptor(hierarchyDescriptor);
    }
}
