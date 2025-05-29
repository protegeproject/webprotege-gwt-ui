package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.ObjectListPresenter;
import edu.stanford.bmir.protege.web.client.form.ObjectListView;
import edu.stanford.bmir.protege.web.client.form.ObjectListViewHolder;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;

public class HierarchyDescriptorRulesPresenter {

    private final ObjectListPresenter<HierarchyDescriptorRule> objectListPresenter;

    @Inject
    public HierarchyDescriptorRulesPresenter(ObjectListView view,
                                             Provider<ObjectPresenter<HierarchyDescriptorRule>> objectListPresenterProvider,
                                             Provider<ObjectListViewHolder> objectViewHolderProvider) {
        this.objectListPresenter = new ObjectListPresenter<>(view, objectListPresenterProvider, objectViewHolderProvider, HierarchyDescriptorRule::empty);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        objectListPresenter.start(container, new SimpleEventBus());
    }

    public void setRules(List<HierarchyDescriptorRule> rules) {
        objectListPresenter.setValues(rules);
    }

    public List<HierarchyDescriptorRule> getRules() {
        return objectListPresenter.getValues();
    }
}
