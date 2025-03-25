package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;

public class DirectParentViewImpl extends Composite implements DirectParentView {

    interface DirectParentViewImplUiBinder extends UiBinder<HTMLPanel, DirectParentViewImpl> {
    }

    private static final DirectParentViewImpl.DirectParentViewImplUiBinder ourUiBinder = GWT.create(DirectParentViewImpl.DirectParentViewImplUiBinder.class);

    @UiField
    HTMLPanel entityRenderingField;

    private ClickHandler clickHandler = (event) -> {
    };

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
        this.entityRenderingField.addDomHandler(clickHandler, ClickEvent.getType());
    }

    @Override
    public void clear() {
        this.entityRenderingField.clear();
    }

    @Override
    public void setSelectionHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }
}
