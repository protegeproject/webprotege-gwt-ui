package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageLocalHistory;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageLocalHistoryStorage;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageView;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasAcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 7 Dec 2017
 */
public class WhoCreateClassDialogViewImpl extends Composite implements WhoCreateClassDialogView, HasAcceptKeyHandler {

    interface CreateEntitiesDialogViewImplUiBinder extends UiBinder<HTMLPanel, WhoCreateClassDialogViewImpl> {
    }

    private static CreateEntitiesDialogViewImplUiBinder ourUiBinder = GWT.create(CreateEntitiesDialogViewImplUiBinder.class);

    @UiField
    ExpandingTextBoxImpl textBox;

    @UiField
    HTMLPanel commitMessageViewContainer;

    private String previousEntitiesString = "";

    @UiField
    Label entityNamesLabel;

    @UiField
    Label entityAlreadyExistsWarn;

    @Nonnull
    private final Messages messages;
    private final CommitMessageView commitMessageView;
    private final CommitMessageLocalHistoryStorage historyStorage;

    @UiField
    SimplePanel duplicateEntityResultsContainer;

    @UiField
    Label reasonForChangeErrorLabel;

    private EntityType entityType;

    private String reasonForChangeErrorMessage = "A reason for the change was not provided.\n" +
            "Please fill in the Reason for change field.";

    private EntitiesStringChangedHandler entitiesStringChangedHandler = (value) -> {
    };


    private final static Logger logger = Logger.getLogger(WhoCreateClassDialogViewImpl.class.getName());

    @Inject
    public WhoCreateClassDialogViewImpl( @Nonnull Messages messages,
                                        CommitMessageView commitMessageView,
                                        CommitMessageLocalHistoryStorage historyStorage) {
        this.messages = checkNotNull(messages);
        this.historyStorage = historyStorage;
        initWidget(ourUiBinder.createAndBindUi(this));
        entityAlreadyExistsWarn.setVisible(false);
        // Initialize commit message view with local history
        CommitMessageLocalHistory localHistory = historyStorage.loadLocalHistory();
        commitMessageView.setLocalHistory(localHistory.getMessages());
        this.commitMessageView = commitMessageView;

        // Inject the commit message view into the container
        commitMessageViewContainer.add(commitMessageView);
    }

    @Override
    public void setEntityType(@Nonnull EntityType<?> entityType) {
        this.entityType = entityType;
        entityNamesLabel.setText(entityType.getPrintName() + " names");
    }

    @Nonnull
    @Override
    public String getText() {
        return textBox.getText().trim();
    }

    @Nonnull
    @Override
    public String getReasonForChange() {
        return commitMessageView.getCommitMessage().trim();
    }

    @Override
    public void clear() {
        textBox.setText("");
        commitMessageView.setCommitMessage("");
        clearErrors();
        clearEntityAlreadyExistsMessage();
        commitMessageView.setLocalHistory(historyStorage.loadLocalHistory().getMessages());
    }


    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(() -> textBox.setFocus(true));
    }

    @Override
    public void setAcceptKeyHandler(AcceptKeyHandler acceptKey) {
        textBox.setAcceptKeyHandler(acceptKey);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        textBox.setFocus(true);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getDuplicateEntityResultsContainer() {
        return duplicateEntityResultsContainer;
    }

    @Override
    public void setEntitiesStringChangedHandler(EntitiesStringChangedHandler handler) {
        this.entitiesStringChangedHandler = handler;
    }

    private void performSearchIfChanged(String entitiesText) {
        if (!previousEntitiesString.equals(entitiesText)) {
            previousEntitiesString = entitiesText;
            entitiesStringChangedHandler.handleEntitiesStringChangedHandler(entitiesText);
        }
    }

    @UiHandler("textBox")
    protected void handleEntitiesInputKeyUp(KeyUpEvent event) {
        if(isNotClassEntityType()){
            return;
        }
        int keyCode = event.getNativeEvent().getKeyCode();
        if (keyCode != KeyCodes.KEY_UP && keyCode != KeyCodes.KEY_DOWN && keyCode != KeyCodes.KEY_ENTER) {
            performSearchIfChanged(textBox.getText());
        }
    }

    private boolean isClassEntityType(){
        return this.entityType.equals(EntityType.CLASS);
    }

    private boolean isNotClassEntityType(){
        return !isClassEntityType();
    }


    @Override
    public boolean isReasonForChangeSet() {
        if (commitMessageView.getCommitMessage().isEmpty()) {
            reasonForChangeErrorLabel.setText(reasonForChangeErrorMessage);
            reasonForChangeErrorLabel.addStyleName(WebProtegeClientBundle.BUNDLE.style().errorLabel());
            textBox.addStyleName(WebProtegeClientBundle.BUNDLE.style().errorBorder());
            return false;
        }
        clearErrors();
        return true;
    }

    public void clearErrors() {
        reasonForChangeErrorLabel.setText("");
        textBox.removeStyleName(WebProtegeClientBundle.BUNDLE.style().errorBorder());
        reasonForChangeErrorLabel.removeStyleName(WebProtegeClientBundle.BUNDLE.style().errorLabel());
    }
    
    /**
     * Updates the local history with the current commit message
     */
    public void saveReasonForChange(String reasonForChange) {
        CommitMessageLocalHistory localHistory = historyStorage.loadLocalHistory();
        localHistory.pushMessage(reasonForChange);
        historyStorage.saveLocalHistory(localHistory);
    }


    @Override
    public void clearEntityAlreadyExistsMessage() {
        entityAlreadyExistsWarn.setVisible(false);
    }

    @Override
    public void displayEntityAlreadyExistsMessage() {
        entityAlreadyExistsWarn.setVisible(true);
    }
}