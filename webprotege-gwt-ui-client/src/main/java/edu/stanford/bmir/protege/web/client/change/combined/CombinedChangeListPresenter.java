package edu.stanford.bmir.protege.web.client.change.combined;

import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.download.ProjectRevisionDownloader;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
import edu.stanford.bmir.protege.web.shared.change.*;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
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
        GetProjectChangesAction action = GetProjectChangesAction.create(projectId, Optional.empty(), pageRequest);
        lastAction = Optional.of(action);
        dispatch.execute(action,
                hasBusy,
                (projChangeResult) -> {
                    GetLinearizationChangesAction linAction = GetLinearizationChangesAction.create(projectId, Optional.empty(), pageRequest);
                    dispatch.execute(linAction, hasBusy, (linChangeResult) -> {
                        fillView(projChangeResult, linChangeResult);
                    });
                });
    }

    public void displayChangesForEntity(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        this.pageNumberChangedHandler = pageNumber -> displayChangesForEntity(entity);
        view.clear();
        PageRequest pageRequest = PageRequest.requestPage(view.getPageNumber());
        GetProjectChangesAction action = GetProjectChangesAction.create(projectId, Optional.of(entity), pageRequest);
        dispatch.execute(action,
                hasBusy,
                (projChangeResult) -> {
                    GetLinearizationChangesAction linAction = GetLinearizationChangesAction.create(projectId, Optional.empty(), pageRequest);
                    dispatch.execute(linAction, hasBusy, (linChangeResult) -> {
                        fillView(projChangeResult, linChangeResult);
                    });
                });
    }

//    public void displayChangesForWatches(@Nonnull UserId userId) {
//        checkNotNull(userId);
//        this.pageNumberChangedHandler = pageNumber -> displayChangesForWatches(userId);
//        view.clear();
//        GetWatchedEntityChangesAction action = GetWatchedEntityChangesAction.create(projectId, userId);
//        dispatch.execute(action,
//                hasBusy,
//                this::fillView);
//    }

    public void clear() {
        view.clear();
    }

    private void fillView(HasProjectChanges projChangeResult, HasProjectChanges linChangeResult) {
        Page<ProjectChange> projChangesPage = projChangeResult.getProjectChanges();
        Page<ProjectChange> linChangesPage = linChangeResult.getProjectChanges();
        List<LinearizationChange> linChanges = new ArrayList<>();
        List<OntologyChange> ontologyChanges = new ArrayList<>();
        linChangesPage.getPageElements()
                .forEach(linChange -> linChanges.add(LinearizationChange.createChange(linChange)));
        projChangesPage.getPageElements()
                .forEach(ontChange -> ontologyChanges.add(OntologyChange.createChange(ontChange)));
        List<GeneralProjectChange> allChanges = new ArrayList<>(linChanges);
        allChanges.addAll(ontologyChanges);
        Page<GeneralProjectChange> totalProjectChanges = Page.create(
                projChangesPage.getPageNumber(),
                projChangesPage.getPageCount() + linChangesPage.getPageCount(),
                allChanges,
                allChanges.size()
        );

        view.clear();
        insertChangesIntoView(totalProjectChanges);
/*
    ToDo:
        add the permision checks when we have a working history.
 */
//        permissionChecker.hasPermission(VIEW_CHANGES,
//                viewChanges -> {
//                    if (viewChanges) {
//                        insertChangesIntoView(mergePage);
//                    }
//                });
    }

    private void insertChangesIntoView(Page<GeneralProjectChange> changes) {
        List<GeneralProjectChange> generalProjectChanges = new ArrayList<>(changes.getPageElements());
        generalProjectChanges.sort(Comparator.comparingLong(change -> change.getChange().getTimestamp()));
        Collections.reverse(generalProjectChanges);
        long previousTimeStamp = 0;
        view.setPageCount(changes.getPageCount());
        view.setPageNumber(changes.getPageNumber());
        for (final GeneralProjectChange generalProjectChange : generalProjectChanges) {
            long changeTimeStamp = generalProjectChange.getChange().getTimestamp();
            if (!TimeUtil.isSameCalendarDay(previousTimeStamp, changeTimeStamp)) {
                previousTimeStamp = changeTimeStamp;
                Date date = new Date(changeTimeStamp);
                view.addSeparator("\u25C9   " + messages.change_changesOn() + " " + DateTimeFormat
                        .getFormat("EEE, d MMM yyyy")
                        .format(date));
            }

            CombinedChangeDetailsView view = new CombinedChangeDetailsViewImpl();

            view.setAuthor(generalProjectChange.getChange().getAuthor());
            view.setHighLevelDescription(generalProjectChange.getChange().getSummary());
            view.setRevertRevisionVisible(false);


            if (generalProjectChange instanceof OntologyChange) {
                view.setRevision(generalProjectChange.getChange().getRevisionNumber());
                view.setRevertRevisionHandler(revisionNumber -> CombinedChangeListPresenter.this.handleRevertRevision(
                        generalProjectChange.getChange()));
                view.setDownloadRevisionHandler(revisionNumber -> {
                    ProjectRevisionDownloader downloader = new ProjectRevisionDownloader(
                            projectId,
                            revisionNumber,
                            DownloadFormatExtension.owl);
                    downloader.download();
                });
                view.setDownloadRevisionVisible(downloadVisible);
//                if (revertChangesVisible) {
//                    permissionChecker.hasPermission(REVERT_CHANGES,
//                            view::setRevertRevisionVisible);
//                }
            } else {
                view.setRevision(null);
                view.setRevertRevisionVisible(false);
            }

            Page<DiffElement<String, String>> page = generalProjectChange.getChange().getDiff();
            List<DiffElement<String, String>> pageElements = page.getPageElements();
            view.setDiff(pageElements, (int) page.getTotalElements());
            view.setChangeCount(generalProjectChange.getChange().getChangeCount());
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
