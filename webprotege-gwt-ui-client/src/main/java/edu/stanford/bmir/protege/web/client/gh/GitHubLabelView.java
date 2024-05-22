package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.color.Color;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-21
 */
public interface GitHubLabelView extends IsWidget {

    void setName(String title);

    void setColor(String color);

    void setDescription(String description);
}
