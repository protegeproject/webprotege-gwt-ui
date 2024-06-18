package edu.stanford.bmir.protege.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import org.checkerframework.checker.units.qual.K;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-23
 */
@DefaultLocale
public interface FormsMessages extends com.google.gwt.i18n.client.Messages {

    public static final FormsMessages MSG = GWT.create(FormsMessages.class);

    @DefaultMessage("Expand/collapse element")
    @Key("expandCollapse")
    String expandCollapse();

    @DefaultMessage("Some forms were not copied")
    @Key("formsNotCopied.title")
    String formsNotCopied_title();

    @DefaultMessage("Some forms (<b>{0}</b>) were not copied because they are already present in this project.  If you would like to replace the existing forms you must first delete them.")
    @Key("formsNotCopied.message")
    String formsNotCopied_message(String uncopiedFormsList);

    @DefaultMessage("Forms")
    @Key("forms.title")
    String forms_Title();

    @DefaultMessage("No form selected")
    @Key("noFormSelected")
    String noFormSelected();

    @DefaultMessage("Please select a form to continue")
    @Key("noFormSelected_description")
    String noFormSelected_description();

    @DefaultMessage("Available forms")
    @Key("availableForms")
    String availableForms();

    @DefaultMessage("Add form")
    @Key("addForm")
    String addForm();

    @DefaultMessage("Copy forms from project")
    @Key("copyFormsFromProject_title")
    String copyFormsFromProject_title();

    @DefaultMessage("You can all or some of the forms from any other projects that you mange to this project.  Select a project and then choose one or more forms from that project.")
    @Key("copyFormsFromProject_explanation")
    String copyFormsFromProject_explanation();

    @DefaultMessage("Select the project that you would like to copy the forms from")
    @Key("copyFormsFromProject_help")
    String copyFormsFromProject_help();

    @DefaultMessage("Delete form")
    @Key("deleteForm")
    String deleteForm();

    @DefaultMessage("Edit form")
    @Key("editForm")
    String editForm();


    @DefaultMessage("Add form element")
    @Key("addFormElement")
    String addFormElement();


    @DefaultMessage("Enter help text")
    @Key("enterHelpText")
    String enterHelpText();

    @DefaultMessage("Enter label")
    @Key("enterLabel")
    String enterLabel();

    @DefaultMessage("Enter placeholder")
    @Key("enterPlaceholder")
    String enterPlaceholder();

    @DefaultMessage("Project Forms")
    @Key("projectForms.title")
    String projectForms_Title();

    @DefaultMessage("Delete")
    @Key("removeFormElement")
    String removeFormElement();

    @DefaultMessage("Move down")
    @Key("moveFormElementDown")
    String moveFormElementDown();


    @DefaultMessage("Move up")
    @Key("moveFormElementUp")
    String moveFormElementUp();

    @DefaultMessage("Delete <b>{0}</b>?")
    @Key("deleteFormElementConfirmation.title")
    String deleteFormElementConfirmation_Title(String formElementId);

    @DefaultMessage("Are you sure that you want to delete the <b>{0}</b> form?  This cannot be undone.")
    @Key("deleteFormElementConfirmation.message")
    String deleteFormElementConfirmation_Message(String formElementId);

    @DefaultMessage("Please select the project that you wish to export the form to")
    @Key("selectProjectToExportTo")
    String selectProjectToExportTo();

    @DefaultMessage("Subject annotations")
    @Key("subForm.subject.annotations")
    String subFormSubjectAnnotations();

    @DefaultMessage("Subject parents/types")
    @Key("subForm.subject.parents")
    String subFormSubjectParents();

    @DefaultMessage("Subject generated name pattern")
    @Key("subForm.subject.pattern")
    String subFormSubjectGeneratedNamePattern();

    @DefaultMessage("Subject type")
    @Key("subForm.subject.type")
    String subFormSubjectType();

    @DefaultMessage("Edit values")
    @Key("edit")
    String edit();

    @DefaultMessage("Apply edits")
    @Key("edits.apply")
    String edits_Apply();

    @DefaultMessage("Cancel edits")
    @Key("edits.cancel")
    String edits_Cancel();

    @DefaultMessage("Visible columns")
    @Key("grid.visibleColumns")
    String grid_visibleColumns();

    @DefaultMessage("Missing Field Label")
    @Key("missingFieldLabel")
    String missingFieldLabel();

    @DefaultMessage("Column  Filter")
    @Key("grid.columnFilter")
    String grid_columnFilter();

    @DefaultMessage("Filtered")
    @Key("filtered")
    String filtered();

    @DefaultMessage("Clear filters")
    @Key("clearFilters")
    String clearFilters();

    @DefaultMessage("No label for form set")
    @Key("noLabelForFormSet")
    String noLabelForFormSet();

    @DefaultMessage("Export forms...")
    @Key("exportForms")
    String exportFormsFromProject_title();

    @DefaultMessage("Import forms...")
    @Key("importForms.title")
    String importFormsIntoProject_title();

    @DefaultMessage("Please paste the forms description into the text area below")
    @Key("importForms.message")
    String importFormsIntoProject_message();

    @DefaultMessage("Forms import failed")
    @Key("importForms.error.title")
    String importFormsIntoProject_error_title();

    @DefaultMessage("An error occurred when importing the specified forms descriptors")
    @Key("importForms.error.message")
    String importFormsIntoProject_error_message();

    @DefaultMessage("Form activation")
    @Key("formActivation")
    String activation();

    @DefaultMessage("Activation criteria")
    @Key("activationCriteria")
    String activationCriteria();

    @DefaultMessage("Form purpose")
    @Key("formPurpose")
    String formPurpose();

    @DefaultMessage("Entity viewing/editing")
    @Key("formPurpose.entityViewingEditing")
    String formPurpose_entityViewingAndEditing();

    @DefaultMessage("Entity creation")
    @Key("formPurpose.entityCreation")
    String formPurpose_entityCreation();

    @DefaultMessage("Parent entity criteria")
    @Key("entityCreation.parentEntityCriteria")
    String entityCreation_parentEntityCriteria();

    @DefaultMessage("Entity deprecation")
    @Key("formPurpose.entityDeprecation")
    String formPurpose_entityDeprecation();

    @DefaultMessage("Entity criteria")
    @Key("entityEditing.entityCriteria")
    String entityEditing_entityCriteria();

    @DefaultMessage("Entity criteria")
    @Key("entityDeprecation.entityCriteria")
    String entityDeprecation_entityCriteria();

    @DefaultMessage("Save edits before switching?")
    @Key("alerts.apply.edits.title")
    String saveEditsAlert_Title();

    @DefaultMessage("Do you want to save your edits before changing selection?")
    @Key("alerts.apply.edits.msg")
    String saveEditsAlert_Message();

    @DefaultMessage("Deprecate")
    @Key("deprecate")
    String deprecate();

    @DefaultMessage("Import and replace forms?")
    @Key("forms.import.confirmation.title")
    String confirmImportFormsIntoProject_title();

    @DefaultMessage("Do you really want to import and replace all of the forms in this project?  This cannot be undone.")
    @Key("forms.import.confirmation.message")
    String confirmImportFormsIntoProject_message();
}
