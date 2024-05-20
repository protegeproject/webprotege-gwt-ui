package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-18
 */
public interface GitHubIssuesView extends IsWidget {

    void setContent(String html);
}
