package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.settings.ApplySettingsHandler;
import edu.stanford.bmir.protege.web.client.settings.CancelSettingsHandler;
import edu.stanford.bmir.protege.web.client.settings.SettingsSectionViewContainer;
import edu.stanford.bmir.protege.web.client.settings.SettingsView;

import javax.inject.Inject;

public class ProjectRolesViewImpl extends Composite implements ProjectRolesView {

    interface ProjectRolesViewImplUiBinder extends UiBinder<HTMLPanel, ProjectRolesViewImpl> {

    }

    private static ProjectRolesViewImplUiBinder ourUiBinder = GWT.create(ProjectRolesViewImplUiBinder.class);

    @UiField(provided = true)
    protected final SettingsView settingsView;

    private final SettingsSectionViewContainer sectionViewContainer;

    @Inject
    public ProjectRolesViewImpl(SettingsView settingsView,
                                SettingsSectionViewContainer sectionViewContainer) {
        this.settingsView = settingsView;
        this.sectionViewContainer = sectionViewContainer;
        initWidget(ourUiBinder.createAndBindUi(this));
        settingsView.addSectionViewContainer(sectionViewContainer);
    }

    @Override
    public AcceptsOneWidget getRoleDefinitionsContainer() {
        return sectionViewContainer;
    }

    @Override
    public void setApplySettingsHandler(ApplySettingsHandler applySettingsHandler) {
        settingsView.setApplySettingsHandler(applySettingsHandler);
    }

    @Override
    public void setCancelSettingsHandler(CancelSettingsHandler cancelSettingsHandler) {
        settingsView.setCancelSettingsHandler(cancelSettingsHandler);
    }
}