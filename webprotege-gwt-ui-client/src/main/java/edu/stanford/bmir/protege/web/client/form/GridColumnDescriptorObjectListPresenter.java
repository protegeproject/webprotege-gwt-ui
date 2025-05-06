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
    public List<FormDescriptorComponentPresenter> getSubComponentPresenters() {
        List<FormDescriptorComponentPresenter> result = new ArrayList<>();
        result.add(this);
        getObjectPresenters()
                .stream()
                .filter(p -> p instanceof FormDescriptorComponentPresenter)
                .map(p -> (FormDescriptorComponentPresenter) p)
                .forEach(p -> {
                    result.add(p);
                    result.addAll(p.getSubComponentPresenters());
                });
        return result;
    }
}
