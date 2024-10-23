package edu.stanford.bmir.protege.web.client.logicaldefinition;


import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPopupPresenterFactory;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.library.modal.ModalStyleConfig;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;


public class LogicalDefinitionModal {

    @Nonnull
    private final ModalManager modalManager;

    private final HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory;
    private final  LogicalDefinitionModalView logicalDefinitionModalView;

    private final LogicalDefinitionResourceBundle.LogicalDefinitionCss style;

    private final ModalPresenter presenter;

    @Inject
    public LogicalDefinitionModal(@Nonnull ModalManager modalManager,
                                  LogicalDefinitionModalView logicalDefinitionModalView,
                                  HierarchyPopupPresenterFactory hierarchyPopupPresenterFactory) {
        this.modalManager = modalManager;
        this.logicalDefinitionModalView = logicalDefinitionModalView;
        this.hierarchyPopupPresenterFactory = hierarchyPopupPresenterFactory;
        LogicalDefinitionResourceBundle.INSTANCE.style().ensureInjected();
        style = LogicalDefinitionResourceBundle.INSTANCE.style();
        presenter = modalManager.createPresenter();
        presenter.setTitle("Select axis value");
        presenter.setView(logicalDefinitionModalView);
        presenter.setModalStyleConfig(new ModalStyleConfig(style.selectValueModalWrapper()));
        presenter.setEscapeButton(DialogButton.CANCEL);
        presenter.setPrimaryButton(DialogButton.OK);

    }

    public void showModal(Set<OWLClass> availableValues, LogicalDefinitionTableConfig.SelectedAxisValueHandler selectionHandler) {

        presenter.setButtonHandler(DialogButton.OK, closer -> {
            presenter.closeModal();
            if(logicalDefinitionModalView.getSelectedEntity().isPresent()) {
                selectionHandler.handleSelectAxisValue(logicalDefinitionModalView.getSelectedEntity().get());
            }
        });

        logicalDefinitionModalView.showTree(availableValues, entityNode -> {
            presenter.closeModal();
            if(logicalDefinitionModalView.getSelectedEntity().isPresent()) {
                selectionHandler.handleSelectAxisValue(logicalDefinitionModalView.getSelectedEntity().get());
            }
        });
        modalManager.showModal(presenter);
    }


}
