package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.user.client.ui.VerticalPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.GetRenderedOwlEntitiesAction;
import edu.stanford.bmir.protege.web.shared.postcoordination.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.HashSet;

public class ScaleValueCardPresenter {

    private final ScaleValueCardView view;
    private final PostCoordinationTableAxisLabel postCoordinationAxis;
    private final PostcoordinationScaleValue scaleValue;
    private final DispatchServiceManager dispatchServiceManager;
    private final ProjectId projectId;

    private boolean isReadOnly = true;


    public ScaleValueCardPresenter(PostCoordinationTableAxisLabel postCoordinationAxis,
                                   PostcoordinationScaleValue scaleValue,
                                   ScaleValueCardView view,
                                   DispatchServiceManager dispatchServiceManager,
                                   ProjectId projectId) {
        this.view = view;
        this.postCoordinationAxis = postCoordinationAxis;
        this.scaleValue = scaleValue;
        this.dispatchServiceManager = dispatchServiceManager;
        this.projectId = projectId;
    }

    /*
        ToDo:
            implement here the pop-up modal for selecting a scale value
     */
    private void bindView() {
        view.setAddButtonClickHandler(event -> addRow("New Value"));
    }

    private void initTable() {
        view.clearTable();
        view.addHeader(postCoordinationAxis.getScaleLabel(), ScaleAllowMultiValue.fromString(scaleValue.getGenericScale().getAllowMultiValue()));
        view.addSelectValueButton();

        dispatchServiceManager.execute(GetRenderedOwlEntitiesAction.create(projectId, new HashSet<>(scaleValue.getValueIris())),
                result -> {
                    result.getRenderedEntities()
                            .forEach(renderedEntity -> addRow(!renderedEntity.getBrowserText().equals("") ? renderedEntity.getBrowserText() : renderedEntity.getEntity().toStringID()));
                    view.setEditMode(!isReadOnly);
                }
        );

        view.setDeleteValueButtonHandler((value) -> scaleValue.getValueIris().remove(value));

    }

    private void addRow(String value) {
        scaleValue.getValueIris().add(value);
        view.addRow(value);
    }

    public ScaleValueCardView getView() {
        return view;
    }

    public PostcoordinationScaleValue getValues() {
        return scaleValue;
    }

    public void setEditMode(boolean editMode) {
        isReadOnly = !editMode;
        view.setEditMode(editMode);
    }

    public void start(VerticalPanel panel, boolean isEditMode) {
        bindView();
        initTable();
        setEditMode(isEditMode);
        panel.add(view.asWidget());
    }
}
