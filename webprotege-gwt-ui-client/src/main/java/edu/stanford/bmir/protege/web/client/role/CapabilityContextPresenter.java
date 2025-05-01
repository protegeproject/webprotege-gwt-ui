package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.inject.Inject;

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
        criteriaPresenter.start(view.getCriteriaContainer());
        criteriaPresenter.setCriteria(CompositeRootCriteria.any());
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

    public CompositeRootCriteria getCriteria() {
        if(view.isForAnyEntity()) {
            return CompositeRootCriteria.any();
        }
        else {
            return criteriaPresenter.getCriteria().map(c -> (CompositeRootCriteria) c).orElse(CompositeRootCriteria.any());
        }

    }

}
