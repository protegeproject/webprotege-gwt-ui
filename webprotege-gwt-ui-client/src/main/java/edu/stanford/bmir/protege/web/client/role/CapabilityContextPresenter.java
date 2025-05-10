package edu.stanford.bmir.protege.web.client.role;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.match.criteria.SubClassOfCriteria;

import javax.inject.Inject;
import java.util.Optional;

public class CapabilityContextPresenter {

    private final EntityCriteriaPresenter criteriaPresenter;

    private final CapabilityContextView view;

    @Inject
    public CapabilityContextPresenter(EntityCriteriaPresenter criteriaPresenter, CapabilityContextView view) {
        this.criteriaPresenter = criteriaPresenter;
        this.view = view;
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        criteriaPresenter.setDisplayAtLeastOneCriteria(true);
        criteriaPresenter.setMatchTextPrefix("matches");
        criteriaPresenter.start(view.getCriteriaContainer());
        criteriaPresenter.setCriteria(createBlankCriteria());
    }

    private static CompositeRootCriteria createBlankCriteria() {
        return CompositeRootCriteria.get(
                ImmutableList.of(
                        SubClassOfCriteria.get(DataFactory.getOWLThing(), HierarchyFilterType.ALL)
                ),
                MultiMatchType.ANY
        );
    }

    public void setCriteria(CompositeRootCriteria criteria) {
        if(criteria.equals(CompositeRootCriteria.any())) {
            view.setForAnyEntity(true);
        }
        else {
            view.setForAnyEntity(false);
            criteriaPresenter.setCriteria(criteria);
        }
    }

    public Optional<CompositeRootCriteria> getCriteria() {
        if(view.isForAnyEntity()) {
            return Optional.empty();
        }
        else {
            return criteriaPresenter.getCriteria().map(c -> (CompositeRootCriteria) c);
        }

    }

    public void clearCriteria() {
        criteriaPresenter.clear();
    }
}
