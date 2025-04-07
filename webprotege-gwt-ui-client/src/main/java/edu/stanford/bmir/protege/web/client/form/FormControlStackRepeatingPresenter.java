package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.*;
import com.google.common.collect.ImmutableList;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.FormControlStackRepeatingView.FormControlContainer;
import edu.stanford.bmir.protege.web.client.pagination.*;
import edu.stanford.bmir.protege.web.client.ui.*;
import edu.stanford.bmir.protege.web.shared.DisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.pagination.*;

import javax.annotation.*;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

public class FormControlStackRepeatingPresenter implements FormControlStackPresenter, ValueChangeHandler<Optional<FormControlData>> {

    private final Logger logger = Logger.getLogger(FormControlStackRepeatingPresenter.class.getName());

    private final HandlerManager handlerManager = new HandlerManager(this);

    @Nonnull
    private final FormControlStackRepeatingView view;

    @Nonnull
    private final PaginatorPresenter paginatorPresenter;

    @Nonnull
    private final FormControlDataEditorFactory formControlFactory;

    private boolean enabled = true;

    @Nonnull
    private final List<FormControl> formControls = new ArrayList<>();

    private FormRegionPosition position;

    @Nonnull
    private RegionPageChangedHandler regionPageChangedHandler = () -> {
    };

    @Nonnull
    private FormRegionFilterChangedHandler formRegionFilterChangedHandler = event -> {
    };

    private FormDataChangedHandler formDataChangedHandler = () -> {
    };

    private final DisplayContextManager displayContextManager = new DisplayContextManager(context -> {
    });

    @AutoFactory
    @Inject
    public FormControlStackRepeatingPresenter(@Provided @Nonnull FormControlStackRepeatingView view,
                                              @Provided @Nonnull PaginatorPresenter paginatorPresenter,
                                              @Nonnull FormRegionPosition position,
                                              @Nonnull FormControlDataEditorFactory formControlFactory) {
        this.view = checkNotNull(view);
        this.position = checkNotNull(position);
        this.paginatorPresenter = checkNotNull(paginatorPresenter);
        this.formControlFactory = checkNotNull(formControlFactory);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        PaginatorView paginatorView = paginatorPresenter.getView();
        view.getPaginatorContainer().setWidget(paginatorView);
        view.setAddFormControlHandler(this::handleAddControl);
    }

    @Override
    public void clearValue() {
        formControls.clear();
        view.setPaginatorVisible(false);
        // TODO: Fire Value changed
    }

    @Override
    public void setValue(@Nonnull Page<FormControlDataDto> value) {
        // TODO: Pristine?
        // TODO: Reuse form controls
        formControls.clear();
        view.clear();
        view.setPaginatorVisible(value.getPageCount() > 1);
        paginatorPresenter.setPageNumber(value.getPageNumber());
        paginatorPresenter.setPageCount(value.getPageCount());
        paginatorPresenter.setElementCount(value.getTotalElements());
        paginatorPresenter.setPageNumberChangedHandler(page -> regionPageChangedHandler.handleRegionPageChanged());
        value.getPageElements()
                .stream()
                .map(this::createFormControl)
                .forEach(this::addFormControl);
    }

    private void handleAddControl() {
        FormControl formControl = createFormControl(null);
        addFormControl(formControl);
        formControl.requestFocus();
        ValueChangeEvent.fire(this, getValue());
    }

    private void handleRemoveControl(FormControl formControl) {
        formControls.remove(formControl);
        ValueChangeEvent.fire(this, getValue());
    }

    private void addFormControl(FormControl formControl) {
        FormControlContainer container = view.addFormControlContainer();
        container.setWidget(formControl);
        container.setEnabled(enabled);
        formControls.add(formControl);
        container.setRemoveHandler(() -> {
            formControls.remove(formControl);
            view.removeFormControlContainer(container);
            ValueChangeEvent.fire(this, getValue());
            formDataChangedHandler.handleFormDataChanged();
        });
    }

    private FormControl createFormControl(@Nullable FormControlDataDto dto) {
        logger.info("FormControlStackRepeatingPresenter createFormCOntrol:" + dto);
        FormControl formControl = formControlFactory.createFormControl();
        formControl.setPosition(position);
        if (dto != null) {
            formControl.setValue(dto);
        }
        formControl.setEnabled(enabled);
        formControl.addValueChangeHandler(this);
        formControl.setFormRegionFilterChangedHandler(formRegionFilterChangedHandler);
        formControl.setFormDataChangedHandler(formDataChangedHandler);
        if (formControl instanceof ContextSensitiveControl) {
            ((ContextSensitiveControl) formControl).updateContextSensitiveCriteria(displayContextManager.fillDisplayContextBuilder().build());
        }
        return formControl;
    }

    @Override
    public void onValueChange(ValueChangeEvent<Optional<FormControlData>> event) {
        // Just pass on to out listeners
        ValueChangeEvent.fire(this, getValue());
    }

    @Nonnull
    @Override
    public ImmutableList<FormControlData> getValue() {
        return formControls.stream()
                .map(FormControl::getValue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toImmutableList());
    }

    @Override
    public boolean isNonEmpty() {
        return formControls.size() > 0;
    }

    @Override
    public boolean isEmpty() {
        return formControls.stream()
                .map(FormControl::getValue)
                .anyMatch(Optional::isPresent);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<FormControlData>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        formControls.forEach(fc -> fc.setEnabled(enabled));
        view.setEnabled(enabled);
    }

    @Override
    public void requestFocus() {
        formControls.stream().findFirst().ifPresent(FormControl::requestFocus);
    }

    @Override
    public void setPageCount(int pageCount) {
        paginatorPresenter.setPageCount(pageCount);
        // We only show the paginator if we have more than one page of data to display
        view.setPaginatorVisible(pageCount > 1);
    }

    @Override
    public void setPageNumber(int pageNumber) {
        paginatorPresenter.setPageNumber(pageNumber);
    }

    @Override
    public int getPageNumber() {
        return paginatorPresenter.getPageNumber();
    }

    @Override
    public void setPageNumberChangedHandler(PageNumberChangedHandler handler) {
        paginatorPresenter.setPageNumberChangedHandler(handler);
    }

    @Nonnull
    @Override
    public ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject, @Nonnull FormRegionId formRegionId) {
        Stream<FormRegionPageRequest> controlPages = formControls.stream()
                .map(formControl -> formControl.getPageRequests(formSubject, formRegionId))
                .flatMap(ImmutableList::stream);
        PageRequest stackPageRequest = PageRequest.requestPageWithSize(paginatorPresenter.getPageNumber(), FormPageRequest.DEFAULT_PAGE_SIZE);
        Stream<FormRegionPageRequest> stackPage = Stream.of(FormRegionPageRequest.get(formSubject, formRegionId, FormPageRequest.SourceType.CONTROL_STACK, stackPageRequest));
        return Stream.concat(stackPage, controlPages).collect(toImmutableList());
    }

    @Override
    public void setRegionPageChangedHandler(RegionPageChangedHandler regionPageChangedHandler) {
        this.regionPageChangedHandler = checkNotNull(regionPageChangedHandler);
        formControls.forEach(formControl -> formControl.setRegionPageChangedHandler(regionPageChangedHandler));
    }

    @Override
    public void forEachFormControl(@Nonnull Consumer<FormControl> formControlConsumer) {
        formControls.forEach(formControlConsumer);
    }

    @Nonnull
    @Override
    public ValidationStatus getValidationStatus() {
        return formControls.stream()
                .map(FormControl::getValidationStatus)
                .filter(ValidationStatus::isInvalid)
                .findFirst()
                .orElse(ValidationStatus.VALID);
    }

    @Override
    public void setFormDataChangedHandler(@Nonnull FormDataChangedHandler formDataChangedHandler) {
        this.formDataChangedHandler = formDataChangedHandler;
        formControls.forEach(control -> control.setFormDataChangedHandler(formDataChangedHandler));
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {
        this.formRegionFilterChangedHandler = checkNotNull(handler);
    }

    @Override
    public void setParentDisplayContextBuilder(HasDisplayContextBuilder parent) {
        logger.info("FormControlStackRepeatingPresenter: set parent displaycontextbuilder :" + displayContextManager.fillDisplayContextBuilder());
        this.displayContextManager.setParentDisplayContextBuilder(parent);
    }

    @Override
    public DisplayContextBuilder fillDisplayContextBuilder() {
        return displayContextManager.fillDisplayContextBuilder();
    }
}
