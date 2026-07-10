package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class LanguageMapEntryPresenter implements ValueEditor<LanguageMapEntry>, HasRequestFocus, HasPlaceholder {

    /**
     * Applied when a row has label text but no explicit language tag, so a value is
     * never silently dropped just because its language wasn't set (see #281).
     */
    private static final String DEFAULT_LANG_TAG = "en";

    @Nonnull
    private final LanguageMapEntryView view;

    @Nonnull
    private final HandlerManager handlerManager = new HandlerManager(this);

    @Inject
    public LanguageMapEntryPresenter(@Nonnull LanguageMapEntryView view) {
        this.view = checkNotNull(view);
        this.view.setValueChangedHandler(value -> ValueChangeEvent.fire(this, getValue()));
        this.view.setLangTagChangedHandler(value -> ValueChangeEvent.fire(this, getValue()));
    }

    @Override
    public void setValue(LanguageMapEntry object) {
        view.setLangTag(object.getLangTag());
        view.setValue(object.getValue());
    }

    @Override
    public void clearValue() {
        view.setValue("");
        view.setLangTag("");
    }

    @Override
    public Optional<LanguageMapEntry> getValue() {
        String value = view.getValue().trim();
        // A row is only ever complete once it has actual text: a tag alone
        // (e.g. set before the label text is typed) isn't a meaningful entry
        // on its own, and treating it as one sent a premature, empty-value
        // save that could race with -- and sometimes overwrite -- the real
        // save that follows once the text is typed.
        if(value.isEmpty()) {
            return Optional.empty();
        }
        String langTag = view.getLangTag().trim();
        if(langTag.isEmpty()) {
            langTag = DEFAULT_LANG_TAG;
        }
        return Optional.of(LanguageMapEntry.get(langTag, value));
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<LanguageMapEntry>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return handlerManager.addHandler(DirtyChangedEvent.TYPE, handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }

    @Override
    public void requestFocus() {
        view.requestFocus();
    }

    @Override
    public String getPlaceholder() {
        return view.getPlaceholder();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        view.setPlaceholder(placeholder);
    }
}
