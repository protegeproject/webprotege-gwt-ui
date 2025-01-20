package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

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
    ExpandingTextBoxImpl reasonForChangeTextBox;

    @UiField
    Label reasonForChangeErrorLabel;

    private final Messages messages;

    public MessageBoxWithReasonForChangeView(Messages messages) {
        this.messages = messages;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
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
        if (reasonForChangeTextBox.getText().trim().isEmpty()) {
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
        return reasonForChangeTextBox.getText().trim();
    }
}