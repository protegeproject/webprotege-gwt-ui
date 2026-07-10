package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.lang.LanguageMapChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Regression tests for #281: clicking the Forms manager's page-level OK button
 * bulk-resends every row's cached FormDescriptor (ObjectListPresenter.getValues()).
 * That cache must reflect the last edit, or OK silently reverts labels that were
 * already saved individually via updateForm on blur.
 */
@RunWith(MockitoJUnitRunner.class)
public class FormsManagerObjectPresenter_TestCase {

    private FormsManagerObjectPresenter presenter;

    @Mock
    private FormsManagerObjectView view;

    @Mock
    private LanguageMapCurrentLocaleMapper localeMapper;

    @Mock
    private PlaceController placeController;

    @Mock
    private ProjectId projectId;

    @Mock
    private FormsMessages formsMessages;

    @Mock
    private FormsManagerService formsManagerService;

    @Mock
    private AcceptsOneWidget container;

    private final FormId formId = FormId.generate();

    private LanguageMapChangedHandler capturedHandler;

    @Before
    public void setUp() {
        presenter = new FormsManagerObjectPresenter(view,
                                                    localeMapper,
                                                    placeController,
                                                    projectId,
                                                    formsMessages,
                                                    formsManagerService);
        presenter.start(container);

        ArgumentCaptor<LanguageMapChangedHandler> captor =
                ArgumentCaptor.forClass(LanguageMapChangedHandler.class);
        verify(view).setLanguageMapChangedHandler(captor.capture());
        capturedHandler = captor.getValue();

        presenter.setValue(FormDescriptor.empty(formId));
    }

    @Test
    public void shouldReflectTheEditedLabelInGetValueAfterALanguageMapChange() {
        LanguageMap newLabel = LanguageMap.get(Map.of("en", "TestForm"));
        when(view.getLanguageMap()).thenReturn(newLabel);

        capturedHandler.handleLanguageMapChanged();

        assertThat(presenter.getValue().isPresent(), is(true));
        assertThat(presenter.getValue().get().getLabel(), is(newLabel));
        verify(formsManagerService).updateForm(argThat(fd -> fd.getLabel().equals(newLabel)),
                                               any(),
                                               any());
    }
}
