package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.settings.ApplySettingsHandler;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Provider;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Regression tests for #281: the Forms manager's OK button used to silently do
 * nothing whenever the current place had no in-memory "next place" to return
 * to -- which is exactly what happens when the page was reached via a URL
 * round trip (browser back/forward, refresh, bookmark) rather than in-app
 * navigation, since FormsPlaceTokenizer can't reconstruct that reference from
 * a URL. OK should fall back to the project's default view in that case.
 */
@RunWith(MockitoJUnitRunner.class)
public class FormsManagerPresenter_TestCase {

    private FormsManagerPresenter presenter;

    @Mock
    private FormsManagerView formManagerView;

    @Mock
    private SettingsPresenter settingsPresenter;

    @Mock
    private FormsManagerService formsManagerService;

    @Mock
    private FormsMessages formsMessages;

    @Mock
    private PlaceController placeController;

    @Mock
    private Messages messages;

    @Mock
    private CopyFormsFromProjectModalPresenter copyFormsFromProjectModalPresenter;

    @Mock
    private FormsManagerObjectListPresenter listPresenter;

    @Mock
    private Provider<FormsDownloader> formDownloaderProvider;

    @Mock
    private LoadProjectRequestHandler loadProjectRequestHandler;

    @Mock
    private AcceptsOneWidget container;

    @Mock
    private EventBus eventBus;

    @Mock
    private Place backHerePlace;

    private final ProjectId projectId = ProjectId.getNil();

    private ApplySettingsHandler capturedApplyHandler;

    @Before
    public void setUp() {
        when(settingsPresenter.addSection(any())).thenReturn(mock(AcceptsOneWidget.class));

        presenter = new FormsManagerPresenter(formManagerView,
                                              settingsPresenter,
                                              formsManagerService,
                                              formsMessages,
                                              placeController,
                                              messages,
                                              copyFormsFromProjectModalPresenter,
                                              listPresenter,
                                              formDownloaderProvider,
                                              loadProjectRequestHandler);
        presenter.start(container, eventBus);

        ArgumentCaptor<ApplySettingsHandler> captor = ArgumentCaptor.forClass(ApplySettingsHandler.class);
        verify(settingsPresenter).setApplySettingsHandler(captor.capture());
        capturedApplyHandler = captor.getValue();

        when(listPresenter.getValues()).thenReturn(ImmutableList.of());
        // Simulate the setForms RPC completing immediately.
        doAnswer(invocation -> {
            Runnable completeHandler = invocation.getArgument(2);
            completeHandler.run();
            return null;
        }).when(formsManagerService).setForms(any(), any(), any());
    }

    @Test
    public void shouldGoToTheRecordedNextPlaceWhenOneIsKnown() {
        FormsPlace formsPlace = FormsPlace.get(projectId, Optional.of(backHerePlace));
        when(placeController.getWhere()).thenReturn(formsPlace);

        capturedApplyHandler.handleApplySettings();

        verify(placeController).goTo(backHerePlace);
        verify(loadProjectRequestHandler, never()).handleProjectLoadRequest(any());
    }

    @Test
    public void shouldFallBackToTheProjectDefaultViewWhenNoNextPlaceIsKnown() {
        FormsPlace formsPlace = FormsPlace.get(projectId, Optional.empty());
        when(placeController.getWhere()).thenReturn(formsPlace);

        capturedApplyHandler.handleApplySettings();

        verify(loadProjectRequestHandler).handleProjectLoadRequest(projectId);
        verify(placeController, never()).goTo(any());
    }
}
