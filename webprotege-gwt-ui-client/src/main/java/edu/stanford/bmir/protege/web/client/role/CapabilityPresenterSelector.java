package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.access.Capability;
import elemental.html.Console;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

public class CapabilityPresenterSelector {

    private static Logger logger = Logger.getLogger(CapabilityPresenterSelector.class.getName());



    private final List<CapabilityPresenterFactory> capabilityPresenterFactories = new ArrayList<>();

    private String selectedTypeId = "";

    private final CapabilityPresenterSelectorView view;

    private final Map<String, CapabilityPresenter> presenters = new HashMap<>();


    @Inject
    public CapabilityPresenterSelector(BasicCapabilityPresenterFactory f0,
                                       FormRegionCapabilityPresenterFactory f1,
                                       CapabilityPresenterSelectorView view) {
        this.view = view;
        capabilityPresenterFactories.add(f0);
        capabilityPresenterFactories.add(f1);
        capabilityPresenterFactories.forEach(pf -> {
            view.addTypeId(pf.getTypeId(), pf.getLabel());
        });
        selectedTypeId = f0.getTypeId();
        view.setSelectedTypeIdChangedHandler(this::handleSelectedTypeIdChanged);
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        updateSelectedPresenter();
    }

    private void handleSelectedTypeIdChanged(String nextSelectedTypeId) {
        this.selectedTypeId = nextSelectedTypeId;
        updateSelectedPresenter();
    }

    private void updateSelectedPresenter() {
        view.setSelectedTypeId(selectedTypeId);
        view.clearPresenter();
        if(selectedTypeId.isEmpty()) {
            return;
        }
        Optional<CapabilityPresenter> presenter = getPresenter();
        presenter.ifPresent(p -> p.start(view.getPresenterContainer()));
    }

    private Optional<CapabilityPresenter> getPresenter() {
        CapabilityPresenter presenter = presenters.get(selectedTypeId);
        if(presenter == null) {
            for (CapabilityPresenterFactory pf : capabilityPresenterFactories) {
                if (pf.getTypeId().equals(selectedTypeId)) {
                    presenter = pf.createPresenter();
                    presenter.start(view.getPresenterContainer());
                    presenters.put(selectedTypeId, presenter);
                    return Optional.of(presenter);
                }
            }
        }
        return Optional.ofNullable(presenter);
    }

    public void setValue(Capability capability) {
        capabilityPresenterFactories.stream()
                .filter(pf -> pf.isPresenterFor(capability))
                .findFirst()
                .ifPresent(pf -> {
                    selectedTypeId = pf.getTypeId();
                    updateSelectedPresenter();
                    getPresenter().ifPresent(p -> p.setCapability(capability));
                });
    }

    public Optional<Capability> getValue() {
        return getPresenter().flatMap(CapabilityPresenter::getCapability);
    }

    public void clearValue() {
        selectedTypeId = "";
        updateSelectedPresenter();
    }


    public boolean isDirty() {
        return false;
    }

    public boolean isWellFormed() {
        return getValue().isPresent();
    }
}
