package edu.stanford.bmir.protege.web.client.searchClassInHierarchy;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class SearchClassUnderHierarchyPresenter {

    @Nonnull
    private final SearchClassUnderHierarchyView view;

    private SearchSelectionChangedHandler handler = (entity) -> {};

    @Inject
    public SearchClassUnderHierarchyPresenter(@Nonnull SearchClassUnderHierarchyView view) {
        this.view = view;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setSelectionChangedHandler(handler);
    }


    public void setRoots(ClassHierarchyDescriptor classHierarchyDescriptor) {
        List<SubClassOfCriteria> subClassOfCriteria = classHierarchyDescriptor.getRoots()
                .stream()
                .map(root -> SubClassOfCriteria.get(root, HierarchyFilterType.ALL))
                .collect(Collectors.toList());
        CompositeRootCriteria criteria = CompositeRootCriteria.get(
                ImmutableList.copyOf(subClassOfCriteria),
                MultiMatchType.ALL
        );
        this.view.setCriteria(criteria);
    }

    public void setSelectionChangedHandler(SearchSelectionChangedHandler searchSelection) {
        this.handler = searchSelection;
    }
}
