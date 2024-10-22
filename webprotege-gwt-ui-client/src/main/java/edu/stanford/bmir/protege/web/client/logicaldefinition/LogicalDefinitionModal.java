package edu.stanford.bmir.protege.web.client.logicaldefinition;


import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenterFactory;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;


public class LogicalDefinitionModal {

    @Nonnull
    private final ModalManager modalManager;

    private final HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory;
    private final  LogicalDefinitionModalView logicalDefinitionModalView;

    @Inject
    public LogicalDefinitionModal(@Nonnull ModalManager modalManager,
                                  LogicalDefinitionModalView logicalDefinitionModalView,
                                  HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory) {
        this.modalManager = modalManager;
        this.logicalDefinitionModalView = logicalDefinitionModalView;
        this.hierarchyPopupPresenterFactory = hierarchyPopupPresenterFactory;
    }

    public void showModal(Set<OWLClass> availableValues, LogicalDefinitionTableConfig.SelectedAxisValueHandler selectionHandler) {

        ModalPresenter presenter = modalManager.createPresenter();
        presenter.setTitle("Set linearization parent");
        presenter.setView(logicalDefinitionModalView);
/*
        presenter.setModalStyleConfig(new ModalStyleConfig(style.getParentModalWrapper()));
*/
        presenter.setEscapeButton(DialogButton.CANCEL);
        presenter.setPrimaryButton(DialogButton.OK);
        presenter.setButtonHandler(DialogButton.OK, closer -> {
//            parentSelectedHandler.handleParentSelected(view.getSelectedParent());
            presenter.closeModal();
        });
        logicalDefinitionModalView.showTree(availableValues, selectionHandler);
        modalManager.showModal(presenter);


    }


}
