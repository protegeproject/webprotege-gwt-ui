package edu.stanford.bmir.protege.web.client.postcoordination;

import edu.stanford.bmir.protege.web.client.card.*;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;

import java.util.*;

public class PostCoordinationTableRow {

    private final LinearizationDefinition linearizationDefinition;

    private final EditableIcon editableIconFront;
    private final EditableIcon editableIconBack;

    private final List<PostCoordinationTableCell> cellList = new ArrayList<>();

    public PostCoordinationTableRow(LinearizationDefinition linearizationDefinition) {
        this.linearizationDefinition = linearizationDefinition;
        this.editableIconFront = new EditableIconImpl();
        this.editableIconBack = new EditableIconImpl();

        this.editableIconBack.setVisible(false);
        this.editableIconFront.setVisible(false);
    }

    public void addCell(PostCoordinationTableCell cell) {
        this.cellList.add(cell);
    }

    public boolean isDerived() {
        return linearizationDefinition.getCoreLinId() != null && !linearizationDefinition.getCoreLinId().isEmpty();
    }

    public LinearizationDefinition getLinearizationDefinition() {
        return linearizationDefinition;
    }

    public List<PostCoordinationTableCell> getCellList() {
        return cellList;
    }

    public EditableIcon getEditableIconFront() {
        return editableIconFront;
    }

    public EditableIcon getEditableIconBack() {
        return editableIconBack;
    }
}
