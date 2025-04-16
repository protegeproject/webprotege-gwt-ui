package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.*;

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
    ExpandingTextBoxImpl reasonForChangeTextBox;

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
    HTMLPanel equivalentParentClassTooltip;

    @UiField
    HTMLPanel equivalentParentClassContainer;

    private static final Logger logger = Logger.getLogger(EditParentsViewImpl.class.getName());

    interface EditParentsViewImplUiBinder extends UiBinder<HTMLPanel, EditParentsViewImpl> {
    }

    private static final EditParentsViewImpl.EditParentsViewImplUiBinder ourUiBinder = GWT
            .create(EditParentsViewImpl.EditParentsViewImplUiBinder.class);

    @Inject
    public EditParentsViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                               @Nonnull Messages messages) {
        this.messages = messages;
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
        this.parents.setValue(entityParents.stream().map(entityParent -> (OWLPrimitiveData) entityParent).collect(Collectors.toList()));
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
        if (reasonForChangeTextBox.getText().isEmpty()) {
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
        reasonForChangeTextBox.setText("");
        clearReasonForChangeErrors();
        clearClassesWithCycleErrors();
        clearLinearizationPathParentErrors();
    }

    @Nonnull
    @Override
    public String getReasonForChange() {
        return reasonForChangeTextBox.getText().trim();
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
    public IsWidget getHelpTooltip() {
        return equivalentParentClassTooltip.asWidget();
    }

    @Override
    public boolean isValid() {
        boolean isReasonSet = this.isReasonForChangeSet();
        boolean isParentSet = this.isAtLestOneParentSet();
        return isReasonSet&&isParentSet;
    }
}
