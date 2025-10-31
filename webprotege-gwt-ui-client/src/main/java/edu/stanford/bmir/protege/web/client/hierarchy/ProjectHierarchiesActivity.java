package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nullable;
import java.util.Optional;

public class ProjectHierarchiesActivity extends AbstractActivity {

    private final ProjectHierarchiesPresenter presenter;

    private final Optional<Place> nextPlace;

    public ProjectHierarchiesActivity(ProjectHierarchiesPresenter presenter, Optional<Place> nextPlace) {
        this.presenter = presenter;
        this.nextPlace = nextPlace;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.setNextPlace(nextPlace);
        presenter.start(panel);
    }


}
