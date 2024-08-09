package edu.stanford.bmir.protege.web.client.change.combined;

import edu.stanford.bmir.protege.web.shared.change.ProjectChange;

public class LinearizationChange implements GeneralProjectChange {

    private ProjectChange projectChange;
    @Override
    public void setOntologyChange(ProjectChange projectChange) {
        //Do nothing
    }

    @Override
    public void setLinearizationChange(ProjectChange projectChange) {
        this.projectChange = projectChange;
    }

    @Override
    public ProjectChange getChange() {
        return projectChange;
    }

    public static LinearizationChange createChange(ProjectChange projectChange) {
        LinearizationChange linChange = new LinearizationChange();
        linChange.setLinearizationChange(projectChange);
        return linChange;
    }
}
