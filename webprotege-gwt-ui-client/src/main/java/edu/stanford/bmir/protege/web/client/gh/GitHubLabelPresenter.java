package edu.stanford.bmir.protege.web.client.gh;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.gh.GitHubLabel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-21
 */
@AutoFactory
public class GitHubLabelPresenter {

    private final GitHubLabelView view;

    @Inject
    public GitHubLabelPresenter(@Provided GitHubLabelView view) {
        this.view = view;
    }

    public void start(WebProtegeEventBus eventBus, AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void displayLabel(GitHubLabel label) {
        view.setName(label.name());
        view.setColor("#" + label.color());
        view.setDescription(label.description());
    }
}
