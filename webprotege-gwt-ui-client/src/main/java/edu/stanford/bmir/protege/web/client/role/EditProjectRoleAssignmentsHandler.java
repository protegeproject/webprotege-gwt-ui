package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import java.util.Optional;

public class EditProjectRoleAssignmentsHandler {

    private final ProjectId projectId;

    private final PlaceController placeController;

    @Inject
    public EditProjectRoleAssignmentsHandler(ProjectId projectId, PlaceController placeController) {
        this.projectId = projectId;
        this.placeController = placeController;
    }

    public void handleEditProjectRoleAssignments() {
        Place nextPlace = placeController.getWhere();
        placeController.goTo(ProjectRoleAssignmentsPlace.create(projectId, Optional.ofNullable(nextPlace)));
    }
}
