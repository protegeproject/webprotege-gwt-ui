package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import com.google.gwt.user.client.ui.VerticalPanel;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;

public class ScaleValueCardPresenter {

    private final ScaleValueCardView view;
    private final PostCoordinationTableAxisLabel postCoordinationAxis;
    private final PostCoordinationScaleValue scaleValues;

    public ScaleValueCardPresenter(PostCoordinationTableAxisLabel postCoordinationAxis, PostCoordinationScaleValue scaleValue, ScaleValueCardView view) {
        this.view = view;
        this.postCoordinationAxis = postCoordinationAxis;
        this.scaleValues = scaleValue;
    }

    private void bindView() {
        view.setAddButtonClickHandler(event -> addRow("New Value"));
    }

    private void initTable() {
        view.clearTable();
        view.addHeader(postCoordinationAxis.getScaleLabel());
        view.addSelectValueButton();

        for (String value : scaleValues.getValueIris()) {
            view.addRow(value);
        }
    }

    private void addRow(String value) {
        scaleValues.getValueIris().add(value);
        view.addRow(value);
    }

    public ScaleValueCardView getView() {
        return view;
    }

    public PostCoordinationScaleValue getValues() {
        return scaleValues;
    }

    public void start(VerticalPanel panel) {
        bindView();
        initTable();
        panel.add(view.asWidget());
    }
}
