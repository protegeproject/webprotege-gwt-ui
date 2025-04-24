package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.access.Capability;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CapabilityPresenterSelector {

    private final List<CapabilityPresenterFactory> capabilityPresenterFactories = new ArrayList<>();

    private Optional<CapabilityPresenter> selectedPresenter = Optional.empty();

    private String selectedTypeId = "";

    private final CapabilityPresenterSelectorView view;

    @Inject
    public CapabilityPresenterSelector(BasicCapabilityPresenterFactory f0,
                                       CapabilityPresenterSelectorView view) {
        this.view = view;
        capabilityPresenterFactories.add(f0);
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
        capabilityPresenterFactories.forEach(pf -> {
            view.addTypeId(pf.getTypeId(), pf.getLabel());
        });
        this.selectedTypeId = capabilityPresenterFactories.get(0).getTypeId();
        updateSelectedPresenter();
        view.setSelectedTypeIdChangedHandler(this::handleSelectedTypeIdChanged);
    }

    private void handleSelectedTypeIdChanged(String nextSelectedTypeId) {
        this.selectedTypeId = nextSelectedTypeId;
        updateSelectedPresenter();
    }

//    @Nonnull
//    public String getTypeId() {
//        return selectedTypeId;
//    }
//
//    public void setSelectedTypeId(@Nonnull String selectedTypeId) {
//        this.selectedTypeId = selectedTypeId;
//        updateSelectedPresenter();
//    }

    private void updateSelectedPresenter() {
        if(selectedPresenter.isPresent()) {
            if(selectedPresenter.get().getTypeId().equals(selectedTypeId)) {
                return;
            }
        }
        selectedPresenter = Optional.empty();
        for (CapabilityPresenterFactory pf : capabilityPresenterFactories) {
            if (pf.getTypeId().equals(selectedTypeId)) {
                CapabilityPresenter presenter = pf.createPresenter();
                selectedPresenter = Optional.of(presenter);
                presenter.start(view.getPresenterContainer());
                break;
            }
        }
    }

    ////////////

    public void setValue(Capability capability) {
        capabilityPresenterFactories.stream()
                .filter(pf -> pf.isPresenterFor(capability))
                .findFirst()
                .ifPresent(pf -> {
                    selectedTypeId = pf.getTypeId();
                    updateSelectedPresenter();
                    selectedPresenter.ifPresent(p -> p.setCapability(capability));
                });
    }

    public Optional<Capability> getValue() {
        return selectedPresenter.flatMap(CapabilityPresenter::getCapability);
    }

    public void clearValue() {
        selectedTypeId = "";
        selectedPresenter = Optional.empty();
    }


    public boolean isDirty() {
        return false;
    }

    public boolean isWellFormed() {
        return getValue().isPresent();
    }
}
