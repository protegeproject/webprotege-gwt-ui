package edu.stanford.bmir.protege.web.client.hierarchy;

import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLEntity;

public final class TreeWidgetUpdaterFactory {
  private final Provider<ProjectId> projectIdProvider;

  @Inject
  public TreeWidgetUpdaterFactory(Provider<ProjectId> projectIdProvider) {
    this.projectIdProvider = checkNotNull(projectIdProvider, 1);
  }

  public TreeWidgetUpdater create(
      TreeWidget<EntityNode, OWLEntity> treeWidget, EntityHierarchyModel hierarchyModel) {
    return new TreeWidgetUpdater(
        checkNotNull(projectIdProvider.get(), 1),
        checkNotNull(treeWidget, 2),
        checkNotNull(hierarchyModel, 3));
  }

  private static <T> T checkNotNull(T reference, int argumentIndex) {
    if (reference == null) {
      throw new NullPointerException(
          "Constructor argument is null but is not marked @Nullable. Argument index: "
              + argumentIndex);
    }
    return reference;
  }
}
