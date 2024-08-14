package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.linearization.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Map;

public interface LinearizationPortletView extends AcceptsOneWidget, IsWidget, HasDispose {

    void setWhoFicEntity(WhoficEntityLinearizationSpecification specification);

    void setLinearizationDefinitonMap(Map<String, LinearizationDefinition> linearizationDefinitonMap);

    void setLinearizationParentsMap(Map<String, EntityNode> linearizationParentsMap);

    void setProjectId(ProjectId projectId);

    void setLinearizationChangeEventHandler(LinearizationChangeEventHandler handler);
}
