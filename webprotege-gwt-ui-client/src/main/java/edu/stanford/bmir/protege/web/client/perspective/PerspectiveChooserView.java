package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDetails;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import java.util.List;
import java.util.Optional;

public interface PerspectiveChooserView extends IsWidget {

    void setAvailablePerspectives(List<PerspectiveDetails> perspectives);

    void setSelectedPerspective(PerspectiveId perspectiveId);

    Optional<PerspectiveId> getSelectedPerspective();

    void clearSelectedPerspective();
}
