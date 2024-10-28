package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

public interface LogicalDefinitionPortletView extends AcceptsOneWidget, IsWidget, HasDispose {

    void setEntity(OWLEntity owlEntity, ProjectId projectId);

    void setEntityData(OWLEntityData entityData);

}
