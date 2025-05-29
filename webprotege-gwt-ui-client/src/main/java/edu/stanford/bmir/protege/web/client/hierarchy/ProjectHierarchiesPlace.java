package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.auto.value.AutoValue;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

@AutoValue
public abstract class ProjectHierarchiesPlace extends Place implements HasProjectId {

    public static ProjectHierarchiesPlace get(ProjectId projectId) {
        return new AutoValue_ProjectHierarchiesPlace(projectId);
    }
}
