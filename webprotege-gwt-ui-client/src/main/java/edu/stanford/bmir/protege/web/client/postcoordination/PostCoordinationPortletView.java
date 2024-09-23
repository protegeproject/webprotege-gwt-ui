package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.*;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.postcoordination.WhoficEntityPostCoordinationSpecification;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.*;

public interface PostCoordinationPortletView extends AcceptsOneWidget, IsWidget, HasDispose {
    void setProjectId(ProjectId projectId);

    void setLabels(Map<String, PostCoordinationTableAxisLabel> labels);

    void setLinearizationDefinitonMap(Map<String, LinearizationDefinition> linearizationDefinitonMap);

    void initializeTable();

    VerticalPanel getScaleValueCardsView();

    void setTableCellChangedHandler(TableCellChangedHandler handler);

    void setTableData(WhoficEntityPostCoordinationSpecification specification);
}
