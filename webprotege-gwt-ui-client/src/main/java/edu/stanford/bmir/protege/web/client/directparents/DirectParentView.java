package edu.stanford.bmir.protege.web.client.directparents;

import com.google.gwt.user.client.ui.IsWidget;

public interface DirectParentView extends IsWidget {

    IsWidget getView();

    void setEntity(String entityHtml);

    void clear();
}
