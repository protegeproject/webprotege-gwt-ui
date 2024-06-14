package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;

public interface DuplicateEntitiesView extends HasBusy, IsWidget {


    AcceptsOneWidget getDuplicateResultsContainer();
}
