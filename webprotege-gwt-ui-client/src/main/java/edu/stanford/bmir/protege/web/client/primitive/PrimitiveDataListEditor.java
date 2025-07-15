package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.inject.Provider;
import java.util.Arrays;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/12/2012
 */
public class PrimitiveDataListEditor extends ValueListFlexEditorImpl<OWLPrimitiveData> implements HasEnabled  {

    public PrimitiveDataListEditor(Provider<PrimitiveDataEditor> primitiveDataEditorProvider,
                                   final PrimitiveType ... allowedTypes) {
        super(() -> {
            PrimitiveDataEditor editor = primitiveDataEditorProvider.get();
            editor.setAllowedTypes(Arrays.asList(allowedTypes));
            editor.setFreshEntitiesSuggestStrategy(new SimpleFreshEntitySuggestStrategy());
            return editor;
        });
    }

    public PrimitiveDataListEditor(ValueEditorFactory<OWLPrimitiveData> valueEditorFactory) {
        super(valueEditorFactory);
    }

    public void setAllowedTypes(PrimitiveType... allowedTypes) {
        forEachEditor(e -> ((PrimitiveDataEditor) e).setAllowedTypes(Arrays.asList(allowedTypes)));
    }
}
