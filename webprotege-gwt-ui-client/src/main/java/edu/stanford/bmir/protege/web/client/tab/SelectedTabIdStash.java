package edu.stanford.bmir.protege.web.client.tab;

import edu.stanford.bmir.protege.web.client.portlet.HasNodeProperties;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class SelectedTabIdStash {

    private static final String KEY = "forms-selected-form";

    @Nonnull
    private final HasNodeProperties nodeProperties;

    public SelectedTabIdStash(@Nonnull HasNodeProperties nodeProperties) {
        this.nodeProperties = checkNotNull(nodeProperties);
    }

    public void stashSelectedForm(@Nonnull FormId formId) {
        nodeProperties.setNodeProperty(KEY, formId.getId());
    }

    @Nonnull
    public Optional<FormId> getSelectedForm() {
        return Optional.ofNullable(nodeProperties.getNodeProperty(KEY, null))
                .map(FormId::get);

    }
}
