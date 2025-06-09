package edu.stanford.bmir.protege.web.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import edu.stanford.bmir.protege.web.client.app.ApplicationPresenter;
import edu.stanford.bmir.protege.web.client.app.ApplicationView;
import edu.stanford.bmir.protege.web.client.app.WebProtegeInitializer;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.client.place.WebProtegeActivityManager;
import edu.stanford.protege.widgetmap.resources.WidgetMapClientBundle;

import java.util.logging.Logger;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;


/**
 * @author Matthew Horridge
 */
public class WebProtege implements EntryPoint {

    private static final Logger logger = Logger.getLogger(WebProtege.class.getName());

    public void onModuleLoad() {
        handlePostLoginHashIfNecessary();

        WebProtegeInitializer initializer = WebProtegeClientInjector.get().getWebProtegeInitializer();
        initializer.init(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("There was a problem initializing WebProtege", caught);
            }

            @Override
            public void onSuccess(Void result) {
                GWT.log("Application initialization complete.  Starting UI Initialization.");
                handleUIInitialization();
            }
        });
    }

    private static void handlePostLoginHashIfNecessary() {
        Storage localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage != null) {
            String savedHash = localStorage.getItem("post-login-hash");
            if (savedHash != null && !savedHash.isEmpty()) {
                logger.info("Applying post-login hash and redirecting: " + savedHash);
                localStorage.removeItem("post-login-hash");
                // Reapply the hash to the URL
                Window.Location.assign(Window.Location.getPath() + "#" + savedHash.replace("#", ""));
            }
        }
    }

    private void handleUIInitialization() {
        buildUI();
    }

    private void buildUI() {

        BUNDLE.laf().ensureInjected();
        BUNDLE.toolbar().ensureInjected();
        BUNDLE.login().ensureInjected();
        BUNDLE.style().ensureInjected();
        BUNDLE.buttons().ensureInjected();
        BUNDLE.discussion().ensureInjected();
        BUNDLE.dragAndDrop().ensureInjected();
        BUNDLE.menu().ensureInjected();
        BUNDLE.settings().ensureInjected();
        BUNDLE.valueList().ensureInjected();
        BUNDLE.tags().ensureInjected();
        BUNDLE.dateTimePicker().ensureInjected();
        BUNDLE.projectList().ensureInjected();
        BUNDLE.entityNode().ensureInjected();
        BUNDLE.modal().ensureInjected();
        BUNDLE.glyphs().ensureInjected();
        BUNDLE.primitiveData().ensureInjected();
        WidgetMapClientBundle.BUNDLE.style().ensureInjected();

        WebProtegeClientInjector injector = WebProtegeClientInjector.get();
        ApplicationPresenter applicationPresenter = injector.getApplicationPresenter();
        applicationPresenter.start();

        ApplicationView applicationView = applicationPresenter.getApplicationView();

        RootLayoutPanel.get().add(applicationView);

        WebProtegeActivityManager activityManager =  injector.getActivityManager();
        activityManager.setDisplay(applicationView);

        PlaceHistoryHandler placeHistoryHandler = injector.getPlaceHistoryHandler();
        placeHistoryHandler.handleCurrentHistory();


    }

}
