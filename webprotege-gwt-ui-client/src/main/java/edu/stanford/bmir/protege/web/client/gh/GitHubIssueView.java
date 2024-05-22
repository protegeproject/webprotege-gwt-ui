package edu.stanford.bmir.protege.web.client.gh;

import com.google.auto.factory.AutoFactory;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-21
 */
public interface GitHubIssueView extends IsWidget {

    void setIssueTitle(@Nonnull String title);

    void setBody(@Nonnull String body);

    void setHtmlUrl(String s);

    AcceptsOneWidget addLabelContainer();

}
