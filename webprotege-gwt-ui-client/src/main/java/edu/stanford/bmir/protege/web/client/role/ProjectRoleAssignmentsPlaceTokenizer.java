package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

public class ProjectRoleAssignmentsPlaceTokenizer implements WebProtegePlaceTokenizer<ProjectRoleAssignmentsPlace> {

    private static final String PROJECTS = "projects/";

    private static final String ROLE_ASSIGNMENTS = "/role-assignments";

    private static RegExp regExp = RegExp.compile("^" + PROJECTS + "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})" + RegExp.quote(ROLE_ASSIGNMENTS) + "$");


    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof ProjectRoleAssignmentsPlace;
    }

    @Override
    public ProjectRoleAssignmentsPlace getPlace(String token) {
        MatchResult matchResult = regExp.exec(token);
        if(matchResult == null) {
            return null;
        }
        String projectIdString = matchResult.getGroup(1);
        if(ProjectId.isWelFormedProjectId(projectIdString)) {
            ProjectId projectId = ProjectId.get(projectIdString);
            return ProjectRoleAssignmentsPlace.create(projectId, Optional.empty());
        }
        else {
            return null;
        }
    }

    @Override
    public String getToken(ProjectRoleAssignmentsPlace place) {
        return PROJECTS + place.getProjectId().getId() + ROLE_ASSIGNMENTS;
    }

}
