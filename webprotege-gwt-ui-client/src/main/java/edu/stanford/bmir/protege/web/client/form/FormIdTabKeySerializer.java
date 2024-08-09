package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.tab.TabKeySerializer;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;

public class FormIdTabKeySerializer implements TabKeySerializer<FormId> {

    @Nonnull
    @Override
    public String serialize(@Nonnull FormId key) {
        return key.getId();
    }

    @Nonnull
    @Override
    public FormId deserialize(@Nonnull String key) {
        return FormId.get(key);
    }
}
