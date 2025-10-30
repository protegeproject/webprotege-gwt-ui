package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.Optional;

public class EditProjectRolesHandler {

    private final ProjectId projectId;

    private final PlaceController placeController;

    @Inject
    public EditProjectRolesHandler(ProjectId projectId, PlaceController placeController) {
        this.projectId = projectId;
        this.placeController = placeController;
    }

    public void handleEditProjectRoles() {
        Place nextPlace = placeController.getWhere();
        placeController.goTo(ProjectRolesPlace.get(projectId, Optional.ofNullable(nextPlace)));
    }
}
