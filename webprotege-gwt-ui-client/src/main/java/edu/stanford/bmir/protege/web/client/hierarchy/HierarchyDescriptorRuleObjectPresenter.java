package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Consumer;

public class HierarchyDescriptorRuleObjectPresenter implements ObjectPresenter<HierarchyDescriptorRule> {

    private final HierarchyDescriptorRulePresenter rulePresenter;

    @Inject
    public HierarchyDescriptorRuleObjectPresenter(HierarchyDescriptorRulePresenter rulePresenter) {
        this.rulePresenter = rulePresenter;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        rulePresenter.start(container);
    }

    @Override
    public void setValue(@Nonnull HierarchyDescriptorRule value) {
        rulePresenter.setHierarchyDescriptorRule(value);
    }

    @Nonnull
    @Override
    public Optional<HierarchyDescriptorRule> getValue() {
        return rulePresenter.getHierarchyDescriptorRule();
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return "Rule";
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {

    }
}
