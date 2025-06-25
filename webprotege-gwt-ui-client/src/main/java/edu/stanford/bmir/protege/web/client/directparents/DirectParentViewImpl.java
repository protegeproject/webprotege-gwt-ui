package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
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

    private String entityIri;

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
    public void setEntity(String entityHtml, String entityIri) {
        this.entityRenderingField.getElement().setInnerHTML(entityHtml);
        this.entityRenderingField.addDomHandler(clickHandler, ClickEvent.getType());
        this.entityIri = entityIri;
    }

    @Override
    public void clear() {
        this.entityRenderingField.clear();
    }

    @Override
    public void setSelectionHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public void markParentAsMain() {
        if (this.entityRenderingField != null) {
            this.entityRenderingField.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        }
    }

    @Override
    public String getEntityIri() {
        return this.entityIri;
    }
}
