package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.permissions.ProjectRoleSelectorPresenter;
import edu.stanford.bmir.protege.web.client.role.CapabilityContextPresenter;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;

import javax.inject.Inject;
import java.util.Optional;

public class FormRegionRoleCriteriaPresenter {

    private final FormRegionRoleCriteriaView view;

    private final ProjectRoleSelectorPresenter projectRolesPresenter;

    private final CapabilityContextPresenter capabilityContextPresenter;

    @Inject
    public FormRegionRoleCriteriaPresenter(FormRegionRoleCriteriaView view, ProjectRoleSelectorPresenter projectRolesPresenter, CapabilityContextPresenter capabilityContextPresenter) {
        this.view = view;
        this.projectRolesPresenter = projectRolesPresenter;
        this.capabilityContextPresenter = capabilityContextPresenter;
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        projectRolesPresenter.start(view.getRoleIdContainer());
        capabilityContextPresenter.start(view.getContextCriteriaContainer());
    }

    public void setValue(RoleCriteriaBinding restriction) {
        projectRolesPresenter.setValue(restriction.getRoleId());
        capabilityContextPresenter.setCriteria(restriction.getCriteria());
    }

    public Optional<RoleCriteriaBinding> getValue() {
            return projectRolesPresenter.getValue().map(roleId -> {
                return capabilityContextPresenter.getCriteria()
                        .map(criteria -> {
                            return RoleCriteriaBinding.get(roleId, criteria);
                        })
                        .orElse(
                                RoleCriteriaBinding.get(roleId)
                        );
            });
    }
}
