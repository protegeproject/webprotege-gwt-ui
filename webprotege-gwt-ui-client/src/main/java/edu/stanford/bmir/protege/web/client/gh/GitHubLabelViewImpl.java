package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;
import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.inject.Inject;

public class GitHubLabelViewImpl extends Composite implements GitHubLabelView {

    interface GitHubLabelViewImplUiBinder extends UiBinder<HTMLPanel, GitHubLabelViewImpl> {

    }

    private static GitHubLabelViewImplUiBinder ourUiBinder = GWT.create(GitHubLabelViewImplUiBinder.class);

    @UiField
    Label name;

    @Inject
    public GitHubLabelViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setName(String title) {
        name.setText(title);
    }

    @Override
    public void setColor(String color) {
        name.getElement().getStyle().setBackgroundColor(color);
    }

    @Override
    public void setTextColor(String color) {
        name.getElement().getStyle().setColor(color);
    }

    @Override
    public void setDescription(String description) {
        name.getElement().setTitle(description);
    }
}