package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveChooserPresenter;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveDetailsListPresenter;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveDetailsAction;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

public class HierarchyDescriptorRulePresenter {

    private final PerspectiveChooserPresenter perspectiveChooserPresenter;

    private final HierarchyDescriptorPresenter hierarchyDescriptorPresenter;

    private final HierarchyDescriptorRuleView view;

    private final DispatchServiceManager dispatch;

    private final ProjectId projectId;

    @Inject
    public HierarchyDescriptorRulePresenter(PerspectiveChooserPresenter perspectiveChooserPresenter, HierarchyDescriptorPresenter hierarchyDescriptorPresenter, HierarchyDescriptorRuleView view, DispatchServiceManager dispatch, ProjectId projectId) {
        this.perspectiveChooserPresenter = perspectiveChooserPresenter;
        this.hierarchyDescriptorPresenter = hierarchyDescriptorPresenter;
        this.view = view;
        this.dispatch = dispatch;
        this.projectId = projectId;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        hierarchyDescriptorPresenter.start(view.getHierarchyDescriptorContainer());
        perspectiveChooserPresenter.start(view.getPerspectiveChooserContainer());
    }

    public void setHierarchyDescriptorRule(HierarchyDescriptorRule value) {
        PerspectiveId perspectiveId = value.getRequiredPerspectiveId();
        if(perspectiveId != null) {
            perspectiveChooserPresenter.setPerspectiveId(perspectiveId);
        }
        else {
            perspectiveChooserPresenter.clearPerspectiveId();
        }
        hierarchyDescriptorPresenter.setHierarchyDescriptor(value.getHierarchyDescriptor());
        view.setCapabilities(new ArrayList<>(value.getRequiredCapabilities()));
    }

    public Optional<HierarchyDescriptorRule> getHierarchyDescriptorRule() {
        return hierarchyDescriptorPresenter.getHierarchyDescriptor()
                .map(hierarchyDescriptor -> {
                    PerspectiveId perspectiveId = perspectiveChooserPresenter.getPerspectiveId().orElse(null);
                    List<Capability> capabilities = view.getCapabilities();
                    return HierarchyDescriptorRule.get(perspectiveId, null, Collections.emptyMap(), null, null, new LinkedHashSet<>(capabilities), hierarchyDescriptor);
                });
    }
}
