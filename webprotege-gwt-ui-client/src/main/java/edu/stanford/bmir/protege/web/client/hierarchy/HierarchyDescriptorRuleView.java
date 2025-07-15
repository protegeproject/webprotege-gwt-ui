package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.access.Capability;

import javax.annotation.Nonnull;
import java.util.List;

public interface HierarchyDescriptorRuleView extends IsWidget {

    void setCapabilities(List<Capability> capabilities);

    List<Capability> getCapabilities();

    @Nonnull
    AcceptsOneWidget getHierarchyDescriptorContainer();

    @Nonnull
    AcceptsOneWidget getPerspectiveChooserContainer();
}
