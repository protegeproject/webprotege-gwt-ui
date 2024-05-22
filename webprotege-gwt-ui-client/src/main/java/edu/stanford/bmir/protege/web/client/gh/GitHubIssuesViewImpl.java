package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.inject.Inject;

public class GitHubIssuesViewImpl extends Composite implements GitHubIssuesView {

    interface GitHubIssuesViewImplUiBinder extends UiBinder<HTMLPanel, GitHubIssuesViewImpl> {

    }

    private static GitHubIssuesViewImplUiBinder ourUiBinder = GWT.create(GitHubIssuesViewImplUiBinder.class);

    @UiField
    HTMLPanel container;

    @Inject
    public GitHubIssuesViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setContent(String html) {
        container.getElement().setInnerHTML(html);
    }

    @Override
    public AcceptsOneWidget addIssueContainer() {
        SimplePanel issueContainer = new SimplePanel();
        container.add(issueContainer);
        return issueContainer;
    }

    @Override
    public void clearIssueContainers() {
        for(int i = container.getWidgetCount() - 1; i > -1; i--) {
            container.remove(i);
        }
    }
}