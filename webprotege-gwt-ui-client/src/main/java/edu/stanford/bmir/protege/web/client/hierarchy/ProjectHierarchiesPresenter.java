package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.hierarchy.GetProjectHierarchyDescriptorRulesAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.SetProjectHierarchyDescriptorRulesAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.SetProjectHierarchyDescriptorRulesResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

public class ProjectHierarchiesPresenter {

    private final ProjectId projectId;

    private final SettingsPresenter settingsPresenter;

    private final HierarchyDescriptorRulesPresenter descriptorRulesPresenter;

    private final DispatchServiceManager dispatch;

    private final MessageBox messageBox;

    @Inject
    public ProjectHierarchiesPresenter(ProjectId projectId, SettingsPresenter settingsPresenter, HierarchyDescriptorRulesPresenter descriptorRulesPresenter, DispatchServiceManager dispatch, MessageBox messageBox) {
        this.projectId = projectId;
        this.settingsPresenter = settingsPresenter;
        this.descriptorRulesPresenter = descriptorRulesPresenter;
        this.dispatch = dispatch;
        this.messageBox = messageBox;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        settingsPresenter.start(container);
        AcceptsOneWidget hierarchyRulesContainer = settingsPresenter.addSection("Hierarchy rules");
        descriptorRulesPresenter.start(hierarchyRulesContainer);
        settingsPresenter.setApplySettingsHandler(this::handleApply);
        dispatch.execute(GetProjectHierarchyDescriptorRulesAction.create(projectId),
                result -> {
                    descriptorRulesPresenter.setRules(result.getRules());
                });
    }

    private void handleApply() {
        List<HierarchyDescriptorRule> rules = descriptorRulesPresenter.getRules();
        dispatch.execute(SetProjectHierarchyDescriptorRulesAction.create(projectId, rules),
                result -> {
                    messageBox.showMessage("Rules saved", "Hierarchy rules have been saved");
                });
    }

}
