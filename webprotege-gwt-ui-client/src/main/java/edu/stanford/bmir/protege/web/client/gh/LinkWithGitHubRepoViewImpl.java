package edu.stanford.bmir.protege.web.client.gh;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import javax.inject.Inject;
import java.util.logging.Logger;

public class LinkWithGitHubRepoViewImpl extends Composite implements LinkWithGitHubRepoView {


    private static final Logger logger = Logger.getLogger(LinkWithGitHubRepoViewImpl.class.getName());

    public static final int REPO_COORDS_CHANGED_DELAY_MILLIS = 1500;

    private RepoCoordinatesChangedHandler coordsChangedHandler = (orgName, repoName) -> {
        logger.warning("No RepoCoordinatesChangedHandler is set");
    };

    private final Timer repoCoordsChangedTimer = new Timer() {
        @Override
        public void run() {
            coordsChangedHandler.handleRepoCoordinatesChanged(getOwner(), getRepo());
        }
    };

    private InstallGitHubAppHandler installGitHubAppHandler = () -> {
        logger.warning("No InstallGitHubAppHandler is set");
    };

    interface LinkWithGitHubRepoViewImplUiBinder extends UiBinder<HTMLPanel, LinkWithGitHubRepoViewImpl> {

    }

    private static LinkWithGitHubRepoViewImplUiBinder ourUiBinder = GWT.create(LinkWithGitHubRepoViewImplUiBinder.class);

    @UiField
    TextBox ownerNameField;

    @UiField
    TextBox repoNameField;

    @UiField
    HTMLPanel statusMessageContainer;

    @UiField
    Label messageLabel;

    @UiField
    HTMLPanel installButtonContainer;

    @Inject
    public LinkWithGitHubRepoViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        statusMessageContainer.setVisible(false);
    }

    @UiHandler("ownerNameField" )
    public void handleOwnerNameChange(ValueChangeEvent<String> event) {
        fireRepoCoordsChanged();
    }

    private void fireRepoCoordsChanged() {
        coordsChangedHandler.handleRepoCoordinatesChanged(getOwner(), getRepo());
    }

    @UiHandler("repoNameField" )
    public void handleRepoNameChange(ValueChangeEvent<String> event) {
        fireRepoCoordsChanged();
    }

    @UiHandler("installAppButton" )
    public void installAppButtonClick(ClickEvent event) {
        installGitHubAppHandler.handleInstallGitHubApp();
    }

    @UiHandler("repoNameField" )
    public void handleKeyUp(KeyUpEvent event) {
        restartCheck();
    }

    private void restartCheck() {
        repoCoordsChangedTimer.cancel();
        repoCoordsChangedTimer.schedule(REPO_COORDS_CHANGED_DELAY_MILLIS);
    }

    @UiHandler("repoNameField" )
    public void handleKeyUp(KeyDownEvent event) {
        repoCoordsChangedTimer.cancel();
    }

    @Override
    public String getOwner() {
        return ownerNameField.getValue().trim();
    }

    @Override
    public void setOwner(String owner) {
        ownerNameField.setValue(owner.trim());
    }

    @Override
    public String getRepo() {
        return repoNameField.getValue().trim();
    }

    @Override
    public void setRepo(String repo) {
        repoNameField.setValue(repo.trim());
    }

    @Override
    public void setRepoCoordinatesChangedHandler(RepoCoordinatesChangedHandler handler) {
        this.coordsChangedHandler = handler;
    }

    @Override
    public void setInstallGitHubAppHandler(InstallGitHubAppHandler handler) {
        this.installGitHubAppHandler = handler;
    }

    @Override
    public String getReturnToUrl() {
        return Window.Location.getProtocol() + "//"
            + Window.Location.getHost() + Window.Location.getPath()
            + Window.Location.getQueryString() + Window.Location.getHash();
    }

    @Override
    public void setStatusMessage(String message) {
        messageLabel.setText(message);
        statusMessageContainer.setVisible(!message.isEmpty());
    }

    @Override
    public void setInstallButtonVisible(boolean visible) {
        installButtonContainer.setVisible(visible);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Scheduler.get().scheduleDeferred(() -> ownerNameField.setFocus(true));
    }
}