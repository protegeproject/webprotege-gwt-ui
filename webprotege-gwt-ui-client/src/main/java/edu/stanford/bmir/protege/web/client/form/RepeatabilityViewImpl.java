package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-15
 */
public class RepeatabilityViewImpl extends Composite implements RepeatabilityView {

    interface RepeatabilityViewImplUiBinder extends UiBinder<HTMLPanel, RepeatabilityViewImpl> {

    }

    private static RepeatabilityViewImplUiBinder ourUiBinder = GWT.create(RepeatabilityViewImplUiBinder.class);

    @UiField(provided = true)
    static Counter counter = new Counter();

    @UiField
    RadioButton nonRepeatableRadio;

    @UiField
    RadioButton repeatableVerticallyRadio;

    @UiField
    RadioButton repeatableHorizontallyRadio;

    @UiField
    TextBox pageSize;

    private int lastPageSize = 10;

    @Inject
    public RepeatabilityViewImpl() {
        counter.increment();
        initWidget(ourUiBinder.createAndBindUi(this));
        nonRepeatableRadio.addValueChangeHandler(event -> updateState());
        repeatableHorizontallyRadio.addValueChangeHandler(event -> updateState());
        repeatableVerticallyRadio.addValueChangeHandler(event -> updateState());
        pageSize.setValue(Long.toString(lastPageSize));
    }

    @Override
    public void setRepeatability(@Nonnull Repeatability repeatability) {
        if(repeatability == Repeatability.REPEATABLE_VERTICALLY) {
            repeatableVerticallyRadio.setValue(true);
        }
        else if(repeatability == Repeatability.REPEATABLE_HORIZONTALLY) {
            repeatableHorizontallyRadio.setValue(true);
        }
        else {
            nonRepeatableRadio.setValue(true);
        }
        updateState();
    }

    @Nonnull
    @Override
    public Repeatability getRepeatability() {
        if(repeatableVerticallyRadio.getValue()) {
            return Repeatability.REPEATABLE_VERTICALLY;
        }
        else if(repeatableHorizontallyRadio.getValue()) {
            return Repeatability.REPEATABLE_HORIZONTALLY;
        }
        else {
            return Repeatability.NON_REPEATABLE;
        }
    }

    @Override
    public int getPageSize() {
        try {
            int ps = Integer.parseInt(pageSize.getText().trim());
            if(ps <= 0) {
                return lastPageSize;
            }
            return ps;
        } catch (NumberFormatException e) {
            return lastPageSize;
        }
    }

    @Override
    public void setPageSize(int pageSize) {
        lastPageSize = pageSize;
        this.pageSize.setText(Long.toString(pageSize));
    }

    private void updateState() {
        pageSize.setEnabled(repeatableVerticallyRadio.getValue() || repeatableHorizontallyRadio.getValue());
    }
}
