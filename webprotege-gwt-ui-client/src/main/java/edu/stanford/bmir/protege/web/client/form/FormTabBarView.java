package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public interface FormTabBarView extends IsWidget {

    void addView(FormTabView tabView);

    void clear();

    void setVisible(boolean visible);
}
