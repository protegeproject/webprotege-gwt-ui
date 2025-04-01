package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class ContextSensitiveSubClassOfCriteriaPresenter implements CriteriaPresenter<ContextSensitiveSubClassOfCriteria> {

    @Nonnull
    private final DirectCheckboxView view;

    @Inject
    public ContextSensitiveSubClassOfCriteriaPresenter(DirectCheckboxView view) {
        this.view = checkNotNull(view);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends ContextSensitiveSubClassOfCriteria> getCriteria() {
        return Optional.of(ContextSensitiveSubClassOfCriteria.get(view.getHierarchyFilterType()));
    }

    @Override
    public void setCriteria(@Nonnull ContextSensitiveSubClassOfCriteria criteria) {
        view.setHierarchyFilterType(criteria.getFilterType());
    }
}
