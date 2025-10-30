package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.place.WebProtegePlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;

public class ProjectHierarchiesPlaceTokenizer implements WebProtegePlaceTokenizer<ProjectHierarchiesPlace> {

        private static final String PROJECTS = "projects/";

        private static final String HIERARCHIES = "/hierarchies";

        private static final RegExp pattern = RegExp.compile(PROJECTS + "(" + UUIDUtil.UUID_PATTERN + ")" + HIERARCHIES);

        @Override
        public boolean matches(String token) {
            return pattern.test(token);
        }

        @Override
        public boolean isTokenizerFor(Place place) {
            return place instanceof ProjectHierarchiesPlace;
        }

        @Override
        public ProjectHierarchiesPlace getPlace(String token) {
            String trimmedToken = token.trim();
            if(!pattern.test(trimmedToken)) {
                return null;
            }
            MatchResult matchResult = pattern.exec(trimmedToken);
            String projectIdString = matchResult.getGroup(1);
            return ProjectHierarchiesPlace.get(ProjectId.get(projectIdString), null);
        }

        @Override
        public String getToken(ProjectHierarchiesPlace place) {
            return PROJECTS + place.getProjectId().getId() + HIERARCHIES;
        }
    }
