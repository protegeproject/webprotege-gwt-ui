package edu.stanford.bmir.protege.web.client.change.combined;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.change.*;
import edu.stanford.bmir.protege.web.client.diff.DiffView;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.library.timelabel.ElapsedTimeLabel;
import edu.stanford.bmir.protege.web.client.user.UserIcon;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.ui.NumberFormatter.format;


public class CombinedChangeDetailsViewImpl extends Composite implements CombinedChangeDetailsView {

    private static Messages messages = GWT.create(Messages.class);

    private DownloadRevisionHandler downloadRevisionHandler = revisionNumber -> {
    };

    private boolean downloadRevisionVisible = false;

    private boolean revertRevisionVisible = false;

    private Optional<RevisionNumber> revision = Optional.empty();

    interface ChangeDetailsViewImplUiBinder extends UiBinder<HTMLPanel, CombinedChangeDetailsViewImpl> {
    }

    private static ChangeDetailsViewImplUiBinder ourUiBinder = GWT.create(ChangeDetailsViewImplUiBinder.class);

    public CombinedChangeDetailsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        revisionField.setVisible(false);
        tooManyChangesMessage.setVisible(true);
        copyIriButton.setVisible(false);
        copyIriButton.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        copyIriButton.addDomHandler(event -> copyIriToClipboard(), com.google.gwt.event.dom.client.ClickEvent.getType());
    }

    @UiField
    protected SimplePanel authorUserIcon;

    @UiField
    protected HTML subjectsField;

    @UiField
    protected Label revisionField;

    @UiField
    protected InlineHTML highLevelDescriptionField;

    @UiField
    protected HasText authorField;

    @UiField
    protected ElapsedTimeLabel timestampField;

    @UiField
    protected DiffView diffView;

    @UiField
    protected HasText changeCountField;

    @UiField
    protected Label tooManyChangesMessage;

    @UiField
    protected Label hiddenChangesCount;

    @UiField
    protected HTMLPanel copyIriButton;

    private String entityIri;

    private RevertRevisionHandler revertRevisionHandler = revisionNumber -> {
    };

    @UiHandler("revisionField")
    protected void showRevisionMenu(ClickEvent event) {
        if (!downloadRevisionVisible && !revertRevisionVisible) {
            return;
        }
        if (!revision.isPresent()) {
            return;
        }
        PopupMenu popupMenu = new PopupMenu();
        if (revertRevisionVisible) {
            popupMenu.addItem(new AbstractUiAction(messages.change_revertChangesInRevision() + " " + revision.get().getValue()) {
                @Override
                public void execute() {
                    revertRevisionHandler.handleRevertRevision(revision.get());
                }
            });
        }
        if (downloadRevisionVisible && revertRevisionVisible) {
            popupMenu.addSeparator();
        }
        if (downloadRevisionVisible) {
            popupMenu.addItem(new AbstractUiAction(messages.change_downloadRevision() + " " + revision.get().getValue()) {
                @Override
                public void execute() {
                    downloadRevisionHandler.handleDownloadRevision(revision.get());
                }
            });
        }

        popupMenu.showRelativeTo(revisionField);
    }

    @Override
    public void setSubjects(Collection<OWLEntityData> subjects) {
        StringBuilder sb = new StringBuilder();
        for (OWLEntityData entityData : subjects) {
            sb.append("<span style=\"padding-left: 5px;\">");
            sb.append(entityData.getBrowserText());
            sb.append(",");
            sb.append("</span>");
        }
        subjectsField.setHTML(sb.toString().trim());
    }

    private void updateRevisionButtonState() {
        String suffix;
        if (revertRevisionVisible || downloadRevisionVisible) {
            suffix = " \u25be";
            revisionField.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        } else {
            suffix = "";
            revisionField.getElement().getStyle().setCursor(Style.Cursor.DEFAULT);
        }
        if (revision.isPresent()) {
            revisionField.setVisible(true);
            RevisionNumber num = revision.get();
            revisionField.setText("R " + Integer.toString(num.getValueAsInt()) + suffix);
        }
    }


    @Override
    public void setRevertRevisionVisible(boolean visible) {
        revertRevisionVisible = visible;
        updateRevisionButtonState();
    }

    public void setRevertRevisionHandler(RevertRevisionHandler revertRevisionHandler) {
        this.revertRevisionHandler = checkNotNull(revertRevisionHandler);
    }

    @Override
    public void setDownloadRevisionVisible(boolean visible) {
        this.downloadRevisionVisible = visible;
        updateRevisionButtonState();
    }

    @Override
    public void setDownloadRevisionHandler(DownloadRevisionHandler downloadRevisionHandler) {
        this.downloadRevisionHandler = downloadRevisionHandler;
    }


    @Override
    public void setRevision(RevisionNumber revision) {
        this.revision = Optional.ofNullable(revision);
        updateRevisionButtonState();
    }

    @Override
    public void setHighLevelDescription(String description) {
        highLevelDescriptionField.setText(checkNotNull(description));
        highLevelDescriptionField.setHTML(description);
        // Extract IRI from description HTML (look for entity links)
        extractIriFromDescription(description);
    }

    private void extractIriFromDescription(String htmlDescription) {
        // Try to extract IRI from HTML links or markdown links
        // Pattern: <a href="iri"> or [text](iri)
        String iri = extractFirstIriFromHtml(htmlDescription);
        if (iri != null && !iri.isEmpty()) {
            this.entityIri = iri;
            copyIriButton.setVisible(true);
        } else {
            copyIriButton.setVisible(false);
        }
    }

    private native String extractFirstIriFromHtml(String html) /*-{
        // Try to find IRI in HTML links
        var tempDiv = $doc.createElement('div');
        tempDiv.innerHTML = html;
        var links = tempDiv.getElementsByTagName('a');
        if (links.length > 0) {
            var href = links[0].getAttribute('href');
            if (href && (href.startsWith('http://') || href.startsWith('https://') || href.startsWith('urn:'))) {
                return href;
            }
        }
        // Try to find IRI in onclick attribute: window.focusClickedEntity(event, 'IRI')
        var onclickPattern = /window\.focusClickedEntity\s*\([^,]+,\s*['"]([^'"]+)['"]\)/g;
        var onclickMatch = onclickPattern.exec(html);
        if (onclickMatch && onclickMatch[1]) {
            var iri = onclickMatch[1];
            if (iri.startsWith('http://') || iri.startsWith('https://') || iri.startsWith('urn:')) {
                return iri;
            }
        }
        // Try to find IRI in title attribute: Click to select entity IRI
        var titlePattern = /title\s*=\s*["']Click to select entity\s+([^"']+)["']/gi;
        var titleMatch = titlePattern.exec(html);
        if (titleMatch && titleMatch[1]) {
            var iri = titleMatch[1];
            if (iri.startsWith('http://') || iri.startsWith('https://') || iri.startsWith('urn:')) {
                return iri;
            }
        }
        // Try to find IRI in markdown links pattern [text](iri)
        var markdownLinkPattern = /\[([^\]]+)\]\(([^)]+)\)/g;
        var match = markdownLinkPattern.exec(html);
        if (match && match[2]) {
            var iri = match[2];
            if (iri.startsWith('http://') || iri.startsWith('https://') || iri.startsWith('urn:')) {
                return iri;
            }
        }
        return null;
    }-*/;

    private native void copyIriToClipboard() /*-{
        var self = this;
        var iri = self.@edu.stanford.bmir.protege.web.client.change.combined.CombinedChangeDetailsViewImpl::entityIri;
        if (!iri) {
            return;
        }
        // Use fallback method which works in all browsers
        self.@edu.stanford.bmir.protege.web.client.change.combined.CombinedChangeDetailsViewImpl::copyToClipboardFallback(Ljava/lang/String;)(iri);
    }-*/;

    private native void copyToClipboardFallback(String text) /*-{
        // Fallback method using temporary textarea
        var textArea = $doc.createElement("textarea");
        textArea.value = text;
        textArea.style.position = "fixed";
        textArea.style.left = "-999999px";
        textArea.style.top = "-999999px";
        $doc.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        try {
            var successful = $doc.execCommand('copy');
            if (!successful) {
                console.log('Fallback: Copy command was unsuccessful');
            }
        } catch (err) {
            console.log('Fallback: Unable to copy', err);
        }
        $doc.body.removeChild(textArea);
    }-*/;

    @Override
    public void setAuthor(UserId author) {
        authorUserIcon.setWidget(UserIcon.get(author));
        authorField.setText(checkNotNull(author).getUserName());
    }

    @Override
    public void setDiff(List<DiffElement<String, String>> diff, int totalChanges) {
        diffView.setDiff(diff,
                lineElement -> new SafeHtmlBuilder().appendHtmlConstant(lineElement).toSafeHtml(),
                document -> new SafeHtmlBuilder().appendHtmlConstant(document).toSafeHtml());
        if (diff.size() < totalChanges) {
            tooManyChangesMessage.setText(messages.showing() + " " + format(diff.size()) + " " + messages.pagination_of() + " " + format(totalChanges) + " " + messages.change_changes());
            tooManyChangesMessage.setVisible(true);
            int hiddenChanges = totalChanges - diff.size();
            hiddenChangesCount.setText(messages.plus() + " " + format(hiddenChanges) + " " + messages.change_moreChangesNotShownHere());
            hiddenChangesCount.setVisible(true);
        } else {
            tooManyChangesMessage.setVisible(false);
            hiddenChangesCount.setVisible(false);
        }
    }

    @Override
    public void setDetailsVisible(boolean detailsVisible) {
        diffView.asWidget().setVisible(detailsVisible);
        subjectsField.setVisible(detailsVisible);
    }

    @Override
    public void setChangeCount(int changeCount) {
        changeCountField.setText(Integer.toString(changeCount));
    }

    @Override
    public void setTimestamp(long timestamp) {
        timestampField.setBaseTime(timestamp);
    }


}