package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;

public class EntityCardContainerImpl extends Composite implements EntityCardContainer {

    interface EntityCardViewImplUiBinder extends UiBinder<HTMLPanel, EntityCardContainerImpl> {
    }

    private static EntityCardViewImplUiBinder ourUiBinder = GWT.create(EntityCardViewImplUiBinder.class);
    @UiField
    SimplePanel cardHolder;

    @Inject
    public EntityCardContainerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setWidget(IsWidget w) {
        cardHolder.setWidget(w);
    }
}