package edu.stanford.bmir.protege.web.client.selection;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.place.ItemSelection;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.*;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
@RunWith(MockitoJUnitRunner.class)
public class SelectionModel_TestCase {

    private SelectionModel selectionModel;

    @Mock
    private DispatchServiceManager dispatch;

    @Spy
    private EventBus eventBus = new SimpleEventBus();

    @Mock
    private PlaceController placeController;

    @Mock
    private SelectedEntityManager<OWLClass> selectedClassManager;

    @Mock
    private SelectedEntityManager<OWLObjectProperty> selectedObjectPropertyManager;

    @Mock
    private SelectedEntityManager<OWLDataProperty> selectedDataPropertyManager;

    @Mock
    private SelectedEntityManager<OWLAnnotationProperty> selectedAnnotationPropertyManager;

    @Mock
    private SelectedEntityManager<OWLDatatype> selectedDatatypeManager;

    @Mock
    private SelectedEntityManager<OWLNamedIndividual> selectedIndividualManager;

    @Mock
    private ProjectViewPlace projectPlace;

    private OWLClass cls = new OWLClassImpl(IRI.create("urn:entity:cls"));

    private ProjectId projectId = ProjectId.getNil();

    @Before
    public void setUp() {
        selectionModel = new SelectionModel(
                projectId,
                dispatch, eventBus,
                placeController,
                selectedClassManager,
                selectedObjectPropertyManager,
                selectedDataPropertyManager,
                selectedAnnotationPropertyManager,
                selectedDatatypeManager,
                selectedIndividualManager);
        setupSelectionForEntity(cls);
    }

    private void setupSelectionForEntity(OWLEntity entity) {
        ItemSelection itemSelection = ItemSelection.builder().addEntity(entity).build();
        when(projectPlace.getItemSelection()).thenReturn(itemSelection);
    }

    @Test
    public void shouldFireEntitySelectionChangedEventOnPlaceChange() {
        eventBus.fireEvent(new PlaceChangeEvent(projectPlace));
        verify(eventBus, times(1)).fireEvent(isA(EntitySelectionChangedEvent.class));
    }

    @Test
    public void shouldNoteFireEntitySelectionChangedEventOnPlaceChangeForSameSelection() {
        eventBus.fireEvent(new PlaceChangeEvent(projectPlace));
        eventBus.fireEvent(new PlaceChangeEvent(projectPlace));
        verify(eventBus, times(1)).fireEvent(isA(EntitySelectionChangedEvent.class));
    }

    @Test
    public void shouldRecordLastSelectedClass() {
        eventBus.fireEvent(new PlaceChangeEvent(projectPlace));
        verify(selectedClassManager, times(1)).setSelection(cls);
    }

    @Test
    public void shouldRecordLastSelectedNamedIndividual() {
        OWLNamedIndividualImpl prop = new OWLNamedIndividualImpl(IRI.create("urn:entity:ind"));
        setupSelectionForEntity(prop);
        eventBus.fireEvent(new PlaceChangeEvent(projectPlace));
        verify(selectedIndividualManager, times(1)).setSelection(prop);
    }
    
    @Test
    public void shouldRecordLastSelectedObjectProperty() {
        OWLObjectPropertyImpl prop = new OWLObjectPropertyImpl(IRI.create("urn:entity:prop"));
        setupSelectionForEntity(prop);
        eventBus.fireEvent(new PlaceChangeEvent(projectPlace));
        verify(selectedObjectPropertyManager, times(1)).setSelection(prop);
    }
    
    @Test
    public void shouldRecordLastSelectedDataProperty() {
        OWLDataPropertyImpl prop = new OWLDataPropertyImpl(IRI.create("urn:entity:prop"));
        setupSelectionForEntity(prop);
        eventBus.fireEvent(new PlaceChangeEvent(projectPlace));
        verify(selectedDataPropertyManager, times(1)).setSelection(prop);
    }
    
    @Test
    public void shouldRecordLastSelectedAnnotationProperty() {
        OWLAnnotationPropertyImpl prop = new OWLAnnotationPropertyImpl(IRI.create("urn:entity:prop"));
        setupSelectionForEntity(prop);
        eventBus.fireEvent(new PlaceChangeEvent(projectPlace));
        verify(selectedAnnotationPropertyManager, times(1)).setSelection(prop);
    }
}
