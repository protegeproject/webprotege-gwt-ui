package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.client.primitive.NullFreshEntitySuggestStrategy;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EditParentsViewImpl extends Composite implements EditParentsView {

    @UiField
    ExpandingTextBoxImpl textBox;

    private final Messages messages;

    @UiField(provided = true)
    final PrimitiveDataListEditor domains;

    @UiField
    ExpandingTextBoxImpl reasonForChangeTextBox;

    @UiField
    Label reasonForChangeErrorLabel;

    @UiField
    HTML classesWithCyclesWarningField;

    @UiField
    HTML classesWithRetiredParentsField;

    @UiField
    HTML linearizationPathParentField;

    private static final Logger logger = Logger.getLogger(EditParentsViewImpl.class.getName());

    interface EditParentsViewImplUiBinder extends UiBinder<HTMLPanel, EditParentsViewImpl> {
    }

    private static EditParentsViewImpl.EditParentsViewImplUiBinder ourUiBinder = GWT
            .create(EditParentsViewImpl.EditParentsViewImplUiBinder.class);

    @Inject
    public EditParentsViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                               @Nonnull Messages messages) {
        this.messages = messages;
        domains = new PrimitiveDataListEditor(primitiveDataEditorProvider, new NullFreshEntitySuggestStrategy(), PrimitiveType.CLASS);
        initWidget(ourUiBinder.createAndBindUi(this));
        domains.setPlaceholder(messages.frame_enterAClassName());
        domains.setValue(new ArrayList<>());
        domains.setEnabled(true);
        textBox.setEnabled(false);
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
        this.domains.setValue(entityParents.stream().map(entityParent -> (OWLPrimitiveData) entityParent).collect(Collectors.toList()));
    }


    @Override
    protected void onAttach() {
        super.onAttach();
    }


    @Override
    public boolean isReasonForChangeSet() {
        if (reasonForChangeTextBox.getText().isEmpty()) {
            reasonForChangeErrorLabel.setText(messages.reasonForChangeError());
            reasonForChangeErrorLabel.addStyleName(WebProtegeClientBundle.BUNDLE.style().errorLabel());
            return false;
        }
        clearReasonForChangeErrors();
        return true;
    }

    public void clearReasonForChangeErrors() {
        reasonForChangeErrorLabel.setText("");
        reasonForChangeErrorLabel.removeStyleName(WebProtegeClientBundle.BUNDLE.style().errorLabel());
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
        return this.domains.getValue().orElseGet(ArrayList::new);
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
    public void markLinearizationPathParent(OWLEntityData linearizationPathParent) {
        linearizationPathParentField.setHTML(messages.classHierarchy_removeParentThatIsLinearizationPathParent(linearizationPathParent.getBrowserText()));
        linearizationPathParentField.setVisible(true);
    }
}
