package edu.stanford.bmir.protege.web.client.searchIcd;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import edu.stanford.bmir.protege.web.client.filter.FilterCheckBox;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.text.PlaceholderTextBox;
import edu.stanford.bmir.protege.web.client.progress.BusyViewImpl;
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


    private final static java.util.logging.Logger logger = Logger.getLogger("SearchIcdViewImpl");

    private static SearchViewIcdImplUiBinder ourUiBinder = GWT.create(SearchViewIcdImplUiBinder.class);

    @UiField
    protected PlaceholderTextBox searchStringField;

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

    @UiField
    FilterCheckBox filterSubtreeCheckbox;


    private SearchIcdResultChosenHandler searchResultChosenHandler = result -> {
    };

    private String selectedURI = "";

    public static String TOP_LEVEL_SUBTREE_FILTER =
            "http://id.who.int/icd/entity/448895267,"   // ICD Entity
                    + "http://id.who.int/icd/entity/1405434703,"  // ICF Entity
                    + "http://id.who.int/icd/entity/60347385,"    // ICHI Entity
                    + "http://id.who.int/icd/entity/1320036174";  // Extension Entities

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

    public void setSelection(String selection) {
        this.selectedURI = selection;
        this.selection.setText(selection);
    }

    public native void exportSetSelection() /*-{
        var that = this;
        $wnd.setSelection = $entry(function (code) {
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
            apiSecured: false,
            height: "300px",
            enableKeyboard: false
        };

        var callbacks = {
            selectedEntityFunction: function (selectedEntity) {
                // paste the code into the <input>
                $wnd.console.log("Selected iNo: " + selectedEntity.iNo + " selectedEntity: " + selectedEntity.title + " selectedId: " + selectedEntity.id + " selectedUri: " + selectedEntity.uri + " selectedEntity: " + selectedEntity);
                $wnd.setSelection(selectedEntity.uri);
            }
        };

        $wnd.ECT.Handler.configure(settings, callbacks);

        $wnd.ECT.Handler.bind("1");

    }-*/;

    public native void triggerSearch(String query) /*-{
        $wnd.ECT.Handler.search("1", query);
    }-*/;

    @Override
    public void setAcceptKeyHandler(@Nonnull AcceptKeyHandler acceptKeyHandler) {
        this.acceptKeyHandler = checkNotNull(acceptKeyHandler);
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(() -> searchStringField.setFocus(true));
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
        if (!this.selectedURI.isEmpty()) {
            searchResultChosenHandler.handleSearchResultChosen(selectedURI);
            acceptKeyHandler.handleAcceptKey();
        }
    }


    public String getSelectedURI() {
        return selectedURI;
    }


    @Override
    public void onLoad() {
        super.onLoad();
        exportSetSelection();
        initEct();
    }

    private native void setJsniSubtreeFilter(String icdSearchFilter) /*-{
        $wnd.ECT.Handler.overwriteConfiguration("1", {subtreesFilter: icdSearchFilter});
        $wnd.console.log("icdSearchFilter: " + icdSearchFilter);
    }-*/;

    public void setSubtreeFilter(String icdSearchFilter) {
        if (icdSearchFilter == null || icdSearchFilter.length() == 0) {
            icdSearchFilter = TOP_LEVEL_SUBTREE_FILTER;
        }
        setJsniSubtreeFilter(icdSearchFilter);
        bind("1");
    }

    public native void bind(String iNo) /*-{
        $wnd.ECT.Handler.bind(iNo);
    }-*/;
}