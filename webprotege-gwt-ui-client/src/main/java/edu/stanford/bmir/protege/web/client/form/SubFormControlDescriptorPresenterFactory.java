package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-21
 */
public class SubFormControlDescriptorPresenterFactory implements FormControlDescriptorPresenterFactory {

    @Nonnull
    private Provider<SubFormControlDescriptorPresenter> presenterProvider;

    @Inject
    public SubFormControlDescriptorPresenterFactory(@Nonnull Provider<SubFormControlDescriptorPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDescriptorLabel() {
        return "Sub-form";
    }

    @Nonnull
    @Override
    public String getDescriptorType() {
        return SubFormControlDescriptor.getFieldTypeId();
    }

    @Nonnull
    @Override
    public FormControlDescriptor createDefaultDescriptor() {
        return new SubFormControlDescriptor(FormDescriptor.empty(FormId.valueOf(UuidV4.uuidv4())));
    }

    @Nonnull
    @Override
    public FormControlDescriptorPresenter create() {
        return presenterProvider.get();
    }
}
