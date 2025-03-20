package edu.stanford.bmir.protege.web.client.card.logicaldefinition;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.logicaldefinition.LogicalDefinitionChangeHandler;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.LogicalConditions;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

public interface LogicalDefinitionCardView extends AcceptsOneWidget, IsWidget, HasDispose {

    void setEntity(OWLEntity owlEntity, ProjectId projectId);

    void setEntityData(OWLEntityData entityData);

    void switchToEditable();

    void switchToReadOnly();

    void saveValues(String commitMessage);

    LogicalConditions getPristineData();

    LogicalConditions getEditedData();

    OWLEntity getEntity();

    void clearDefinitions();

    void setLogicalDefinitionChangeHandler(LogicalDefinitionChangeHandler handler);
}
