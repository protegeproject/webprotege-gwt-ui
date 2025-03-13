package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBoxHandler;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommitChangesWorkflow {

    private static final Logger logger = Logger.getLogger("CommitChangesWorkflow");

    private final DispatchServiceManager dispatch;

    private final InputBox inputBox;

    private final Messages messages;

    @Inject
    public CommitChangesWorkflow(DispatchServiceManager dispatch, InputBox inputBox, Messages messages) {
        this.dispatch = dispatch;
        this.inputBox = inputBox;
        this.messages = messages;
    }

    public void run(List<EntityCardPresenter> cardPresenters,
                    Runnable finishedHandler) {
        inputBox.showDialog(messages.editing_commitMessage_title(),
                true, "", new InputBoxHandler() {
                    @Override
                    public void handleAcceptInput(String commitMessage) {
                        saveChangesWithCommitMessage(cardPresenters, commitMessage);
                        finishedHandler.run();
                    }

                    @Override
                    public void handleCancelInput() {
                        finishedHandler.run();
                    }
                });
    }

    private void saveChangesWithCommitMessage(List<EntityCardPresenter> cardPresenters,
                                              String commitMessage) {
        try {
            dispatch.beginBatch();
            cardPresenters.forEach(card -> {
                if(card instanceof EntityCardEditorPresenter) {
                    try {
                        ((EntityCardEditorPresenter) card).finishEditing(commitMessage);
                    }
                    catch (Exception e) {
                        logger.log(Level.SEVERE, "Error when finishing editing in card", e);
                    }
                }
            });
        }
        finally {
            dispatch.executeCurrentBatch();
        }
    }
}
