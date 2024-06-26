package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.core.client.Scheduler;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.client.library.modal.*;

import javax.annotation.Nonnull;
import javax.inject.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/04/2013
 */
public class InputBox {

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final Provider<InputBoxView> viewProvider;

    @Inject
    public InputBox(@Nonnull ModalManager modalManager, @Nonnull Provider<InputBoxView> viewProvider) {
        this.modalManager = checkNotNull(modalManager);
        this.viewProvider = checkNotNull(viewProvider);
    }

    public void showDialog(String title, InputBoxHandler handler) {
        showDialog(title, true, "", handler);
    }

    public void showDialog(String title, boolean multiline, String initialInput, InputBoxHandler handler) {
        showModal(title, "", multiline, initialInput, handler, true);
    }

    public void showDialog(String title, String message, boolean multiline, String initialInput, InputBoxHandler handler) {
        showModal(title, message, multiline, initialInput, handler, true);
    }

    public void showOkDialog(String title, boolean multiline, String initialInput, InputBoxHandler handler) {
        showModal(title, "", multiline, initialInput, handler, false);
    }

    public void showOkDialog(String title, String message, boolean multiline, String initialInput, InputBoxHandler handler) {
        showModal(title, message, multiline, initialInput, handler, false);
    }

    public void showOkDialog(String title, boolean multiline, String initialInput, InputBoxHandler handler, boolean disableFields) {
        showModal(title, "", multiline, initialInput, handler, false, disableFields);
    }

    private void showModal(@Nonnull String title,
                           @Nonnull String message,
                           boolean multiline,
                           @Nonnull String initialInput,
                           InputBoxHandler handler,
                           boolean showCancelButton,
                           boolean disableFields) {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(title);
        InputBoxView view = viewProvider.get();
        view.setMessage(message);
        view.setMultiline(multiline);
        view.setInitialInput(initialInput);
        if (disableFields) {
            view.disableFields();
        }
        modalPresenter.setView(view);
        if (showCancelButton) {
            modalPresenter.setEscapeButton(DialogButton.CANCEL);
        }
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setPrimaryButtonFocusedOnShow(false);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            closer.closeModal();
            handler.handleAcceptInput(view.getInputValue());
        });
        modalManager.showModal(modalPresenter);
        Scheduler.get()
                .scheduleDeferred(() -> {
                    view.getInitialFocusable().ifPresent(HasRequestFocus::requestFocus);
                });
    }

    private void showModal(@Nonnull String title,
                           @Nonnull String message,
                           boolean multiline,
                           @Nonnull String initialInput,
                           InputBoxHandler handler,
                           boolean showCancelButton) {
        showModal(title, message, multiline, initialInput, handler, showCancelButton, false);
    }
}
