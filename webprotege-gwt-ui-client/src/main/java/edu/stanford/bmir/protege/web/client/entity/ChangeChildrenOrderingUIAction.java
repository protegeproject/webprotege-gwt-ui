package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetHierarchyChildrenAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.SaveEntityChildReorderingAction;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChangeChildrenOrderingUIAction extends AbstractUiAction {
    Logger logger = Logger.getLogger("ChangeChildrenOrderingUIAction");


    @Nonnull
    private Supplier<ImmutableSet<OWLEntityData>> selectionSupplier = ImmutableSet::of;

    private final DispatchServiceManager dispatchServiceManager;
    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ModalManager modalManager;
    private final Messages messages;

    private Consumer<Void> handleAfterSave;
    @Nonnull
    private final ChangeChildrenOrderingDialogView view;

    @Inject
    public ChangeChildrenOrderingUIAction(@Nonnull Messages messages,
                                          @Nonnull DispatchServiceManager dispatchServiceManager,
                                          @Nonnull ProjectId projectId,
                                          @Nonnull ModalManager modalManager,
                                          @Nonnull ChangeChildrenOrderingDialogView view) {
        super(messages.reorderChildren());
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.modalManager = modalManager;
        this.messages = messages;
        this.view = view;
    }

    @Override
    public void execute() {
        OWLEntityData entityData = selectionSupplier.get().iterator().next();
        OWLClass owlClass = (OWLClass) entityData.getEntity();
        HierarchyDescriptor descriptor = ClassHierarchyDescriptor.get(new HashSet<>(Collections.singletonList(owlClass)));

        dispatchServiceManager.execute(GetHierarchyChildrenAction.create(projectId, owlClass, descriptor), (result)-> {
            ModalPresenter modalPresenter = modalManager.createPresenter();
            modalPresenter.setTitle(messages.reorderChildrenDialogTitle(entityData.getBrowserText()));
            modalPresenter.setView(view);
            modalPresenter.setEscapeButton(DialogButton.CANCEL);
            modalPresenter.setPrimaryButton(DialogButton.UPDATE);
            modalPresenter.setButtonHandler(DialogButton.UPDATE, closer -> {
                dispatchServiceManager.execute(SaveEntityChildReorderingAction.create(projectId, owlClass.getIRI(), view.getOrderedChildren()),
                        saveResult -> {
                            handleAfterSave.accept(null);
                            closer.closeModal();
                        });
            });
            modalManager.showModal(modalPresenter);
            view.setChildren(result.getChildren().getPageElements().stream().map(GraphNode::getUserObject).collect(Collectors.toList()));
        });

    }

    public void setSelectionSupplier(@Nonnull Supplier<ImmutableSet<OWLEntityData>> selectionSupplier) {
        this.selectionSupplier = checkNotNull(selectionSupplier);
    }

    public void setHandleAfterSave(Consumer<Void> handleAfterSave) {
        this.handleAfterSave = handleAfterSave;
    }

    @Override
    public boolean hasIcon() {
        return super.hasIcon();
    }

    @Nonnull
    @Override
    public String getStyle() {
        return super.getStyle();
    }
}
