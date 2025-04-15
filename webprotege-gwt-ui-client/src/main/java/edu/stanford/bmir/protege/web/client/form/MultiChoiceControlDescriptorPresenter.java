package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceListSourceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.MultiChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-10
 */
public class MultiChoiceControlDescriptorPresenter implements FormControlDescriptorPresenter {

    @Nonnull
    private final MultiChoiceControlDescriptorView view;

    @Nonnull
    private final ChoiceListSourceDescriptorPresenter choiceListSourceDescriptorPresenter;

    @Inject
    public MultiChoiceControlDescriptorPresenter(@Nonnull MultiChoiceControlDescriptorView view,
                                                 @Nonnull ChoiceListSourceDescriptorPresenter choiceListSourceDescriptorPresenter) {
        this.view = checkNotNull(view);
        this.choiceListSourceDescriptorPresenter = checkNotNull(choiceListSourceDescriptorPresenter);
    }

    @Override
    public void clear() {
        choiceListSourceDescriptorPresenter.clear();
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        choiceListSourceDescriptorPresenter.start(view.getSourceContainer());
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        ChoiceListSourceDescriptor choiceListSourceDescriptor = choiceListSourceDescriptorPresenter.getDescriptor();
        return MultiChoiceControlDescriptor.get(choiceListSourceDescriptor,
                                                ImmutableList.of());
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {
        if(formControlDescriptor instanceof MultiChoiceControlDescriptor) {
            MultiChoiceControlDescriptor descriptor = (MultiChoiceControlDescriptor) formControlDescriptor;
            choiceListSourceDescriptorPresenter.setDescriptor(descriptor.getSource());
        }
        else {
            clear();
        }
    }

    @Override
    public List<FormDescriptorComponentPresenter> getSubComponentPresenters() {
        return Collections.singletonList(this);
    }

}
