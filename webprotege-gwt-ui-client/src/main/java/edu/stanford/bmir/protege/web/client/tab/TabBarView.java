package edu.stanford.bmir.protege.web.client.tab;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public interface TabBarView extends IsWidget {

    void addView(TabView tabView);

    void clear();

    void setVisible(boolean visible);
}
