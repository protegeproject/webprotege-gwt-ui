package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.place.ProjectSettingsPlace;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

import static java.util.Optional.empty;

public class ProjectRolesPlaceTokenizer implements WebProtegePlaceTokenizer<ProjectRolesPlace> {

    private static final String PROJECTS = "projects/";

    private static final String ROLES = "/roles";

    private static RegExp regExp = RegExp.compile("^" + PROJECTS + "([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})" + ROLES + "$");


    @Override
    public boolean matches(String token) {
        return regExp.test(token);
    }

    @Override
    public boolean isTokenizerFor(Place place) {
        return place instanceof ProjectRolesPlace;
    }

    @Override
    public ProjectRolesPlace getPlace(String token) {
        MatchResult matchResult = regExp.exec(token);
        if(matchResult == null) {
            return null;
        }
        String projectIdString = matchResult.getGroup(1);
        if(ProjectId.isWelFormedProjectId(projectIdString)) {
            ProjectId projectId = ProjectId.get(projectIdString);
            return ProjectRolesPlace.get(projectId, Optional.empty());
        }
        else {
            return null;
        }
    }

    @Override
    public String getToken(ProjectRolesPlace place) {
        return PROJECTS + place.getProjectId().getId() + ROLES;
    }

}
