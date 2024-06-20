package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeHtmlRenderer;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameSettingsManager;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class EntitySearchResultViewImpl extends Composite implements EntitySearchResultView {

    interface SearchResultViewUiBinder extends UiBinder<HTMLPanel, EntitySearchResultViewImpl> {

    }

    private static SearchResultViewUiBinder ourUiBinder = GWT.create(SearchResultViewUiBinder.class);


    @UiField
    HTMLPanel matchesContainer;

    @UiField
    HTML entityRenderingField;
    @UiField
    Label oboIdField;

    @Nonnull
    private final EntityNodeHtmlRenderer renderer;

    @Nonnull
    private final DisplayNameSettingsManager displayNameSettingsManager;


    @UiField
    Button showInHierarchyButton;

    SearchPopUpHierarchyHandler searchPopUpHierarchyHandler = (uiObject) -> {};

    @Inject
    public EntitySearchResultViewImpl(@Nonnull EntityNodeHtmlRenderer renderer,
                                      @Nonnull DisplayNameSettingsManager displayNameSettingsManager) {
        this.renderer = checkNotNull(renderer);
        this.displayNameSettingsManager = checkNotNull(displayNameSettingsManager);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setEntity(@Nonnull EntityNode entityNode) {
        renderer.setRenderTags(false);
        DisplayNameSettings displayNameSettings = displayNameSettingsManager.getLocalDisplayNameSettings();
        renderer.setDisplayLanguage(displayNameSettings);
        String htmlRendering = renderer.getHtmlRendering(entityNode);
        String primaryDisplayName = renderer.getPrimaryDisplayName(entityNode);
        entityRenderingField.setHTML(htmlRendering);
        entityRenderingField.setTitle(primaryDisplayName);
    }

    @Override
    public void setResultMatchViews(@Nonnull ImmutableList<SearchResultMatchView> views) {
        matchesContainer.clear();
        views.forEach(view -> matchesContainer.add(view));
    }

    @Override
    public void clearOboId() {
        oboIdField.setText("");
        oboIdField.setVisible(false);
    }

    @Override
    public void setOboId(@Nonnull String oboId) {
        oboIdField.setText(oboId);
        oboIdField.setVisible(true);
    }

    @UiHandler("showInHierarchyButton")
    public void showHierarchyButtonClicked(ClickEvent event){
        this.searchPopUpHierarchyHandler.showPupUpHierarchy(showInHierarchyButton);
    }

    @Override
    public void setPopUpHierarchyHandler(SearchPopUpHierarchyHandler handler) {
        this.searchPopUpHierarchyHandler = handler;
    }
}