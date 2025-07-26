package edu.stanford.bmir.protege.web.client.gh;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.showdown.Showdown;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.gh.GitHubIssue;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-21
 */
@AutoFactory
public class GitHubIssuePresenter {

    private final GitHubIssueView view;

    private final GitHubLabelPresenterFactory labelPresenterFactory;

    private WebProtegeEventBus eventBus;

    @Inject
    public GitHubIssuePresenter(@Provided GitHubIssueView view,
                                @Provided GitHubLabelPresenterFactory labelPresenterFactory) {
        this.view = view;
        this.labelPresenterFactory = labelPresenterFactory;
    }

    public void start(WebProtegeEventBus eventBus, AcceptsOneWidget container) {
        this.eventBus = eventBus;
        container.setWidget(view);

    }

    public void displayIssue(GitHubIssue issue) {
        String bodyInMarkdown = Showdown.renderMarkdown(issue.body());
        view.setIssueTitle(issue.title());
        view.setIssueNumber(issue.number());
        view.setBody(bodyInMarkdown);
        view.setHtmlUrl(issue.htmlUrl());
        view.setState(issue.state());
        issue.labels()
                .forEach(label -> {
                    AcceptsOneWidget labelContainer = view.addLabelContainer();
                    GitHubLabelPresenter labelPresenter = labelPresenterFactory.create();
                    labelPresenter.start(eventBus, labelContainer);
                    labelPresenter.displayLabel(label);
                });
    }

    public void stop() {

    }

}
