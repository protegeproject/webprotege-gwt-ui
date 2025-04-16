package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.inject.Inject;
import java.util.Optional;

public class EntityCardUiImpl extends Composite implements EntityCardUi {

    interface EntityCardUiImplUiBinder extends UiBinder<HTMLPanel, EntityCardUiImpl> {
    }

    private static EntityCardUiImplUiBinder ourUiBinder = GWT.create(EntityCardUiImplUiBinder.class);

    @UiField
    SimplePanel cardHolder;

    @Inject
    public EntityCardUiImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setWidget(IsWidget w) {
        cardHolder.setWidget(w);
    }

    @Override
    public void setWritable(boolean writable) {
        if (writable) {
            cardHolder.addStyleName(WebProtegeClientBundle.BUNDLE.style().entityCard__writable());
        }
        else {
            cardHolder.removeStyleName(WebProtegeClientBundle.BUNDLE.style().entityCard__writable());
        }
    }
}