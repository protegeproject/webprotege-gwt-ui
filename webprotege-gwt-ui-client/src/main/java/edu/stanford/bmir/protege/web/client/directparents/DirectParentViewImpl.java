package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;

public class DirectParentViewImpl extends Composite implements DirectParentView {

    interface DirectParentViewImplUiBinder extends UiBinder<HTMLPanel, DirectParentViewImpl> {
    }

    private static final DirectParentViewImpl.DirectParentViewImplUiBinder ourUiBinder = GWT.create(DirectParentViewImpl.DirectParentViewImplUiBinder.class);

    @UiField
    HTMLPanel entityRenderingField;

    @Inject
    public DirectParentViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public IsWidget getView() {
        return this;
    }

    @Override
    public void setEntity(String entityHtml) {
        this.entityRenderingField.getElement().setInnerHTML(entityHtml);
    }

    @Override
    public void clear() {
        this.entityRenderingField.clear();
    }
}
