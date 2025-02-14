package edu.stanford.bmir.protege.web.client.change.combined;

import edu.stanford.bmir.protege.web.shared.change.ProjectChange;

public interface GeneralProjectChange {
    void setOntologyChange(ProjectChange projectChange);
    void setLinearizationChange(ProjectChange projectChange);
    ProjectChange getChange();
}
