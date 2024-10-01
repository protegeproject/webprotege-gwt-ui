package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.scaleValueSelectionModal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;

import javax.annotation.Nonnull;
import javax.inject.*;
import java.util.*;
import java.util.logging.*;

public class ScaleValueSelectionViewImpl extends Composite implements ScaleValueSelectionView {

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

    private static final Logger logger = Logger.getLogger(ScaleValueSelectionViewImpl.class.getName());

    interface ScaleValueSelectionModalImplUiBinder extends UiBinder<HTMLPanel, ScaleValueSelectionViewImpl> {
    }

    private static ScaleValueSelectionViewImpl.ScaleValueSelectionModalImplUiBinder ourUiBinder = GWT
            .create(ScaleValueSelectionModalImplUiBinder.class);

    @Inject
    public ScaleValueSelectionViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
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
    protected void onAttach() {
        super.onAttach();
    }

    public void clearErrors() {
        reasonForChangeErrorLabel.setText("");
        reasonForChangeErrorLabel.removeStyleName(WebProtegeClientBundle.BUNDLE.style().errorLabel());
    }


    @Override
    public void clear() {
        textBox.setText("");
        reasonForChangeTextBox.setText("");
        clearErrors();
    }
}
