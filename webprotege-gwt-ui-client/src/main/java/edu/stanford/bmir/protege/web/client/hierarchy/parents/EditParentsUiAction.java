package edu.stanford.bmir.protege.web.client.hierarchy.parents;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class EditParentsUiAction extends AbstractUiAction {

    @Nonnull
    private final EditParentsPresenter editParentsPresenter;

    @Nonnull
    private final SelectionModel selectionModel;

    @Inject
    protected EditParentsUiAction(@Nonnull EditParentsPresenter editParentsPresenter, @Nonnull Messages messages, @Nonnull SelectionModel selectionModel) {
        super(messages.hierarchy_editParents());
        this.editParentsPresenter = checkNotNull(editParentsPresenter);
        this.selectionModel = checkNotNull(selectionModel);
    }


    @Override
    public void execute() {
        selectionModel.getSelection().ifPresent(this::showDialog);
    }

    private void showDialog(OWLEntity entity) {
        editParentsPresenter.setHierarchyDescriptor(ClassHierarchyDescriptor.get());
        editParentsPresenter.start(entity);
    }
}
