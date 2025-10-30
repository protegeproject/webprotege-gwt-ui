package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.auto.value.AutoValue;
import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class ProjectHierarchiesPlace extends Place implements HasProjectId {

    public static ProjectHierarchiesPlace get(ProjectId projectId, @Nullable Place nextPlace) {
        return new AutoValue_ProjectHierarchiesPlace(projectId, nextPlace);
    }

    @Nullable
    protected abstract Place getNextPlaceInternal();

    @Nonnull
    public Optional<Place> getNextPlace() {
        return Optional.ofNullable(getNextPlaceInternal());
    }
}
