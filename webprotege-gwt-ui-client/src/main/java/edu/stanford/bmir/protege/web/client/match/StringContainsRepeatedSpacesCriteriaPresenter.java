package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringContainsRepeatedSpacesCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringContainsRepeatedSpacesCriteriaPresenter implements CriteriaPresenter<StringContainsRepeatedSpacesCriteria> {

    @Nonnull
    private final BlankCriteriaView view;

    @Inject
    public StringContainsRepeatedSpacesCriteriaPresenter(@Nonnull BlankCriteriaView view) {
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
    public Optional<StringContainsRepeatedSpacesCriteria> getCriteria() {
        StringContainsRepeatedSpacesCriteria lexicalValueCriteria = StringContainsRepeatedSpacesCriteria.get();
        return Optional.of(lexicalValueCriteria);
    }

    @Override
    public void setCriteria(@Nonnull StringContainsRepeatedSpacesCriteria criteria) {

    }
}
