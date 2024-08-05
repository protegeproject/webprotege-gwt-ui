package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;

public class EntityCardViewImpl extends Composite implements EntityCardView {

    interface EntityCardViewImplUiBinder extends UiBinder<HTMLPanel, EntityCardViewImpl> {
    }

    private static EntityCardViewImplUiBinder ourUiBinder = GWT.create(EntityCardViewImplUiBinder.class);
    @UiField
    SimplePanel cardHolder;
    @UiField
    Label cardLabel;

    @Inject
    public EntityCardViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setWidget(IsWidget w) {
        cardHolder.setWidget(w);
    }

    @Override
    public void setLabel(String label) {
        cardLabel.setText(label);
    }
}