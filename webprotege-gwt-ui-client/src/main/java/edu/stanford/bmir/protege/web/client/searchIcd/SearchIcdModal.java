package edu.stanford.bmir.protege.web.client.searchIcd;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2018
 */
public class SearchIcdModal {

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final SearchIcdPresenter searchIcdPresenter;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final SelectionModel selectionModel;

    @Nonnull
    private String title;

    Logger logger = java.util.logging.Logger.getLogger("SearchIcdModal");

    @Inject
    public SearchIcdModal(@Nonnull ModalManager modalManager, @Nonnull SearchIcdPresenter searchIcdPresenter, @Nonnull Messages messages, @Nonnull SelectionModel selectionModel) {
        this.modalManager = modalManager;
        this.searchIcdPresenter = searchIcdPresenter;
        this.messages = messages;
        this.selectionModel = selectionModel;
        title = messages.searchIcd();
    }

    public void setEntityTypes(EntityType<?>... entityTypes) {
        searchIcdPresenter.setEntityTypes(entityTypes);
        if (entityTypes.length == 1) {
            title = messages.searchFor(entityTypes[0].getPrintName());
        }
    }

    public void setHierarchySelectedOptions(Optional<EntityNode> selectedOption) {

        searchIcdPresenter.setSubTreeFilter(selectedOption);
    }

    public void showModal() {
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setTitle(title);
        modalPresenter.setView(searchIcdPresenter.getView());
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setPrimaryButton(DialogButton.SELECT);
        modalPresenter.setButtonHandler(DialogButton.SELECT, closer -> {
            closer.closeModal();
            selectChosenEntity();
        });
        searchIcdPresenter.start();
        searchIcdPresenter.setSearchResultChosenHandler(result -> modalPresenter.accept());
        modalManager.showModal(modalPresenter);
    }

    private void selectChosenEntity() {
        searchIcdPresenter.getSelectedSearchResult()
                .ifPresent(sel -> {
                    logger.info("selectedEntity: " + sel);
                    GWT.log("selectedEntity: " + sel);
                    selectionModel.setSelection(sel);
                });
    }
}
