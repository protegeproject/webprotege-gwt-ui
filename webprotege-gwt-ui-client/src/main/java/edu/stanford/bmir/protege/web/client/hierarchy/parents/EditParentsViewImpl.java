package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxImpl;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

public class EditParentsViewImpl extends Composite implements EditParentsView {

    @UiField
    ExpandingTextBoxImpl textBox;

    private OWLEntity entity;

    private final Messages messages;


    @UiField(provided = true)
    final PrimitiveDataListEditor domains;

    interface EditParentsViewImplUiBinder extends UiBinder<HTMLPanel, EditParentsViewImpl> {
    }

    private static EditParentsViewImpl.EditParentsViewImplUiBinder ourUiBinder = GWT.create(EditParentsViewImpl.EditParentsViewImplUiBinder.class);


    @Inject
    public EditParentsViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
    @Nonnull Messages messages) {
        this.messages = messages;
        domains = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.CLASS);
        domains.setPlaceholder(messages.frame_enterAClassName());
        textBox.setEnabled(false);
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void setBusy(boolean busy) {

    }

    @Override
    public void setOwlEntity(OWLEntity entity) {
        this.entity = entity;
    }
}
