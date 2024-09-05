package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.shared.card.CardDescriptor;

import javax.inject.Inject;

public class CardWorkflow {

    // Status


    @Inject
    public CardWorkflow() {
    }

    public void handleBeforeCardChange(CardDescriptor from, CardDescriptor to) {
        // Outstanding edits
    }

    public void handleAfterCardChange(CardDescriptor from, CardDescriptor to) {

    }

    public void handleEditRequest() {

    }

    public void handleSaveRequest() {

    }

    public void handleCancelRequest() {

    }

}
