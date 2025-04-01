package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public interface DirectCheckboxView extends IsWidget {

    HierarchyFilterType getHierarchyFilterType();

    void setHierarchyFilterType(@Nonnull HierarchyFilterType filterType);
}
