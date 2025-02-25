package edu.stanford.bmir.protege.web.client.card;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.usage.UsageView;
import edu.stanford.bmir.protege.web.client.usage.UsageViewImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageAction;
import edu.stanford.bmir.protege.web.shared.usage.UsageFilter;
import edu.stanford.bmir.protege.web.shared.usage.UsageReference;
import edu.stanford.webprotege.shared.annotations.Card;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.Collection;

@Card(id = "class.stats")
public class UsageCardPresenter implements EntityCardPresenter {

    private final ProjectId projectId;

    private final DispatchServiceManager dispatch;

    private final UsageView usageView;

    @Inject
    public UsageCardPresenter(ProjectId projectId, DispatchServiceManager dispatch) {
        this.projectId = projectId;
        this.dispatch = dispatch;
        this.usageView = new UsageViewImpl();
    }

    @Override
    public void start(EntityCardUi ui, WebProtegeEventBus eventBus) {
        ui.setWidget(usageView);
    }


    @Override
    public void requestFocus() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setEntity(OWLEntity entity) {
        final GetUsageAction action = GetUsageAction.create(entity, projectId, new UsageFilter());
        dispatch.execute(action, result -> {
            final Collection<UsageReference> references = result.getUsageReferences();
            usageView.setData(entity, references);
        });
    }

    @Override
    public void clearEntity() {
        usageView.clearData();
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {

    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return () -> {};
    }
}
