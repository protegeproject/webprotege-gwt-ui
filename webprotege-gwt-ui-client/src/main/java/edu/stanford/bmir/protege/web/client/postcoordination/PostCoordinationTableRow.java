package edu.stanford.bmir.protege.web.client.postcoordination;

import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;

import java.util.*;

public class PostCoordinationTableRow {

    private final LinearizationDefinition linearizationDefinition;

    private final List<PostCoordinationTableCell> cellList = new ArrayList<>();

    public PostCoordinationTableRow(LinearizationDefinition linearizationDefinition) {
        this.linearizationDefinition = linearizationDefinition;
    }

    public void addCell(PostCoordinationTableCell cell) {
        this.cellList.add(cell);
    }

    public void updateDerivedCell(PostCoordinationTableCell changedCell) {
        if (isDerived() && linearizationDefinition.getCoreLinId().equalsIgnoreCase(changedCell.getLinearizationDefinition().getId())) {
            Optional<PostCoordinationTableCell> equivalentCell = this.cellList.stream()
                    .filter(cell -> cell.getAxisLabel().getPostCoordinationAxis().equalsIgnoreCase(changedCell.getAxisLabel().getPostCoordinationAxis()))
                    .findFirst();
            equivalentCell.ifPresent(cell -> {
                if (!cell.isTouched() || cell.getValue().startsWith("DEFAULT")) {
                    cell.setParentValue(changedCell.getAsCheckboxValue());
                    cell.setValue("DEFAULT_" + changedCell.getValue());
                }
            });
        }
    }

    public boolean isDerived() {
        return linearizationDefinition.getCoreLinId() != null && !linearizationDefinition.getCoreLinId().isEmpty();
    }

    public LinearizationDefinition getLinearizationDefinition() {
        return linearizationDefinition;
    }

    public void bindToParentRow(List<PostCoordinationTableRow> tableRows) {
        if (isDerived()) {
            PostCoordinationTableRow parentRow = findParentRow(linearizationDefinition.getCoreLinId(), tableRows);
            bindCellsToParentCells(parentRow);
        }
    }


    private void bindCellsToParentCells(PostCoordinationTableRow parentRow) {
        for (PostCoordinationTableCell parentCell : parentRow.cellList) {
            Optional<PostCoordinationTableCell> cellToUpload = this.cellList.stream()
                    .filter(myCell -> myCell.getAxisLabel().getPostCoordinationAxis().equalsIgnoreCase(parentCell.getAxisLabel().getPostCoordinationAxis()))
                    .findFirst();

            cellToUpload.ifPresent(cell -> cell.setParentCell(parentCell));
            cellToUpload.ifPresent(cell -> cell.setParentValue(parentCell.getAsCheckboxValue()));
        }
    }

    PostCoordinationTableRow findParentRow(String parentIRI, List<PostCoordinationTableRow> rows) {
        return rows.stream().filter(row -> row.linearizationDefinition.getId().equalsIgnoreCase(parentIRI))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Parent not found"));
    }

    public List<PostCoordinationTableCell> getCellList() {
        return cellList;
    }
}
