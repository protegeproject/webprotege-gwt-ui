package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public interface PostCoordinationPortletView extends AcceptsOneWidget, IsWidget, HasDispose {
    void setProjectId(ProjectId projectId);
}
