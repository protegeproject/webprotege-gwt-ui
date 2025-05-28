package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.primitive.NullFreshEntitySuggestStrategy;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataListEditor;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HierarchyDescriptorViewImpl extends Composite implements HierarchyDescriptorView {

    interface HierarchyDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, HierarchyDescriptorViewImpl> {

    }

    private static HierarchyDescriptorViewImplUiBinder ourUiBinder = GWT.create(HierarchyDescriptorViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataListEditor entityList;

    @UiField
    ListBox entityTypeList;

    @Inject
    public HierarchyDescriptorViewImpl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        entityList = new PrimitiveDataListEditor(() -> {
            PrimitiveDataEditor editor = primitiveDataEditorProvider.get();
            editor.setAllowedTypes(getAllowedTypes());
            editor.setFreshEntitiesSuggestStrategy(new NullFreshEntitySuggestStrategy());
            return editor;
        });
        entityList.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        entityList.setEnabled(true);

        initWidget(ourUiBinder.createAndBindUi(this));
        entityTypeList.addItem("Class");
        entityTypeList.addItem("Object property");
        entityTypeList.addItem("Data property");
        entityTypeList.addItem("Annotation property");
        entityTypeList.addChangeHandler(event -> {
            entityList.setAllowedTypes(PrimitiveType.get(getEntityType()));
        });
    }

    private Set<PrimitiveType> getAllowedTypes() {
        return Collections.singleton(PrimitiveType.get(getEntityType()));
    }

    @Override
    public void setEntityType(EntityType<?> entityType) {
        entityList.setAllowedTypes(PrimitiveType.get(entityType));
        entityTypeList.setSelectedIndex(0);
        for(int i = 0; i<entityTypeList.getItemCount(); i++){
            if(entityTypeList.getItemText(i).equals(entityType.getPrintName())){
                entityTypeList.setSelectedIndex(i);
            }
        }
    }

    @Override
    public EntityType<?> getEntityType() {
        String selValue = entityTypeList.getSelectedValue();
        for(EntityType<?> entityType : EntityType.values()) {
            if(entityType.getPrintName().equals(selValue)){
                return entityType;
            }
        }
        return EntityType.CLASS;
    }

    @Override
    public void setRoots(List<OWLEntityData> rootEntities) {
        entityList.setValue(new ArrayList<>(rootEntities));
        entityList.setAllowedTypes(PrimitiveType.get(getEntityType()));
    }

    @Override
    public List<OWLEntityData> getRoots() {
        return entityList.getValue().map(v -> v.stream().map(p -> (OWLEntityData) p).collect(Collectors.toList())).orElse(new ArrayList<>());
    }
}