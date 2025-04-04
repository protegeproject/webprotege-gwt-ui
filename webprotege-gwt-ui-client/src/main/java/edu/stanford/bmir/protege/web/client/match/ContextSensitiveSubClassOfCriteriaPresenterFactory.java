package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class ContextSensitiveSubClassOfCriteriaPresenterFactory implements CriteriaPresenterFactory<ContextSensitiveSubClassOfCriteria> {

    @Nonnull
    private final Provider<ContextSensitiveSubClassOfCriteriaPresenter> presenterProvider;

    @Inject
    public ContextSensitiveSubClassOfCriteriaPresenterFactory(@Nonnull Provider<ContextSensitiveSubClassOfCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Is a subclass of selected";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends ContextSensitiveSubClassOfCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
