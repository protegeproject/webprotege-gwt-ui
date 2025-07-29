package edu.stanford.bmir.protege.web.client.card;

import edu.stanford.bmir.protege.web.client.commit.CommitMessageDialogPresenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommitChangesWorkflow {

    private static final Logger logger = Logger.getLogger("CommitChangesWorkflow");

    private final DispatchServiceManager dispatch;

    private final CommitMessageDialogPresenter commitMessageDialogPresenter;

    @Inject
    public CommitChangesWorkflow(DispatchServiceManager dispatch,
                                 CommitMessageDialogPresenter commitMessageDialogPresenter) {
        this.dispatch = dispatch;
        this.commitMessageDialogPresenter = commitMessageDialogPresenter;
    }

    public void run(List<EntityCardPresenter> cardPresenters,
                    Runnable finishedHandler) {
        commitMessageDialogPresenter.showCommitMessageDialog(commitMessage -> {
            saveChangesWithCommitMessage(cardPresenters, commitMessage);
            finishedHandler.run();
        }, finishedHandler::run);
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
