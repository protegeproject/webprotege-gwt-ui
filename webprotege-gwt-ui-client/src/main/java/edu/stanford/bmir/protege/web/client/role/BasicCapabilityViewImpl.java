package edu.stanford.bmir.protege.web.client.role;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BasicCapabilityViewImpl extends Composite implements BasicCapabilityView {

    interface BasicCapabilityViewImplUiBinder extends UiBinder<HTMLPanel, BasicCapabilityViewImpl> {

    }

    private static BasicCapabilityViewImplUiBinder ourUiBinder = GWT.create(BasicCapabilityViewImplUiBinder.class);

    @UiField
    ListBox listBox;

    private final List<String> availableIds = new ArrayList<>();

    @Inject
    public BasicCapabilityViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setId(String id) {
        int itemCount = listBox.getItemCount();
        for(int i = 0; i < itemCount; i++) {
            if(listBox.getValue(i).equals(id)) {
                listBox.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public String getId() {
        return Optional.ofNullable(listBox.getSelectedValue()).orElse("");
    }

    @Override
    public void setAvailableIds(List<String> ids) {
        String current = listBox.getSelectedValue();
        availableIds.clear();
        availableIds.addAll(ids);
        for(String id : ids) {
            listBox.addItem(id);
        }
        setId(current);
    }
}