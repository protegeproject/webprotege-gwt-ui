package edu.stanford.bmir.protege.web.client.card.linearization;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.linearization.LinearizationChangeEventHandler;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.linearization.WhoficEntityLinearizationSpecification;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Map;

public interface LinearizationCardView extends AcceptsOneWidget, IsWidget, HasDispose {

    void setWhoFicEntity(WhoficEntityLinearizationSpecification specification);

    void setLinearizationDefinitonMap(Map<String, LinearizationDefinition> linearizationDefinitonMap);

    void setEntityParentsMap(Map<String, String> linearizationParentsMap);

    void setProjectId(ProjectId projectId);

    void setLinearizationChangeEventHandler(LinearizationChangeEventHandler handler);

    boolean isReadOnly();

    void saveValues(String commitMessage);

    void setEditable();

    void setReadOnly();

    WhoficEntityLinearizationSpecification getLinSpec();

    void setCanEditResiduals(boolean canEditResiduals);

    void setCanViewResiduals(boolean canViewResiduals);
}
