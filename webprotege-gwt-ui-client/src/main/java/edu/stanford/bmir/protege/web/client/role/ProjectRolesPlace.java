package edu.stanford.bmir.protege.web.client.role;

import com.google.auto.value.AutoValue;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@AutoValue
public abstract class ProjectRolesPlace extends Place implements HasProjectId {

    public static ProjectRolesPlace get(ProjectId projectId, Optional<Place> nextPlace) {
        return new AutoValue_ProjectRolesPlace(projectId, nextPlace);
    }

    @Nonnull
    public abstract Optional<Place> getNextPlace();

}
