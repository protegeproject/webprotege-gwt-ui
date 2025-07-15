package edu.stanford.bmir.protege.web.client.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.form.LanguageMapCurrentLocaleMapper;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class TabViewImpl extends Composite implements TabView {

    public static final String SELECTED_ITEM_STYLE = WebProtegeClientBundle.BUNDLE.style()
                                                                                  .formTabBar__tab__selected();
    private Color color;
    private Color backgroundColor;

    interface FormTabViewImplUiBinder extends UiBinder<HTMLPanel, TabViewImpl> {

    }

    private static FormTabViewImplUiBinder ourUiBinder = GWT.create(FormTabViewImplUiBinder.class);

    @UiField
    Label label;

    @UiField
    HTMLPanel dirtyIndicator;

    private HandlerRegistration handlerRegistration = () -> {};

    private final LanguageMapCurrentLocaleMapper localeMapper = new LanguageMapCurrentLocaleMapper();

    @Inject
    public TabViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        dirtyIndicator.setVisible(false);
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {
        this.label.setText(localeMapper.getValueForCurrentLocale(label));
    }

    @Override
    public void setSelected(boolean selected) {
        if(selected) {
            addStyleName(SELECTED_ITEM_STYLE);
            label.getElement().getStyle().clearColor();
            getElement().getStyle().clearBackgroundColor();
        }
        else {
            removeStyleName(SELECTED_ITEM_STYLE);
            if (color != null) {
                label.getElement().getStyle().setColor(color.getHex());
            }
            if(backgroundColor != null) {
                getElement().getStyle().setBackgroundColor(backgroundColor.getHex());
            }
        }
    }

    @Override
    public void setDirty(boolean dirty) {
        dirtyIndicator.setVisible(dirty);
    }

    @Override
    public void setClickHandler(@Nonnull ClickHandler clickHandler) {
        handlerRegistration.removeHandler();
        handlerRegistration = label.addClickHandler(clickHandler);
    }

    @Override
    public void setColor(@Nonnull Color color) {
        this.color = color;
    }

    @Override
    public void setBackgroundColor(@Nonnull Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        getElement().getStyle().setBackgroundColor(backgroundColor.getHex());
    }
}
