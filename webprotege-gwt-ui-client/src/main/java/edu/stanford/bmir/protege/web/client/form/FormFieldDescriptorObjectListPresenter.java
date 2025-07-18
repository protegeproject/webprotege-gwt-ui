package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.stream.Collectors;

public class FormFieldDescriptorObjectListPresenter extends ObjectListPresenter<FormFieldDescriptor> {

    @Inject
    public FormFieldDescriptorObjectListPresenter(@Nonnull ObjectListView view,
                                                  @Nonnull Provider<ObjectPresenter<FormFieldDescriptor>> objectPresenter,
                                                  @Nonnull Provider<ObjectListViewHolder> objectViewHolderProvider,
                                                  @Nonnull UuidV4Provider uuidV4Provider) {
        super(view, objectPresenter, objectViewHolderProvider,
                () -> getDefaultFormFieldDescriptor(uuidV4Provider));
    }

    private static FormFieldDescriptor getDefaultFormFieldDescriptor(UuidV4Provider uuidSupplier) {
        return FormFieldDescriptor.get(
                FormRegionId.get(uuidSupplier.get()),
                null,
                LanguageMap.empty(),
                FieldRun.START,
                FormFieldDeprecationStrategy.DELETE_VALUES,
                new TextControlDescriptor(LanguageMap.empty(),
                                          StringType.SIMPLE_STRING,
                                          "",
                                          LineMode.SINGLE_LINE,
                                          "",
                                          LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                20,
                Optionality.REQUIRED,
                false,
                ExpansionState.EXPANDED,
                LanguageMap.empty()
        );
    }

    public List<FormFieldDescriptorPresenter> getFormFieldDescriptorPresenters() {
        return super.getObjectPresenters()
                .stream()
                .map(p -> (FormFieldDescriptorPresenter) p)
                .collect(Collectors.toList());
    }
}
