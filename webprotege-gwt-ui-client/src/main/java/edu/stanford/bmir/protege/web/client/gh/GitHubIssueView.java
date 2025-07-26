package edu.stanford.bmir.protege.web.client.gh;

import com.google.auto.factory.AutoFactory;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.gh.GitHubState;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-21
 */
public interface GitHubIssueView extends IsWidget {

    void setIssueTitle(@Nonnull String title);

    void setIssueNumber(int number);

    void setState(GitHubState state);

    void setBody(@Nonnull String body);

    void setHtmlUrl(String s);

    AcceptsOneWidget addLabelContainer();

}
