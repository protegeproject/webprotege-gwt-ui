package edu.stanford.bmir.protege.web.client.commit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitGeneratedAnnotationsSettingsPresenter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CommitMessageViewImpl extends Composite implements CommitMessageView {

    public static final int MAX_RECENT_MESSAGES = 10;

    private final List<String> localHistory = new ArrayList<>();

    interface CommitMessageViewImplUiBinder extends UiBinder<HTMLPanel, CommitMessageViewImpl> {

    }

    private static CommitMessageViewImplUiBinder ourUiBinder = GWT.create(CommitMessageViewImplUiBinder.class);

    @UiField
    TextArea messageArea;

    @UiHandler("recentMessages" )
    public void handleRecentMessagesChange(ChangeEvent event) {
        String selectedRecentMessage = recentMessages.getSelectedValue();
        if(selectedRecentMessage != null) {
            messageArea.setValue(selectedRecentMessage);
        }
    }

    @UiField
    ListBox recentMessages;

    @Inject
    public CommitMessageViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setCommitMessage(String commitMessage) {
        messageArea.setValue(commitMessage);
    }

    @Override
    public String getCommitMessage() {
        return messageArea.getValue().trim();
    }

    @Override
    public void setLocalHistory(List<String> localHistory) {
        recentMessages.clear();
        this.localHistory.clear();
        this.localHistory.addAll(localHistory);
        for(int i = 0; i < MAX_RECENT_MESSAGES && i < localHistory.size(); i++) {
            String recentMessage = localHistory.get(i);
            recentMessages.addItem(recentMessage);
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        messageArea.setFocus(true);
        messageArea.setSelectionRange(0, messageArea.getText().length());
    }
}