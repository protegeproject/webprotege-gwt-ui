package edu.stanford.bmir.protege.web.client.change.combined;

import com.google.common.collect.Ordering;
import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.change.ChangeDetailsView;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
import edu.stanford.bmir.protege.web.shared.change.*;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 26/02/15
 */
public class CombinedChangeListPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChangeListView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final Messages messages;

    private boolean revertChangesVisible = false;

    private boolean downloadVisible = false;

    private HasBusy hasBusy = busy -> {
    };

    private Optional<GetProjectChangesAction> lastAction = Optional.empty();

    private HasPagination.PageNumberChangedHandler pageNumberChangedHandler = pageNumber -> {
    };

    @Nonnull
    private MessageBox messageBox;

    private EntityDisplay entityDisplay;

    @Inject
    public CombinedChangeListPresenter(@Nonnull ProjectId projectId,
                                       @Nonnull ChangeListView view,
                                       @Nonnull DispatchServiceManager dispatch,
                                       @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                       @Nonnull Messages messages,
                                       @Nonnull MessageBox messageBox) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.dispatch = checkNotNull(dispatch);
        this.messages = checkNotNull(messages);
        this.messageBox = checkNotNull(messageBox);
        this.view.setPageNumberChangedHandler(pageNumber -> pageNumberChangedHandler.handlePageNumberChanged(pageNumber));
    }

    public void setRevertChangesVisible(boolean revertChangesVisible) {
        this.revertChangesVisible = revertChangesVisible;
    }

    public void setDownloadVisible(boolean downloadVisible) {
        this.downloadVisible = downloadVisible;
    }

    @Nonnull
    public ChangeListView getView() {
        return view;
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    public void displayChangesForProject() {
        this.pageNumberChangedHandler = pageNumber -> displayChangesForProject();
        view.clear();
        PageRequest pageRequest = PageRequest.requestPage(view.getPageNumber());

        GetProjectChangesForHistoryViewAction projectHistoryAction = GetProjectChangesForHistoryViewAction.create(projectId, Optional.empty(), pageRequest);
        dispatch.execute(projectHistoryAction, hasBusy, this::fillView);
    }

    public void displayChangesForEntity(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        this.pageNumberChangedHandler = pageNumber -> displayChangesForEntity(entity);
        view.clear();
        PageRequest pageRequest = PageRequest.requestPage(view.getPageNumber());

        GetProjectChangesForHistoryViewAction projectHistoryAction = GetProjectChangesForHistoryViewAction.create(projectId, Optional.of(entity), pageRequest);
        dispatch.execute(projectHistoryAction, hasBusy, this::fillView);
    }

    public void clear() {
        view.clear();
    }

    private void fillView(HasProjectChanges result) {
        Page<ProjectChange> changes = result.getProjectChanges();
        view.clear();
        permissionChecker.hasPermission(VIEW_CHANGES,
                viewChanges -> {
                    if (viewChanges) {
                        insertChangesIntoView(changes);
                    }
                });
    }

    private void insertChangesIntoView(Page<ProjectChange> changes) {
        List<ProjectChange> projectChanges = new ArrayList<>(changes.getPageElements());
        projectChanges.sort(Ordering.compound(Collections.singletonList(
                Ordering.from(new ProjectChangeTimestampComparator()).reverse())));
        long previousTimeStamp = 0;
        view.setPageCount(changes.getPageCount());
        view.setPageNumber(changes.getPageNumber());
        for (final ProjectChange projectChange : projectChanges) {
            long changeTimeStamp = projectChange.getTimestamp();
            if (!TimeUtil.isSameCalendarDay(previousTimeStamp, changeTimeStamp)) {
                previousTimeStamp = changeTimeStamp;
                Date date = new Date(changeTimeStamp);
                view.addSeparator("\u25C9   " + messages.change_changesOn() + " " + DateTimeFormat
                        .getFormat("EEE, d MMM yyyy")
                        .format(date));
            }

            ChangeDetailsView view = new CombinedChangeDetailsViewImpl();
            view.setRevision(null);
            view.setAuthor(projectChange.getAuthor());
            view.setHighLevelDescription(projectChange.getSummary());
            view.setRevertRevisionVisible(false);
            view.setDownloadRevisionVisible(false);
            Page<DiffElement<String, String>> page = projectChange.getDiff();
            List<DiffElement<String, String>> pageElements = page.getPageElements();
            view.setDiff(pageElements, (int) page.getTotalElements());
            view.setChangeCount(projectChange.getChangeCount());
            view.setTimestamp(changeTimeStamp);
            this.view.addChangeDetailsView(view);
        }
    }

    private void handleRevertRevision(final ProjectChange projectChange) {
        startRevertChangesWorkflow(projectChange);
    }

    private void startRevertChangesWorkflow(final ProjectChange projectChange) {
        String subMessage = messages.change_revertChangesInRevisionQuestion();
        messageBox.showConfirmBox(
                messages.change_revertChangesQuestion(),
                subMessage,
                CANCEL,
                DialogButton.get(messages.change_revert()),
                () -> revertChanges(projectChange),
                CANCEL);
    }

    private void revertChanges(ProjectChange projectChange) {
        final RevisionNumber revisionNumber = projectChange.getRevisionNumber();
        dispatch.execute(RevertRevisionAction.create(projectId, revisionNumber),
                this::handleChangedReverted);
    }

    private void handleChangedReverted(@Nonnull RevertRevisionResult result) {
        RevisionNumber revisionNumber = result.getRevisionNumber();
        messageBox.showMessage(messages.change_revertChangesInRevisionSuccessful(revisionNumber.getValue()));
        lastAction.ifPresent(action -> displayChangesForProject());
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }
}
