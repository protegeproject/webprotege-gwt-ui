package edu.stanford.bmir.protege.web.client.upload;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import edu.stanford.bmir.protege.web.client.library.dlg.ValidationState;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogValidator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 */
public class UploadFileDialogForm extends WebProtegeDialogForm {

    private static final String SUBMIT_FILE_URL = GWT.getModuleBaseURL() + "submitfile";
    //    private static final String SUBMIT_FILE_URL = "/files/submit";

    private final FileUpload fileUpload;
    private final CheckBox overrideCheckbox;

    public static final String FILE_NAME_FIELD_LABEL = "File";
    public static final String OVERRIDE_CHECKBOX_LABEL = "Override Existing";

    public UploadFileDialogForm(boolean showOverrideCheckbox) {
        setPostURL(SUBMIT_FILE_URL);

        fileUpload = new FileUpload();
        fileUpload.setName("file");
        addWidget(FILE_NAME_FIELD_LABEL, fileUpload);
        fileUpload.setWidth("300px");

        if (showOverrideCheckbox) {
            overrideCheckbox = new CheckBox(OVERRIDE_CHECKBOX_LABEL);
            addWidget("", overrideCheckbox);
        } else {
            overrideCheckbox = null;
        }

        addDialogValidator(new FileNameValidator());
    }

    public UploadFileDialogForm() {
        this(false);
    }

    public String getFileName() {
        return fileUpload.getFilename().trim();
    }

    public boolean shouldOverrideExisting() {
        return overrideCheckbox != null && overrideCheckbox.getValue();
    }

    private class FileNameValidator implements WebProtegeDialogValidator {
        public ValidationState getValidationState() {
            return getFileName().isEmpty() ? ValidationState.INVALID : ValidationState.VALID;
        }

        public String getValidationMessage() {
            return "A file name must be specified. Please specify a file name.";
        }
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }
}
