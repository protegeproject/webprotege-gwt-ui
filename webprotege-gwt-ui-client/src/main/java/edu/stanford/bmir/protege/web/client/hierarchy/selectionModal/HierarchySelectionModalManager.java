package edu.stanford.bmir.protege.web.client.hierarchy.selectionModal;

import com.google.gwt.event.shared.SimpleEventBus;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

public class HierarchySelectionModalManager {

    private final ModalManager modalManager;

    private final HierarchySelectionModalPresenter modelPresenter;

    @Inject
    public HierarchySelectionModalManager(@Nonnull ModalManager modalManager,
                                          HierarchySelectionModalPresenter modelPresenter) {
        this.modalManager = modalManager;
        this.modelPresenter = modelPresenter;
    }


    public void showModal(String title, Set<OWLClass> roots, HierarchySelectionHandler handler) {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modelPresenter.clean();
        modalPresenter.setTitle(title);
        modalPresenter.setView(modelPresenter.getView());
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.SELECT);
        modalPresenter.setButtonHandler(DialogButton.SELECT, closer -> {
            closer.closeModal();
            modelPresenter.getSelection().ifPresent(handler::handleSelection);
        });
        WebProtegeEventBus eventBus = new WebProtegeEventBus(new SimpleEventBus());
        modelPresenter.start(eventBus, ClassHierarchyDescriptor.get(roots));
        modalManager.showModal(modalPresenter);
    }
}
