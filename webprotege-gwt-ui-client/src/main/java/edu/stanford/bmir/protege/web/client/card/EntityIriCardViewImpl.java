package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import javax.inject.Inject;

public class EntityIriCardViewImpl extends Composite {
    interface EntityIriCardViewImplUiBinder extends UiBinder<HTMLPanel, EntityIriCardViewImpl> {
    }

    private static EntityIriCardViewImplUiBinder ourUiBinder = GWT.create(EntityIriCardViewImplUiBinder.class);
    @UiField
    Label iriLabel;
    @UiField
    Label iriLength;

    @Inject
    public EntityIriCardViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setIri(String iri) {
        iriLabel.setText(iri);
        iriLength.setText("Iri Length: " + iri.length());
    }

}