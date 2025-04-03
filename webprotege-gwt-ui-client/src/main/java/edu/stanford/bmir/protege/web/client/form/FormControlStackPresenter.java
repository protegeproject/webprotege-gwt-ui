package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.ui.HasDisplayContextBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.RegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.ValidationStatus;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public interface FormControlStackPresenter extends HasEnabled, HasRequestFocus, HasPagination, HasValueChangeHandlers<List<FormControlData>>, HasFormRegionFilterChangedHandler, HasDisplayContextBuilder {

    void start(@Nonnull AcceptsOneWidget container);

    void clearValue();

    void setValue(@Nonnull Page<FormControlDataDto> value);

    @Nonnull
    ImmutableList<FormControlData> getValue();

    boolean isNonEmpty();

    boolean isEmpty();

    @Nonnull
    ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject,
                                                         @Nonnull FormRegionId formRegionId);

    void setRegionPageChangedHandler(RegionPageChangedHandler regionPageChangedHandler);

    void forEachFormControl(@Nonnull Consumer<FormControl> formControlConsumer);

    @Nonnull
    ValidationStatus getValidationStatus();

    void setFormDataChangedHandler(@Nonnull FormDataChangedHandler formDataChangedHandler);
}
