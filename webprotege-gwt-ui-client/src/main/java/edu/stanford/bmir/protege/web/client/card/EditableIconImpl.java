package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;

public class EditableIconImpl extends Composite implements EditableIcon, HasVisibility {

    interface EditableIconImplUiBinder extends UiBinder<HTML, EditableIconImpl> {
    }

    private static EditableIconImpl.EditableIconImplUiBinder ourUiBinder = GWT.create(EditableIconImpl.EditableIconImplUiBinder.class);

    @UiField
    HTML editableIcon;

    @Inject
    public EditableIconImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setVisible(boolean visible) {
        this.editableIcon.setVisible(visible);
    }

    @Override
    public void addStyleName(String styleName) {
        this.editableIcon.addStyleName(styleName);
    }
}
