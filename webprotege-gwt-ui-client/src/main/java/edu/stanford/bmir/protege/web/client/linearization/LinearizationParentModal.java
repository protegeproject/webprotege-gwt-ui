package edu.stanford.bmir.protege.web.client.linearization;

import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;


import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.logging.Logger;

public class LinearizationParentModal {

    @Nonnull
    private final ModalManager modalManager;

    Logger logger = java.util.logging.Logger.getLogger("LinearizationParentModal");

    @Inject
    public LinearizationParentModal(@Nonnull ModalManager modalManager) {
        this.modalManager = modalManager;
    }


    public void showModal() {

    }

}