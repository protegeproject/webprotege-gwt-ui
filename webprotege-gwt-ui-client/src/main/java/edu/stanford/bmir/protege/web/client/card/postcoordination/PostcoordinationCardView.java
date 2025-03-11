package edu.stanford.bmir.protege.web.client.card.postcoordination;

import com.google.gwt.user.client.ui.VerticalPanel;
import edu.stanford.bmir.protege.web.client.postcoordination.CancelButtonHandler;
import edu.stanford.bmir.protege.web.client.postcoordination.EditButtonHandler;
import edu.stanford.bmir.protege.web.client.postcoordination.SaveButtonHandler;
import edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard.TableCellChangedHandler;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;
import edu.stanford.bmir.protege.web.shared.postcoordination.WhoficEntityPostCoordinationSpecification;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Map;
import java.util.Optional;

public interface PostcoordinationCardView {
    void setProjectId(ProjectId projectId);

    void setLabels(Map<String, PostCoordinationTableAxisLabel> labels);

    void setLinearizationDefinitonMap(Map<String, LinearizationDefinition> linearizationDefinitonMap);

    void initializeTable();

    VerticalPanel getScaleValueCardsView();

    void setTableCellChangedHandler(TableCellChangedHandler handler);

    void setTableData(WhoficEntityPostCoordinationSpecification specification);
    void setEditButtonHandler(EditButtonHandler handler);
    void setCancelButtonHandler(CancelButtonHandler handler);
    void setSaveButtonHandler(SaveButtonHandler handler);
    void setEditMode(boolean editMode);

    Optional<WhoficEntityPostCoordinationSpecification> getTableData();
}
