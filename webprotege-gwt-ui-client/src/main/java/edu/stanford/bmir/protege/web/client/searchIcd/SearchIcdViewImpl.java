package edu.stanford.bmir.protege.web.client.searchIcd;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;
import edu.stanford.bmir.protege.web.client.progress.BusyViewImpl;
import edu.stanford.bmir.protege.web.client.search.SearchResultChosenHandler;
import edu.stanford.bmir.protege.web.client.search.SearchStringChangedHandler;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class SearchIcdViewImpl extends Composite implements SearchIcdView {


    private final static java.util.logging.Logger logger = Logger.getLogger("DispatchServiceManager");

    private static SearchViewIcdImplUiBinder ourUiBinder = GWT.create(SearchViewIcdImplUiBinder.class);

    @UiField
    protected PlaceholderTextBox searchStringField;

    @Nonnull
    private IncrementSelectionHandler incrementSelectionHandler = () -> {
    };

    @Nonnull
    private DecrementSelectionHandler decrementSelectionHandler = () -> {
    };

    @Nonnull
    private SearchStringChangedHandler searchStringChangedHandler = () -> {
    };

    @Nonnull
    private AcceptKeyHandler acceptKeyHandler = () -> {
    };

    private String previousSearchString = "";


    @UiField
    BusyViewImpl busyView;
    @UiField
    HTMLPanel ectElement;

    @UiField
    TextArea selection;


    private SearchIcdResultChosenHandler searchResultChosenHandler;

    private String selectedURI = "";

    @Inject
    public SearchIcdViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        Element element = searchStringField.getElement();
        element.setPropertyString("autocomplete", "off");
        element.setPropertyString("autocorrect", "off");
        element.setPropertyString("autocapitalize", "off");
        element.setPropertyString("spellcheck", "off");
        element.setAttribute("data-ctw-ino", "1");
        ectElement.getElement().setAttribute("data-ctw-ino", "1");

        selection.getElement().setId("selectionTextArea");
    }

    public void setSelection(String selection){
        this.selectedURI = selection;
        this.selection.setText(selection);
    }

    public native void exportSetSelection() /*-{
        var that = this;
        $wnd.setSelection = $entry(function(code) {
            that.@SearchIcdViewImpl::setSelection(Ljava/lang/String;)(code);
        });
    }-*/;

    public native void logSomething() /*-{
         console.log("i'm here in initECT");
         $wnd.console.log("i'm here in initECT");

    }-*/;

    public native void initEct() /*-{
        $wnd.console.log("i'm here in initECT");
        $wnd.console.log("ECT2 este " + $wnd.ECT);

        var settings = {
            apiServerUrl: "https://icd11restapi-developer-test.azurewebsites.net",
            simplifiedMode: false,
            popupMode: false,
            icdLinearization: "foundation",
            autoBind: false,
            apiSecured: false
        };

        var callbacks ={
            selectedEntityFunction: function(selectedEntity)  {
            // paste the code into the <input>
                $wnd.console.log("Selected iNo: " + selectedEntity.iNo + " selectedEntity: " + selectedEntity.title +" selectedId: "+selectedEntity.id +" selectedUri: "+selectedEntity.uri + " selectedEntity: "+selectedEntity);
                $wnd.setSelection(selectedEntity.uri);
        }
    };

        $wnd.ECT.Handler.configure(settings,callbacks);

        $wnd.ECT.Handler.bind("1");

    }-*/;

    public native void triggerSearch(String query) /*-{
        $wnd.ECT.Handler.search("1", query);
    }-*/;

    @UiHandler("searchStringField")
    protected void handleSearchStringFileKeyUp(KeyUpEvent event) {
        int keyCode = event.getNativeEvent().getKeyCode();
        if (keyCode != KeyCodes.KEY_UP && keyCode != KeyCodes.KEY_DOWN && keyCode != KeyCodes.KEY_ENTER) {
            performSearchIfChanged();
        }
    }

    private void performSearchIfChanged() {
        String searchString = searchStringField.getText();
        if (!previousSearchString.equals(searchString)) {
            previousSearchString = searchString;
            searchStringChangedHandler.handleSearchStringChanged();
        }
    }

    @Override
    public void setIncrementSelectionHandler(@Nonnull IncrementSelectionHandler handler) {
        incrementSelectionHandler = checkNotNull(handler);
    }

    @Override
    public void setDecrementSelectionHandler(@Nonnull DecrementSelectionHandler handler) {
        decrementSelectionHandler = checkNotNull(handler);
    }

    @Override
    public void setAcceptKeyHandler(@Nonnull AcceptKeyHandler acceptKeyHandler) {
        this.acceptKeyHandler = checkNotNull(acceptKeyHandler);
    }

    @UiHandler("searchStringField")
    protected void handleSearchStringFieldKeyDown(KeyDownEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
            event.preventDefault();
            incrementSelectionHandler.handleIncrementSelection();
        } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
            event.preventDefault();
            decrementSelectionHandler.handleDecrementSelection();
        } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            event.preventDefault();
            acceptKeyHandler.handleAcceptKey();
        } else {
            performSearchIfChanged();
        }
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(() -> searchStringField.setFocus(true));
    }

    @Override
    public String getSearchString() {
        return searchStringField.getText();
    }

    @Override
    public void setSearchStringChangedHandler(SearchStringChangedHandler handler) {
        this.searchStringChangedHandler = handler;
    }

    @Override
    public void setBusy(boolean busy) {
        busyView.setVisible(busy);
    }

    interface SearchViewIcdImplUiBinder extends UiBinder<HTMLPanel, SearchIcdViewImpl> {

    }

    private void chooseSearchResult() {
        if(!this.selectedURI.isEmpty()) {
            searchResultChosenHandler.handleSearchResultChosen(selectedURI);
            acceptKeyHandler.handleAcceptKey();
        }
    }


    @Override
    public void onLoad() {
        super.onLoad();
        GWT.log("SUnt apelat");
        logger.info("Sunt mega apelat in on load");
        exportSetSelection();
        initEct();
        logger.info("sunt apelat searchview");
    }
}