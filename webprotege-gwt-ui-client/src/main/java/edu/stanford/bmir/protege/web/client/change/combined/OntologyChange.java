package edu.stanford.bmir.protege.web.client.change.combined;

import edu.stanford.bmir.protege.web.shared.change.ProjectChange;

public class OntologyChange implements GeneralProjectChange {
    private ProjectChange projectChange;

    @Override
    public void setOntologyChange(ProjectChange projectChange) {
    this.projectChange = projectChange;
    }

    @Override
    public void setLinearizationChange(ProjectChange projectChange) {
        //Do nothing
    }

    @Override
    public ProjectChange getChange() {
        return projectChange;
    }

    public static OntologyChange createChange(ProjectChange projectChange) {
        OntologyChange ontChange = new OntologyChange();
        ontChange.setOntologyChange(projectChange);
        return ontChange;
    }
}
