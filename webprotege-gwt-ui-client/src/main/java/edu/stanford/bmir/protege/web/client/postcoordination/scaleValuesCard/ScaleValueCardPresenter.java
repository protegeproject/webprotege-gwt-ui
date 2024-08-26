package edu.stanford.bmir.protege.web.client.postcoordination.scaleValuesCard;

import java.util.*;

public class ScaleValueCardPresenter {

    private final ScaleValueCardView view;
    private final String postCoordinationAxis;
    private final List<String> scaleValues;

    public ScaleValueCardPresenter(String postCoordinationAxis, List<String> scaleValues, ScaleValueCardView view) {
        this.view = view;
        this.postCoordinationAxis = postCoordinationAxis;
        this.scaleValues = new ArrayList<>(scaleValues);

        bindView();
        initTable();
    }

    private void bindView() {
        view.setAddButtonClickHandler(event -> addRow("New Value"));
    }

    private void initTable() {
        view.clearTable();
        view.addHeader(postCoordinationAxis);
        view.addSelectValueButton();

        for (String value : scaleValues) {
            view.addRow(value);
        }
    }

    private void addRow(String value) {
        scaleValues.add(value);
        view.addRow(value);
    }

    public ScaleValueCardView getView() {
        return view;
    }

    public List<String> getValues() {
        return scaleValues;
    }
}
