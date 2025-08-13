package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageLocalHistory;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageLocalHistoryStorage;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageView;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.inject.Inject;
import java.util.Optional;


public class MessageBoxWithReasonForChangeView extends Composite implements MessageBoxView {

    interface MessageBoxViewImplUiBinder extends UiBinder<HTMLPanel, MessageBoxWithReasonForChangeView> {

    }

    private static MessageBoxViewImplUiBinder ourUiBinder = GWT.create(MessageBoxViewImplUiBinder.class);

    @UiField
    protected HasHTML titleLabel;

    @UiField
    protected HasHTML messageLabel;

    @UiField
    protected Image iconImage;

    @UiField
    HTMLPanel commitMessageViewContainer;

    private final CommitMessageView commitMessageView;
    private final CommitMessageLocalHistoryStorage historyStorage;

    @UiField
    Label reasonForChangeErrorLabel;

    private final Messages messages;

    @Inject
    public MessageBoxWithReasonForChangeView(Messages messages, 
                                            CommitMessageView commitMessageView,
                                            CommitMessageLocalHistoryStorage historyStorage) {
        this.messages = messages;
        this.commitMessageView = commitMessageView;
        this.historyStorage = historyStorage;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        
        // Inject the commit message view into the container
        commitMessageViewContainer.add(commitMessageView);
        
        // Initialize commit message view with local history
        CommitMessageLocalHistory localHistory = historyStorage.loadLocalHistory();
        commitMessageView.setLocalHistory(localHistory.getMessages());
    }

    public void setMessageStyle(MessageStyle messageStyle) {
        Optional<DataResource> res = messageStyle.getImage();
        res.ifPresent(dataResource -> {
            iconImage.getElement().getStyle().setOpacity(1);
            iconImage.setUrl(dataResource.getSafeUri().asString());
        });
        if(!res.isPresent()) {
            iconImage.getElement().getStyle().setOpacity(0);
        }
    }

    @Override
    public void setMainMessage(String title) {
        titleLabel.setHTML(title);
    }

    @Override
    public void setSubMessage(String message) {
        messageLabel.setHTML(message);
    }

    public boolean isReasonForChangeSet() {
        if (commitMessageView.getCommitMessage().trim().isEmpty()) {
            reasonForChangeErrorLabel.setText(messages.reasonForChangeError());
            reasonForChangeErrorLabel.addStyleName(WebProtegeClientBundle.BUNDLE.style().errorLabel());
            return false;
        }
        clearErrors();
        return true;
    }

    public void clearErrors() {
        reasonForChangeErrorLabel.setText("");
        reasonForChangeErrorLabel.removeStyleName(WebProtegeClientBundle.BUNDLE.style().errorLabel());
    }

    public String getReasonForChangeString(){
        return commitMessageView.getCommitMessage().trim();
    }
    
    /**
     * Updates the local history with the current commit message
     */
    public void updateLocalHistory() {
        String currentCommitMessage = commitMessageView.getCommitMessage();
        if(currentCommitMessage.isEmpty()) {
           return;
        }
        CommitMessageLocalHistory localHistory = historyStorage.loadLocalHistory();
        localHistory.pushMessage(currentCommitMessage);
        historyStorage.saveLocalHistory(localHistory);
    }
}