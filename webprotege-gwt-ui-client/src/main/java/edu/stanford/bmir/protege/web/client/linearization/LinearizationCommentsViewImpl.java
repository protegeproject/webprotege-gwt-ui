package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionCallback;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.gwtcodemirror.client.GWTCodeMirror;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;
import javax.inject.Inject;


public class LinearizationCommentsViewImpl  extends Composite implements LinearizationCommentsView {

    interface LinearizationCommentsViewImplUiBinder extends UiBinder<HTMLPanel, LinearizationCommentsViewImpl> {

    }

    private static LinearizationCommentsViewImplUiBinder ourUiBinder = GWT.create(LinearizationCommentsViewImplUiBinder.class);

    @UiField(provided = true)
    GWTCodeMirror bodyField;

    private final LinearizationCommentsResourceBundle.LinearizationCommentsCss style = LinearizationCommentsResourceBundle.INSTANCE.style();

    @Inject
    public LinearizationCommentsViewImpl() {
        bodyField = new GWTCodeMirror("gfm");
        bodyField.setAutoCompletionHandler(this::handleAutocomplete);
        bodyField.setLineNumbersVisible(false);
        bodyField.addAutoCompleteCharacter('@');
        bodyField.addStyleName(style.getLinearizationEditor());
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    private void handleAutocomplete(String query, EditorPosition caretPosition, int caretPos, AutoCompletionCallback callback) {
    }

    @Override
    public void clear() {
        bodyField.setValue("");
    }

    @Override
    public void setBody(@Nonnull String body, boolean readonly) {
        Scheduler.get().scheduleDeferred(() -> {
            bodyField.setValue(body);
            bodyField.setEnabled(!readonly);
        });
    }

    @Nonnull
    @Override
    public String getBody() {
        return bodyField.getValue().trim();
    }

    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.of(() -> bodyField.setFocus(true));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Scheduler.get().scheduleDeferred(() -> focus(bodyField.getElement()));
    }

    private void focus(@Nonnull Element element) {
        if("textarea".equalsIgnoreCase(element.getTagName())) {
            element.focus();
            return;
        }
        for(int i = 0; i < element.getChildCount(); i++) {
            Node node = element.getChild(i);
            if(Element.is(node)) {
                focus(Element.as(node));
            }
        }
    }
}
