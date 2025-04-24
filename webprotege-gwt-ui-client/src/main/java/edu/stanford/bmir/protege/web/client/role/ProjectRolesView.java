package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.settings.ApplySettingsHandler;
import edu.stanford.bmir.protege.web.client.settings.CancelSettingsHandler;

public interface ProjectRolesView extends IsWidget {

    AcceptsOneWidget getRoleDefinitionsContainer();

    void setApplySettingsHandler(ApplySettingsHandler applySettingsHandler);

    void setCancelSettingsHandler(CancelSettingsHandler cancelSettingsHandler);
}
