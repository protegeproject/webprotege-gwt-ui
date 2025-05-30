package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class ProjectHierarchiesActivity extends AbstractActivity {

    private final ProjectHierarchiesPresenter presenter;

    public ProjectHierarchiesActivity(ProjectHierarchiesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.start(panel);
    }
}
