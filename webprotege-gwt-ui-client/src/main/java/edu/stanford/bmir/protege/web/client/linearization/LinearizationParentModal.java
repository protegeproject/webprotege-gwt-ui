package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.modal.ModalStyleConfig;
import edu.stanford.bmir.protege.web.shared.icd.GetClassAncestorsAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;


import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.logging.Logger;

public class LinearizationParentModal {

    @Nonnull
    private final ModalManager modalManager;


    private final DispatchServiceManager dispatchServiceManager;

    LinearizationParentsResourceBundle.LinearizationParentCss style = LinearizationParentsResourceBundle.INSTANCE.style();

    @Inject
    public LinearizationParentModal(@Nonnull ModalManager modalManager, DispatchServiceManager dispatchServiceManager) {
        this.modalManager = modalManager;
        this.dispatchServiceManager = dispatchServiceManager;
        style.ensureInjected();
    }


    public void showModal(IRI selectedEntityIri, ProjectId projectId, LinearizationParentLabel.ParentSelectedHandler parentSelectedHandler) {
        dispatchServiceManager.execute(GetClassAncestorsAction.create(selectedEntityIri, projectId), getHierarchyParentsResult -> {
            ModalPresenter presenter = modalManager.createPresenter();
            LinearizationParentView view = new LinearizationParentViewImpl(getHierarchyParentsResult.getAncestorsTree());
            presenter.setTitle("Set linearization parent");
            presenter.setView(view);
            presenter.setModalStyleConfig(new ModalStyleConfig(style.getParentModalWrapper()));
            presenter.setEscapeButton(DialogButton.CANCEL);
            presenter.setPrimaryButton(DialogButton.OK);
            presenter.setButtonHandler(DialogButton.OK, closer -> {
                parentSelectedHandler.handleParentSelected(view.getSelectedParent());
                presenter.closeModal();
            });
            modalManager.showModal(presenter);
        });
    }



}