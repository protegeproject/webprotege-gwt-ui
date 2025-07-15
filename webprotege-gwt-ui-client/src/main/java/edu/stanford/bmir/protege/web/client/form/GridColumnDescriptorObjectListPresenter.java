package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GridColumnDescriptorObjectListPresenter extends ObjectListPresenter<GridColumnDescriptor> implements FormDescriptorComponentPresenter{

    @Inject
    public GridColumnDescriptorObjectListPresenter(@Nonnull ObjectListView view,
                                                   @Nonnull Provider<ObjectPresenter<GridColumnDescriptor>> objectListPresenter,
                                                   @Nonnull Provider<ObjectListViewHolder> objectViewHolderProvider,
                                                   @Nonnull UuidV4Provider uuidV4Provider) {
        super(view, objectListPresenter, objectViewHolderProvider, () -> getDefaultColumnDescriptor(uuidV4Provider));
    }

    private static GridColumnDescriptor getDefaultColumnDescriptor(UuidV4Provider uuidV4Provider) {
        return GridColumnDescriptor.get(
                FormRegionId.get(uuidV4Provider.get()),
                Optionality.REQUIRED,
                Repeatability.NON_REPEATABLE,
                null,
                LanguageMap.empty(),
                TextControlDescriptor.getDefault()
        );
    }

    @Override
    public void addChildren(FormDescriptorComponentPresenterHierarchyNode thisNode) {
        getObjectPresenters()
                .stream()
                .map(presenter -> (GridColumnDescriptorPresenter) presenter)
                .forEach(presenter -> {
                    FormDescriptorComponentPresenterHierarchyNode presenterNode = thisNode.addChildForPresenter(presenter);
                    presenter.addChildren(presenterNode);
                });
    }
}
