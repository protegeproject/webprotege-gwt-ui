package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.access.CapabilityId;
import edu.stanford.bmir.protege.web.shared.form.FormRegionAccessRestriction;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormFieldDescriptorPresenter implements ObjectPresenter<FormFieldDescriptor>, FormDescriptorComponentPresenter {

    @Nonnull
    private ProjectId projectId;

    @Nonnull
    private final FormFieldDescriptorView view;

    @Nonnull
    private final OwlBindingPresenter bindingPresenter;

    @Nonnull
    private final FormControlDescriptorChooserPresenter fieldDescriptorChooserPresenter;

    @Nonnull
    private Optional<FormRegionId> formFieldId = Optional.empty();

    @Nonnull
    private LanguageMapCurrentLocaleMapper localeMapper = new LanguageMapCurrentLocaleMapper();

    @Nonnull
    private final FormsMessages formsMessages;

    private UuidV4Provider uuidV4Provider;

    @Inject
    public FormFieldDescriptorPresenter(@Nonnull ProjectId projectId,
                                        @Nonnull FormFieldDescriptorView view,
                                        @Nonnull OwlBindingPresenter bindingPresenter,
                                        @Nonnull FormControlDescriptorChooserPresenter fieldChooserPresenter,
                                        @Nonnull FormsMessages formsMessages,
                                        @Nonnull UuidV4Provider uuidV4Provider) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.bindingPresenter = checkNotNull(bindingPresenter);
        this.fieldDescriptorChooserPresenter = checkNotNull(fieldChooserPresenter);
        this.formsMessages = checkNotNull(formsMessages);
        this.uuidV4Provider = uuidV4Provider;
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        String valueForCurrentLocale = localeMapper.getValueForCurrentLocale(view.getLabel());
        if(valueForCurrentLocale.isEmpty()) {
            return formsMessages.missingFieldLabel();
        }
        else {
            return valueForCurrentLocale;
        }
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {
        view.setLabelChangedHandler(elementId -> {
            headerLabelHandler.accept(localeMapper.getValueForCurrentLocale(view.getLabel()));
        });
    }

    @Nonnull
    public Optional<FormFieldDescriptor> getValue() {
        Optional<FormControlDescriptor> formFieldDescriptor = fieldDescriptorChooserPresenter.getFormFieldDescriptor();
        if(!formFieldDescriptor.isPresent()) {
            return Optional.empty();
        }
        FormRegionId formFieldIdToSave = formFieldId.orElseGet(() -> {
            String id = uuidV4Provider.get();
            FormRegionId formFieldId = FormRegionId.get(id);
            this.formFieldId = Optional.of(formFieldId);
            return formFieldId;
        });
        FormFieldDescriptor descriptor = FormFieldDescriptor.get(formFieldIdToSave,
                                                                 bindingPresenter.getBinding().orElse(null),
                                                                 view.getLabel(),
                                                                 view.getFieldRun(),
                                                                 view.getDeprecationStrategy(),
                                                                 formFieldDescriptor.get(),
                                                                 view.getRepeatability(),
                                                                 view.getOptionality(),
                                                                 view.isReadOnly(),
                                                                 view.getInitialExpansionState(),
                                                                 view.getHelp());
        return Optional.of(descriptor);
    }

    public void setValue(@Nonnull FormFieldDescriptor descriptor) {
        bindingPresenter.clear();
        descriptor.getOwlBinding().ifPresent(bindingPresenter::setBinding);

        if(descriptor.getId().getId().equals("")) {
            this.formFieldId = Optional.of(FormRegionId.get(uuidV4Provider.get()));
        }
        else {
            this.formFieldId = Optional.of(descriptor.getId());
        }

        view.setFieldRun(descriptor.getFieldRun());

        view.setLabel(descriptor.getLabel());

        view.setHelp(descriptor.getHelp());

        view.setDeprecationStrategy(descriptor.getDeprecationStrategy());

        view.setRepeatability(descriptor.getRepeatability());

        view.setOptionality(descriptor.getOptionality());

        view.setReadOnly(descriptor.isReadOnly());

        view.setInitialExpansionState(descriptor.getInitialExpansionState());


        FormControlDescriptor formControlDescriptor = descriptor.getFormControlDescriptor();
        fieldDescriptorChooserPresenter.setFormFieldDescriptor(formControlDescriptor);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        fieldDescriptorChooserPresenter.start(view.getFieldDescriptorViewContainer());
        bindingPresenter.start(view.getOwlBindingViewContainer());
    }

    @Override
    public List<FormDescriptorComponentPresenter> getSubComponentPresenters() {
        return Arrays.asList(this, fieldDescriptorChooserPresenter);
    }

    @Override
    public List<FormRegionAccessRestriction> getFormRegionAccessRestrictions() {
        List<FormRegionAccessRestriction> result = new ArrayList<>();
        formFieldId.ifPresent(id -> {
            view.getViewAccessRoles().forEach(r -> {
                FormRegionAccessRestriction accessRestriction = FormRegionAccessRestriction.get(id, r.getRoleId(), CapabilityId.valueOf("ViewFormRegion"), r.getCriteria());
                result.add(accessRestriction);
            });
            view.getEditAccessRoles().forEach(r -> {
                FormRegionAccessRestriction accessRestriction = FormRegionAccessRestriction.get(id, r.getRoleId(), CapabilityId.valueOf("EditFormRegion"), r.getCriteria());
                result.add(accessRestriction);
            });
        });
        return result;
    }

    @Override
    public void setFormRegionAccessRestrictions(List<FormRegionAccessRestriction> formRegionAccessRestrictions) {
        formFieldId.ifPresent(id -> {
            List<RoleCriteriaBinding> viewRoles = formRegionAccessRestrictions.stream()
                    .filter(r -> r.getFormRegionId().equals(id))
                    .filter(r -> r.getCapabilityId().equals(CapabilityId.valueOf("ViewFormRegion")))
                    .map(r -> RoleCriteriaBinding.get(r.getRoleId(), r.getContextCriteria()))
                    .collect(Collectors.toList());
            view.setViewAccessRoles(viewRoles);

            List<RoleCriteriaBinding> editRoles = formRegionAccessRestrictions.stream()
                    .filter(r -> r.getFormRegionId().equals(id))
                    .filter(r -> r.getCapabilityId().equals(CapabilityId.valueOf("EditFormRegion")))
                    .map(r -> RoleCriteriaBinding.get(r.getRoleId(), r.getContextCriteria()))
                    .collect(Collectors.toList());
            view.setEditAccessRoles(editRoles);
        });
    }

    @Override
    public String toString() {
        return "FormFieldDescriptorPresenter{" +
               "formFieldId=" + formFieldId +
               ", label=" + view.getLabel().asMap() +
               '}';
    }
}
