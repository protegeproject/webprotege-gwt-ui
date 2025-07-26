package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.markdown.Markdown;
import edu.stanford.bmir.protege.web.client.markdown.MarkdownPreviewImpl;
import edu.stanford.bmir.protege.web.shared.gh.GitHubState;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class GitHubIssueViewImpl extends Composite implements GitHubIssueView {

    interface GitHubIssueViewImplUiBinder extends UiBinder<HTMLPanel, GitHubIssueViewImpl> {

    }

    private static GitHubIssueViewImplUiBinder ourUiBinder = GWT.create(GitHubIssueViewImplUiBinder.class);

    @UiField
    Label issueTitle;

    @UiField
    MarkdownPreviewImpl issueBody;

    @UiField
    Anchor htmlUrl;

    @UiField
    HTMLPanel labelContainer;

    @UiField
    Label issueNumber;

    @UiField
    HTMLPanel issueClosedBadge;

    @UiField
    HTMLPanel issueOpenBadge;

    @Inject
    public GitHubIssueViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setIssueTitle(@Nonnull String title) {
        issueTitle.setText(title);
    }

    @Override
    public void setIssueNumber(int number) {
        issueNumber.setText("#" + Integer.toString(number));
    }

    @Override
    public void setState(GitHubState state) {
        issueOpenBadge.setVisible(state.equals(GitHubState.OPEN));
        issueClosedBadge.setVisible(state.equals(GitHubState.CLOSED));
    }

    @Override
    public void setBody(@Nonnull String body) {
        issueBody.setMarkdown(body);
    }

    @Override
    public void setHtmlUrl(String s) {
        htmlUrl.setHref(s);
    }

    @Override
    public AcceptsOneWidget addLabelContainer() {
        SimplePanel container = new SimplePanel();
        labelContainer.add(container);
        return container;
    }
}