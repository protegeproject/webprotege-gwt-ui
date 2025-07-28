package edu.stanford.bmir.protege.web.client.commit;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

public interface CommitMessageView extends IsWidget {

    void setCommitMessage(String commitMessage);

    String getCommitMessage();

    void setLocalHistory(List<String> localHistory);
}
