package edu.stanford.bmir.protege.web.client.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class TabBarViewImpl extends Composite implements TabBarView {

    @Override
    public void addView(TabView tabView) {
        itemContainer.add(tabView);
    }

    interface FormTabBarViewImplUiBinder extends UiBinder<HTMLPanel, TabBarViewImpl> {

    }

    private static FormTabBarViewImplUiBinder ourUiBinder = GWT.create(FormTabBarViewImplUiBinder.class);

    @UiField
    HTMLPanel itemContainer;

    @Inject
    public TabBarViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        itemContainer.clear();
    }
}
