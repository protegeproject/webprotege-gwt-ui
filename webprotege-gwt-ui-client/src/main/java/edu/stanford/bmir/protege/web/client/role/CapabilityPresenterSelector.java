package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.access.Capability;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

public class CapabilityPresenterSelector {

    private final static Logger logger = Logger.getLogger(CapabilityPresenterSelector.class.getName());


    private final List<CapabilityPresenterFactory> capabilityPresenterFactories = new ArrayList<>();

    private String selectedTypeId = "";

    private final CapabilityPresenterSelectorView view;

    private final Map<String, CapabilityPresenter> presenters = new HashMap<>();


    @Inject
    public CapabilityPresenterSelector(BasicCapabilityPresenterFactory f0,
                                       FormRegionCapabilityPresenterFactory f1,
                                       LinearizationRowsCapabilityPresenterFactory f2,
                                       ContextAwareCapabilityPresenterFactory f3,
                                       CapabilityPresenterSelectorView view) {
        this.view = view;
        capabilityPresenterFactories.add(f0);
        capabilityPresenterFactories.add(f1);
        capabilityPresenterFactories.add(f2);
        capabilityPresenterFactories.add(f3);
        capabilityPresenterFactories.forEach(pf -> view.addTypeId(pf.getTypeId(), pf.getLabel()));
        view.setSelectedTypeIdChangedHandler(this::handleSelectedTypeIdChanged);
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    private void handleSelectedTypeIdChanged(String nextSelectedTypeId) {
        logger.info("Selected type id changed: " + nextSelectedTypeId);
        this.selectedTypeId = nextSelectedTypeId;
        updateSelectedPresenter();
    }

    private void updateSelectedPresenter() {
        logger.info("Updating selected presenter");
        view.setSelectedTypeId(selectedTypeId);
        view.clearPresenter();
        if (selectedTypeId.isEmpty()) {
            return;
        }
        CapabilityPresenter presenter = getPresenter();
        presenter.start(view.getPresenterContainer());
    }

    private CapabilityPresenter getPresenter() {
        CapabilityPresenter presenter = presenters.get(selectedTypeId);
        if (presenter == null) {
            for (CapabilityPresenterFactory pf : capabilityPresenterFactories) {
                if (pf.getTypeId().equals(selectedTypeId)) {
                    presenter = pf.createPresenter();
                    presenter.start(view.getPresenterContainer());
                    presenters.put(selectedTypeId, presenter);
                    break;
                }
            }
        }
        return presenter;
    }

    public void setValue(Capability capability) {
        logger.info("Setting capability in presenter: " + capability);
        capabilityPresenterFactories.stream()
                .filter(pf -> pf.isPresenterFor(capability))
                .findFirst()
                .ifPresent(pf -> {
                    selectedTypeId = pf.getTypeId();
                    updateSelectedPresenter();
                    getPresenter().setCapability(capability);
                });
    }

    public Optional<Capability> getValue() {
        return getPresenter().getCapability();
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
