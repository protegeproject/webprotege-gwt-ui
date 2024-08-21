package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.Map;
import java.util.logging.Logger;

public class PostCoordinationPortletViewImpl  extends Composite implements PostCoordinationPortletView {

    Logger logger = java.util.logging.Logger.getLogger("PostCoordinationPortletPresenter");

    @UiField
    HTMLPanel paneContainer;

    private Map<String, PostCoordinationTableAxisLabel> labels;

    private final DispatchServiceManager dispatch;


    private static PostCoordinationPortletViewImpl.PostCoordinationPortletViewImplUiBinder ourUiBinder = GWT.create(PostCoordinationPortletViewImpl.PostCoordinationPortletViewImplUiBinder.class);

    @Inject
    public PostCoordinationPortletViewImpl(DispatchServiceManager dispatch) {
        initWidget(ourUiBinder.createAndBindUi(this));

        this.dispatch = dispatch;
    }

    @Override
    public void setProjectId(ProjectId projectId) {

    }

    @Override
    public void setLabels(Map<String, PostCoordinationTableAxisLabel> labels) {
        this.labels = labels;
        logger.info("ALEX am primit " + labels);
    }

    @Override
    public void setWidget(IsWidget w) {

    }

    @Override
    public void dispose() {

    }


    interface PostCoordinationPortletViewImplUiBinder extends UiBinder<HTMLPanel, PostCoordinationPortletViewImpl> {

    }
}
