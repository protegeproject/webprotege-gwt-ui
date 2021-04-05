package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.DateIsBeforeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class DateIsBeforePresenter implements CriteriaPresenter<DateIsBeforeCriteria> {

    @Nonnull
    private final DateView view;

    @Inject
    public DateIsBeforePresenter(@Nonnull DateView view) {
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
    public Optional<DateIsBeforeCriteria> getCriteria() {
        DateIsBeforeCriteria criteria = DateIsBeforeCriteria.get(
                view.getYear(),
                view.getMonth(),
                view.getDay()
        );
        return Optional.of(criteria);
    }

    @Override
    public void setCriteria(@Nonnull DateIsBeforeCriteria criteria) {
        view.setYear(criteria.getYear());
        view.setMonth(criteria.getMonth());
        view.setDay(criteria.getDay());
    }
}
