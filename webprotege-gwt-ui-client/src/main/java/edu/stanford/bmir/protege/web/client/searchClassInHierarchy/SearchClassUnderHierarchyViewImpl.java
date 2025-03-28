package edu.stanford.bmir.protege.web.client.searchClassInHierarchy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;


public class SearchClassUnderHierarchyViewImpl extends Composite implements SearchClassUnderHierarchyView {

    private static final SearchClassUnderHierarchyViewImpl.ClassUnderHierarchyViewImplUiBinder ourUiBinder = GWT.create(SearchClassUnderHierarchyViewImpl.ClassUnderHierarchyViewImplUiBinder.class);

    interface ClassUnderHierarchyViewImplUiBinder extends UiBinder<HTMLPanel, SearchClassUnderHierarchyViewImpl> {

    }

    private SearchSelectionChangedHandler handler = (entity) -> {
    };

    private final Messages messages;

    @UiField
    Label searchLabel;

    @UiField(provided = true)
    PrimitiveDataEditor editorField;


    @Inject
    SearchClassUnderHierarchyViewImpl(Messages messages,
                                      @Nonnull PrimitiveDataEditorImpl editorField) {
        this.messages = messages;
        this.editorField = checkNotNull(editorField);
        initWidget(ourUiBinder.createAndBindUi(this));
        this.searchLabel.setText(messages.hierarchy_searchInHierarchy());
        this.editorField.addValueChangeHandler(value -> {
            Optional<OWLPrimitiveData> valueChanged = value.getValue();
            if (valueChanged.isPresent()) {
                OWLPrimitiveData primitiveData = valueChanged.get();
                Optional<OWLEntity> optionalOWLEntity = primitiveData.asEntity();
                handler.handleSelectionChanged(optionalOWLEntity);
            }
        });
        this.editorField.setMode(PrimitiveDataEditorView.Mode.SINGLE_LINE);
        this.editorField.setEntityLinkMode(EntityLinkMode.DO_NOT_SHOW_LINKS_FOR_ENTITIES);
    }

    @Nonnull
    @Override
    public Optional<OWLEntity> getSelected() {
        return editorField.getSelectedSuggestion()
                .map(entitySuggestion -> entitySuggestion.getEntity().getEntity());
    }

    @Override
    public void setCriteria(CompositeRootCriteria criteria) {
        this.editorField.setCriteria(criteria);
    }


    @Override
    public void setSelectionChangedHandler(SearchSelectionChangedHandler handler) {
        this.handler = handler;
    }

    @Override
    public void clearValue() {
        this.editorField.clearValue();
    }
}
