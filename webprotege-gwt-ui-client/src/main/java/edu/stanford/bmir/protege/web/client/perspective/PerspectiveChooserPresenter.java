package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.perspective.GetPerspectiveDetailsAction;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

public class PerspectiveChooserPresenter {

    private final PerspectiveChooserView view;

    private final DispatchServiceManager dispatch;

    private final ProjectId projectId;

    @Inject
    public PerspectiveChooserPresenter(PerspectiveChooserView view, DispatchServiceManager dispatch, ProjectId projectId) {
        this.view = view;
        this.dispatch = dispatch;
        this.projectId = projectId;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        dispatch.execute(GetPerspectiveDetailsAction.create(projectId), result -> {
            view.setAvailablePerspectives(result.getPerspectiveDetails());
        });
    }

    public void setPerspectiveId(@Nonnull PerspectiveId perspectiveId) {
        view.setSelectedPerspective(perspectiveId);
    }

    public void clearPerspectiveId() {
        view.clearSelectedPerspective();
    }

    public Optional<PerspectiveId> getPerspectiveId() {
        return view.getSelectedPerspective();
    }
}
