package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

public class EditParentsViewImpl extends Composite implements EditParentsView {

    @UiField(provided = true)
    ExpandingTextBox textBox;

    private OWLEntityData entityData;

    private final Messages messages;


    @UiField(provided = true)
    final PrimitiveDataListEditor domains;

    interface EditParentsViewImplUiBinder extends UiBinder<HTMLPanel, EditParentsViewImpl> {
    }

    private static EditParentsViewImpl.EditParentsViewImplUiBinder ourUiBinder = GWT.create(EditParentsViewImpl.EditParentsViewImplUiBinder.class);


    @Inject
    public EditParentsViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                               @Nonnull Messages messages, ExpandingTextBox expandingTextBox) {
        this.messages = messages;
        this.textBox = expandingTextBox;
        domains = new PrimitiveDataListEditor(primitiveDataEditorProvider, PrimitiveType.CLASS);
        domains.setPlaceholder(messages.frame_enterAClassName());
//        domains.setValue(Collections.singletonList(entityData));
        textBox.setEnabled(false);
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void setBusy(boolean busy) {

    }

    @Override
    public void setOwlEntityData(OWLEntityData entity) {
        this.entityData = entity;
    }
}
