package edu.stanford.bmir.protege.web.client.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

@AutoValue
public abstract class ProjectRoleAssignmentsPlace extends Place implements HasProjectId {

    public static ProjectRoleAssignmentsPlace create(@JsonProperty("projectId") ProjectId projectId,
                                                     Optional<Place> nextPlace) {
        return new AutoValue_ProjectRoleAssignmentsPlace(projectId, nextPlace);
    }

    public abstract Optional<Place> getNextPlace();
}
