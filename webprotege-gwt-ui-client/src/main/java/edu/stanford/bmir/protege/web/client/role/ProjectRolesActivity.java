package edu.stanford.bmir.protege.web.client.role;

import com.google.auto.value.AutoValue;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class ProjectRolesActivity extends AbstractActivity implements HasProjectId {

    public static ProjectRolesActivity get(ProjectId projectId, Place nextPlace, ProjectRolesPresenter presenter) {
        return new AutoValue_ProjectRolesActivity(projectId, nextPlace, presenter);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nullable
    public abstract Place getNextPlace();

    public abstract ProjectRolesPresenter getPresenter();

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        getPresenter().setNextPlace(Optional.ofNullable(getNextPlace()));
        getPresenter().start(panel, eventBus);
    }
}
