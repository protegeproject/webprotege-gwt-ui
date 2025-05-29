package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDetails;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class PerspectiveChooserViewImpl extends Composite implements PerspectiveChooserView {

    private Optional<PerspectiveId> selectedPerspective = Optional.empty();

    interface PerspectiveChooserViewImplUiBinder extends UiBinder<HTMLPanel, PerspectiveChooserViewImpl> {

    }

    private static PerspectiveChooserViewImplUiBinder ourUiBinder = GWT.create(PerspectiveChooserViewImplUiBinder.class);

    @UiField
    ListBox listBox;

    @Inject
    public PerspectiveChooserViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setAvailablePerspectives(List<PerspectiveDetails> perspectives) {
        listBox.clear();
        listBox.addItem("Any", "");
        perspectives.forEach(p -> {
            LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
            String label = p.getLabel().get(localeInfo.getLocaleName());
            if(label.isEmpty()) {
                label = p.getPerspectiveId().getId();
            }
            listBox.addItem(label, p.getPerspectiveId().getId());
        });
        selectedPerspective.ifPresent(this::setSelectedPerspective);
    }

    @Override
    public void setSelectedPerspective(PerspectiveId perspectiveId) {
        this.selectedPerspective = Optional.of(perspectiveId);
        for(int i=0; i < listBox.getItemCount(); i++) {
            if(listBox.getValue(i).equals(perspectiveId.getId())) {
                listBox.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public Optional<PerspectiveId> getSelectedPerspective() {
        String sel = listBox.getSelectedValue();
        if(sel.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(PerspectiveId.get(sel));
    }

    @Override
    public void clearSelectedPerspective() {
        selectedPerspective = Optional.empty();
        listBox.setSelectedIndex(0);
    }
}