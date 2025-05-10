package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class FormDescriptorPresenter implements Presenter, FormDescriptorComponentPresenter {

    @Nonnull
    private final FormDescriptorView view;

    @Nonnull
    private final FormFieldDescriptorObjectListPresenter formFieldDescriptorObjectListPresenter;

    @Nullable
    private FormId formId;

    @Inject
    public FormDescriptorPresenter(@Nonnull FormDescriptorView view,
                                   @Nonnull FormFieldDescriptorObjectListPresenter formFieldDescriptorObjectListPresenter) {
        this.view = checkNotNull(view);
        this.formFieldDescriptorObjectListPresenter = checkNotNull(formFieldDescriptorObjectListPresenter);
        this.formId = FormId.valueOf(UuidV4.uuidv4());
    }

    public void clear() {
        formId = FormId.get(UuidV4.uuidv4());
        formFieldDescriptorObjectListPresenter.clear();
    }

    @Nonnull
    public LanguageMap getFormLabel() {
        return view.getLabel();
    }

    public void setFormId(@Nonnull FormId formId) {
        this.formId = checkNotNull(formId);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(view);
        formFieldDescriptorObjectListPresenter.start(
                view.getFieldDescriptorListContainer(),
                eventBus);
        formFieldDescriptorObjectListPresenter.setDefaultStateCollapsed();
        // TODO: Resource bundle
        formFieldDescriptorObjectListPresenter.setAddObjectText("Add field");
        view.setAddFormFieldHandler(this::handleAddFormElement);
    }

    private void handleAddFormElement() {
        formFieldDescriptorObjectListPresenter.addElement();
    }

    public void setFormDescriptor(@Nonnull FormDescriptor formDescriptor) {
        this.formId = checkNotNull(formDescriptor.getFormId(), "formId in supplied FormDescriptor is null");
        view.setLabel(formDescriptor.getLabel());
        formFieldDescriptorObjectListPresenter.setValues(formDescriptor.getFields());
    }

    @Nonnull
    public FormDescriptor getFormDescriptor() {
        LanguageMap label = view.getLabel();
        List<FormFieldDescriptor> elementDescriptors = formFieldDescriptorObjectListPresenter.getValues();
        return new FormDescriptor(this.formId, label, elementDescriptors, Optional.empty());
    }

    @Override
    public List<FormDescriptorComponentPresenter> getSubComponentPresenters() {
        List<FormDescriptorComponentPresenter> result = new ArrayList<>();
        result.add(this);
        formFieldDescriptorObjectListPresenter.getFormFieldDescriptorPresenters()
                        .forEach(p -> {
                            result.add(p);
                            result.addAll(p.getSubComponentPresenters());
                        });
        return result;
    }
}
