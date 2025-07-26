package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.gh.GitHubRepositoryCoordinates;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-18
 */
public interface GitHubIssuesView extends IsWidget {

    AcceptsOneWidget addIssueContainer();

    void clearIssueContainers();
}
