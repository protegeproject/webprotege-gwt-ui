package edu.stanford.bmir.protege.web.client.postcoordination;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ConfigurableCheckbox;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationDefinition;
import edu.stanford.bmir.protege.web.shared.postcoordination.PostCoordinationTableAxisLabel;

public class PostCoordinationTableCell {
        private ConfigurableCheckbox configurableCheckbox;
        private LinearizationDefinition linearizationDefinition;
        private PostCoordinationTableAxisLabel axisLabel;

    public PostCoordinationTableCell(ConfigurableCheckbox configurableCheckbox, LinearizationDefinition linearizationDefinition, PostCoordinationTableAxisLabel axisLabel) {
        this.configurableCheckbox = configurableCheckbox;
        this.linearizationDefinition = linearizationDefinition;
        this.axisLabel = axisLabel;
    }

    public Widget asWidget(){
        return configurableCheckbox.asWidget();
    }
}
