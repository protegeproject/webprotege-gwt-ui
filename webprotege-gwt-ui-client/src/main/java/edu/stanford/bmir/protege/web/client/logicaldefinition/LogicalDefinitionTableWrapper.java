package edu.stanford.bmir.protege.web.client.logicaldefinition;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.logicaldefinition.LogicalDefinition;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;

public interface LogicalDefinitionTableWrapper extends AcceptsOneWidget, IsWidget, HasDispose {

    void setEntity(OWLEntity owlEntity);

    void setAncestorList(List<OWLEntityData> ancestorsList);

    void setLabels(List<PostCoordinationTableAxisLabel> labels);

    void addExistingRows(List<LogicalDefinitionTableRow> rows);

    void setParentIRI(String parentIRI);

    void enableReadOnly();

    void enableEditable();

    void asExistingTable();

    void asNewTable();

    void setRemoveTableHandleWrapper(LogicalDefinitionTableWrapperImpl.RemoveTableHandler removeTableHandler);

    LogicalDefinition getLogicalDefinition();
}
