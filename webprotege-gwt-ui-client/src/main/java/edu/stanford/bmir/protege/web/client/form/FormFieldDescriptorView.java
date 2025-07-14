package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.field.FieldRun;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDeprecationStrategy;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface FormFieldDescriptorView extends IsWidget, HasRequestFocus {

    void setHelp(@Nonnull LanguageMap help);

    @Nonnull
    LanguageMap getHelp();

    void setLabel(@Nonnull LanguageMap label);

    @Nonnull
    LanguageMap getLabel();

    void setFieldRun(@Nonnull FieldRun fieldRun);

    @Nonnull
    FieldRun getFieldRun();

    void setOptionality(@Nonnull Optionality optionality);

    @Nonnull
    Optionality getOptionality();

    @Nonnull
    AcceptsOneWidget getOwlBindingViewContainer();

    void setRepeatability(@Nonnull Repeatability repeatability);

    @Nonnull
    Repeatability getRepeatability();

    void setPageSize(int pageSize);

    int getPageSize();

    void setReadOnly(boolean readOnly);

    boolean isReadOnly();

    void setLabelChangedHandler(@Nonnull Consumer<LanguageMap> runnable);

    @Nonnull
    AcceptsOneWidget getFieldDescriptorViewContainer();

    @Nonnull
    ExpansionState getInitialExpansionState();

    void setInitialExpansionState(@Nonnull ExpansionState expansionState);

    void setDeprecationStrategyVisible(boolean visible);

    void setDeprecationStrategy(@Nonnull FormFieldDeprecationStrategy deprecationStrategy);

    @Nonnull
    FormFieldDeprecationStrategy getDeprecationStrategy();

    void setViewAccessRoles(List<RoleCriteriaBinding> restrictions);

    List<RoleCriteriaBinding> getViewAccessRoles();

    void setEditAccessRoles(List<RoleCriteriaBinding> restrictions);

    List<RoleCriteriaBinding> getEditAccessRoles();
}
