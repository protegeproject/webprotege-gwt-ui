package edu.stanford.bmir.protege.web.client.postcoordination;

import java.util.ArrayList;
import java.util.List;

public class PostCoordinationTableRow {

    private List<PostCoordinationTableCell> cellList = new ArrayList<>();

    public void addCell(PostCoordinationTableCell cell) {
        this.cellList.add(cell);
    }
}
