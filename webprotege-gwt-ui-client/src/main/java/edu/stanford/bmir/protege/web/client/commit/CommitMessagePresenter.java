package edu.stanford.bmir.protege.web.client.commit;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

public class CommitMessagePresenter {

    private final CommitMessageView view;

    private final CommitMessageLocalHistoryStorage historyStorage;

    @Inject
    public CommitMessagePresenter(CommitMessageView view, CommitMessageLocalHistoryStorage historyStorage) {
        this.view = view;
        this.historyStorage = historyStorage;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        CommitMessageLocalHistory localHistory = historyStorage.loadLocalHistory();
        view.setLocalHistory(localHistory.getMessages());
    }

    public void updateLocalHistory() {
        String currentCommitMessage = view.getCommitMessage();
        if(currentCommitMessage.isEmpty()) {
           return;
        }
        CommitMessageLocalHistory localHistory = historyStorage.loadLocalHistory();
        localHistory.pushMessage(currentCommitMessage);
        historyStorage.saveLocalHistory(localHistory);
    }

    public String getCommitMessage() {
        return view.getCommitMessage();
    }
}
