package edu.stanford.bmir.protege.web.client.role;

import com.google.common.base.Preconditions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;

public class ProjectRolesViewImpl extends Composite implements ProjectRolesView {

    private ResetProjectRolesHandler resetProjectRolesHandler = () -> {};

    interface ProjectRolesViewImplUiBinder extends UiBinder<HTMLPanel, ProjectRolesViewImpl> {

    }

    private static ProjectRolesViewImplUiBinder ourUiBinder = GWT.create(ProjectRolesViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @UiField
    Button resetRolesButton;

    @Inject
    public ProjectRolesViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("resetRolesButton" )
    public void handleClick(ClickEvent event) {
        resetProjectRolesHandler.handleResetProjectRoles();
    }

    @Override
    public AcceptsOneWidget getRoleDefinitionsContainer() {
        return container;
    }

    @Override
    public void setResetProjectRolesHandler(ResetProjectRolesHandler handler) {
        this.resetProjectRolesHandler = Preconditions.checkNotNull(handler);
    }
}