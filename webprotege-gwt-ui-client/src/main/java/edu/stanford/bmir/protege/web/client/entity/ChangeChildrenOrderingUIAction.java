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
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsAction;
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
    @Nonnull
    private final MessageBox messageBox;
    private final Messages messages;
    @Nonnull
    private final ChangeChildrenOrderingDialogView view;
    @Inject
    public ChangeChildrenOrderingUIAction(@Nonnull Messages messages,
                                          @Nonnull DispatchServiceManager dispatchServiceManager,
                                          @Nonnull ProjectId projectId,
                                          @Nonnull ModalManager modalManager, @Nonnull MessageBox messageBox, @Nonnull ChangeChildrenOrderingDialogView view) {
        super(messages.reorderChildren());
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
        this.modalManager = modalManager;
        this.messages = messages;
        this.messageBox = messageBox;
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
                        saveResult -> messageBox.showMessage("Children ordering",
                                "The new children ordering has been applied."));
                closer.closeModal();
            });
            modalManager.showModal(modalPresenter);
            view.setChildren(result.getChildren().getPageElements().stream().map(GraphNode::getUserObject).collect(Collectors.toList()));
        });

    }

    public void setSelectionSupplier(@Nonnull Supplier<ImmutableSet<OWLEntityData>> selectionSupplier) {
        this.selectionSupplier = checkNotNull(selectionSupplier);
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
