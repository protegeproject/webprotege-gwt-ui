package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

public class ManageHierarchiesHandler {

    private final PlaceController placeController;

    private final ProjectId projectId;

    @Inject
    public ManageHierarchiesHandler(PlaceController placeController, ProjectId projectId) {
        this.placeController = placeController;
        this.projectId = projectId;
    }

    public void handleManageHierarchies() {
        Place currentPlace = placeController.getWhere();
        placeController.goTo(ProjectHierarchiesPlace.get(projectId, currentPlace));
    }
}
