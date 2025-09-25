package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageLocalHistory;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageLocalHistoryStorage;
import edu.stanford.bmir.protege.web.client.commit.CommitMessageView;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.*;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;

public class EditParentsViewImpl extends Composite implements EditParentsView {

    @UiField
    ExpandingTextBoxImpl textBox;

    private final Messages messages;

    @UiField(provided = true)
    final PrimitiveDataListEditor parents;

    @UiField(provided = true)
    final PrimitiveDataListEditor equivalentClassParents;

    @UiField
    HTMLPanel commitMessageViewContainer;

    private final CommitMessageView commitMessageView;
    private final CommitMessageLocalHistoryStorage historyStorage;

    @UiField
    Label reasonForChangeErrorLabel;

    @UiField
    Label noParentSetErrorLabel;

    @UiField
    HTML classesWithCyclesWarningField;

    @UiField
    HTML classesWithRetiredParentsField;

    @UiField
    HTML linearizationPathParentField;

    @UiField
    HTML releasedChildrenErrorsField;
    @UiField
    HTMLPanel equivalentParentClassTooltip;

    @UiField
    HTMLPanel equivalentParentClassContainer;

    private Set<IRI> linearizationPathParents = new HashSet<>();

    private static final Logger logger = Logger.getLogger(EditParentsViewImpl.class.getName());

    interface EditParentsViewImplUiBinder extends UiBinder<HTMLPanel, EditParentsViewImpl> {
    }

    private static final EditParentsViewImpl.EditParentsViewImplUiBinder ourUiBinder = GWT
            .create(EditParentsViewImpl.EditParentsViewImplUiBinder.class);

    @Inject
    public EditParentsViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                               @Nonnull Messages messages,
                               CommitMessageView commitMessageView,
                               CommitMessageLocalHistoryStorage historyStorage) {
        this.messages = messages;
        this.historyStorage = historyStorage;
        parents = new PrimitiveDataListEditor(primitiveDataEditorProvider, new NullFreshEntitySuggestStrategy(), PrimitiveType.CLASS);
        equivalentClassParents = new PrimitiveDataListEditor(primitiveDataEditorProvider, new NullFreshEntitySuggestStrategy(), PrimitiveType.CLASS);
        initWidget(ourUiBinder.createAndBindUi(this));
        parents.setPlaceholder(messages.frame_enterAClassName());
        parents.setValue(new ArrayList<>());
        parents.setEnabled(true);
        this.parents.setEnforceMinOneValue(true);
        textBox.setEnabled(false);

        equivalentClassParents.setValue(new ArrayList<>());
        equivalentClassParents.setEnabled(false);

        reasonForChangeErrorLabel.addStyleName(WebProtegeClientBundle.BUNDLE.style().errorLabel());
        noParentSetErrorLabel.addStyleName(WebProtegeClientBundle.BUNDLE.style().errorLabel());
        // Initialize commit message view with local history
        CommitMessageLocalHistory localHistory = historyStorage.loadLocalHistory();
        commitMessageView.setLocalHistory(localHistory.getMessages());
        this.commitMessageView = commitMessageView;

        // Inject the commit message view into the container
        commitMessageViewContainer.add(commitMessageView);
        

    }

    @Override
    public void setBusy(boolean busy) {

    }

    @Override
    public void setOwlEntityData(OWLEntityData entity) {
        this.textBox.setText(entity.getBrowserText());
        logger.log(Level.FINE, "[EditParentsViewImpl] setting entityData "
                + entity);
    }

    @Override
    public void setEntityParents(Set<OWLEntityData> entityParents) {
        this.parents.setValue(entityParents.stream().map(entityParent -> (OWLPrimitiveData) entityParent).collect(Collectors.toList()),
                (entityParent) -> {
                    return this.linearizationPathParents.contains(entityParent.asEntity().get().getIRI());
                },
                "The delete button is disabled due to being a linearization parent"
        );
    }

    @Override
    public void setParentsFromEquivalentClasses(Set<OWLEntityData> equivalentClassParents) {
        if (equivalentClassParents.isEmpty()) {
            this.equivalentParentClassContainer.setVisible(false);
            return;
        }
        this.equivalentParentClassContainer.setVisible(true);
        this.equivalentClassParents.setValue(equivalentClassParents.stream().map(equivalentClassParent -> (OWLPrimitiveData) equivalentClassParent).collect(Collectors.toList()));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
    }


    private boolean isReasonForChangeSet() {
        if (commitMessageView.getCommitMessage().isEmpty()) {
            reasonForChangeErrorLabel.setText(messages.reasonForChangeError());
            return false;
        }
        clearReasonForChangeErrors();
        return true;
    }

    private void clearReasonForChangeErrors() {
        reasonForChangeErrorLabel.setText("");
    }


    private boolean isAtLestOneParentSet() {
        Optional<List<OWLPrimitiveData>> parentsOptional = this.parents.getValue();
        if (parentsOptional.isPresent() && !parentsOptional.get().isEmpty()) {
            clearNoParentSetErrors();
            return true;
        }
        noParentSetErrorLabel.setText(messages.noParentSetError());
        return false;
    }

    private void clearNoParentSetErrors() {
        noParentSetErrorLabel.setText("");
    }


    @Override
    public void clear() {
        textBox.setText("");
        commitMessageView.setCommitMessage("");
        clearReasonForChangeErrors();
        clearNoParentSetErrors();
        clearClassesWithCycleErrors();
        clearLinearizationPathParentErrors();
        clearReleasedChildrenError();
    }

    @Nonnull
    @Override
    public String getReasonForChange() {
        return commitMessageView.getCommitMessage().trim();
    }

    @Override
    public List<OWLPrimitiveData> getNewParentList() {
        return this.parents.getValue().orElseGet(ArrayList::new);
    }

    @Override
    public void clearClassesWithCycleErrors() {
        classesWithCyclesWarningField.setVisible(false);
        classesWithCyclesWarningField.setHTML("");
    }

    @Override
    public void markClassesWithCycles(Set<OWLEntityData> classesWithCycles) {
        String classes = classesWithCycles.stream().map(OWLEntityData::getBrowserText).collect(Collectors.joining(", "));
        classesWithCyclesWarningField.setHTML(messages.classHierarchy_cyclesHaveBeenCreated(classes));
        classesWithCyclesWarningField.setVisible(true);
    }

    @Override
    public void clearClassesWithRetiredParentsErrors() {
        classesWithRetiredParentsField.setVisible(false);
        classesWithRetiredParentsField.setHTML("");
    }

    @Override
    public void markClassesWithRetiredParents(Set<OWLEntityData> classesWithRetiredParents) {
        String classes = classesWithRetiredParents.stream().map(OWLEntityData::getBrowserText).collect(Collectors.joining(", "));
        classesWithCyclesWarningField.setHTML(messages.classHierarchy_parentsHaveRetiredAncestors(classes));
        classesWithCyclesWarningField.setVisible(true);
    }

    @Override
    public void clearLinearizationPathParentErrors() {
        linearizationPathParentField.setVisible(false);
        linearizationPathParentField.setHTML("");
    }

    @Override
    public void markLinearizationPathParent(String linearizationPathParents) {
        linearizationPathParentField.setHTML(messages.classHierarchy_removeParentThatIsLinearizationPathParent(linearizationPathParents));
        linearizationPathParentField.setVisible(true);
    }

    @Override
    public void displayReleasedChildrenError(String entityName, String validationMessage) {
        releasedChildrenErrorsField.setVisible(true);
        releasedChildrenErrorsField.setHTML(messages.classHierarchy_cannotMoveReleasedClassWithChildrenToRetiredParent(validationMessage));
    }

    @Override
    public void clearReleasedChildrenError(){
        releasedChildrenErrorsField.setVisible(false);
        releasedChildrenErrorsField.setHTML("");
    }


    @Override
    public IsWidget getHelpTooltip() {
        return equivalentParentClassTooltip.asWidget();
    }

    @Override
    public boolean isValid() {
        boolean isReasonSet = this.isReasonForChangeSet();
        boolean isParentSet = this.isAtLestOneParentSet();
        return isReasonSet && isParentSet;
    }

    @Override
    public void setLinearizationPathParents(Set<IRI> linearizationPathParents) {
        this.linearizationPathParents = linearizationPathParents;
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
