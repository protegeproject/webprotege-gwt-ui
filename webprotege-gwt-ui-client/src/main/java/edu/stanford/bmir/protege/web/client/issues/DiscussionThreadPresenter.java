package edu.stanford.bmir.protege.web.client.issues;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectCapabilityChecker;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.HandlerRegistrationManager;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInCapability.*;
import static edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentAction.addComment;
import static edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent.ON_COMMENT_POSTED;
import static edu.stanford.bmir.protege.web.shared.issues.CommentUpdatedEvent.ON_COMMENT_UPDATED;
import static edu.stanford.bmir.protege.web.shared.issues.DeleteEntityCommentAction.deleteComment;
import static edu.stanford.bmir.protege.web.shared.issues.EditCommentAction.editComment;
import static edu.stanford.bmir.protege.web.shared.issues.SetDiscussionThreadStatusAction.setDiscussionThreadStatus;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
public class DiscussionThreadPresenter implements HasDispose {

    @Nonnull
    private final DiscussionThreadView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final HandlerRegistrationManager eventBus;

    @Nonnull
    private final LoggedInUserProjectCapabilityChecker capabilityChecker;

    @Nonnull
    private final LoggedInUserProvider loggedInUserProvider;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CommentViewFactory commentViewFactory;

    @Nonnull
    private final CommentEditorModal commentEditorModal;

    @Nonnull
    private final MessageBox messageBox;

    private final Map<CommentId, CommentView> commentViewMap = new HashMap<>();


    private Optional<ThreadId> currentThreadId = Optional.empty();


    @Inject
    public DiscussionThreadPresenter(@Nonnull DiscussionThreadView view,
                                     @Nonnull Messages messages,
                                     @Nonnull ProjectId projectId,
                                     @Nonnull DispatchServiceManager dispatch,
                                     @Nonnull HandlerRegistrationManager eventBus,
                                     @Nonnull LoggedInUserProjectCapabilityChecker capabilityChecker,
                                     @Nonnull LoggedInUserProvider loggedInUserProvider,
                                     @Nonnull CommentViewFactory commentViewFactory,
                                     @Nonnull MessageBox messageBox,
                                     @Nonnull CommentEditorModal commentEditorModal) {
        this.view = checkNotNull(view);
        this.messages = checkNotNull(messages);
        this.projectId = checkNotNull(projectId);
        this.dispatch = checkNotNull(dispatch);
        this.eventBus = eventBus;
        this.capabilityChecker = capabilityChecker;
        this.loggedInUserProvider = loggedInUserProvider;
        this.commentViewFactory = checkNotNull(commentViewFactory);
        this.commentEditorModal = commentEditorModal;
        this.messageBox = checkNotNull(messageBox);
    }

    @Nonnull
    public DiscussionThreadView getView() {
        return view;
    }

    public void start() {
        eventBus.registerHandlerToProject(projectId,
                                          ON_PERMISSIONS_CHANGED,
                                          event -> updateDisplayedViews());
        eventBus.registerHandlerToProject(projectId,
                                          ON_COMMENT_UPDATED,
                                          event -> updateComment(event.getComment()));
        eventBus.registerHandlerToProject(projectId,
                                          ON_COMMENT_POSTED,
                                          event -> handleCommentAdded(event.getThreadId(), event.getComment()));
    }

    private void updateDisplayedViews() {
        commentViewMap.forEach((commentId, view) -> updateCommentView(view));
    }

    private void updateCommentView(CommentView commentView) {
        final boolean userIsCommentCreator = commentView
                .getCreatedBy()
                .equals(Optional.of(loggedInUserProvider.getCurrentUserId()));
        if (userIsCommentCreator) {
            capabilityChecker.hasCapability(EDIT_OWN_OBJECT_COMMENT, commentView::setEditButtonVisible);
        }
        else {
            capabilityChecker.hasCapability(EDIT_ANY_OBJECT_COMMENT, commentView::setEditButtonVisible);
        }
        commentView.setReplyButtonVisible(false);
        capabilityChecker.hasCapability(CREATE_OBJECT_COMMENT, commentView::setReplyButtonVisible);
    }


    @Override
    public void dispose() {
        eventBus.removeHandlers();
    }

    /**
     * Sets the discussion thread to be presented
     *
     * @param thread The thread.
     */
    public void setDiscussionThread(@Nonnull EntityDiscussionThread thread) {
        currentThreadId = Optional.of(checkNotNull(thread).getId());
        view.clear();
        commentViewMap.clear();
        view.setStatus(thread.getStatus());
        capabilityChecker.hasCapability(SET_OBJECT_COMMENT_STATUS, canSetStatus -> {
            view.setStatusVisible(canSetStatus);
            view.setStatusChangedHandler(() -> handleToggleStatus(thread.getId()));
        });

        for (Comment comment : thread.getComments()) {
            addCommentView(thread.getId(), comment);
        }
    }

    private void addCommentView(@Nonnull ThreadId threadId, Comment comment) {
        CommentView commentView = createCommentView(threadId, comment);
        updateCommentView(commentView);
        view.addCommentView(commentView);
        commentViewMap.put(comment.getId(), commentView);
    }

    private CommentView createCommentView(ThreadId threadId, Comment comment) {
        return commentViewFactory.createAndInitView(
                comment,
                () -> handleReplyToComment(threadId),
                () -> handleEditComment(threadId, comment),
                () -> handleDeleteComment(comment)
        );
    }

    private void handleToggleStatus(ThreadId threadId) {
        Status nextStatus = view.getStatus() == Status.OPEN ? Status.CLOSED : Status.OPEN;
        dispatch.execute(
                setDiscussionThreadStatus(projectId, threadId, nextStatus),
                (result) -> view.setStatus(result.getResult())
        );
    }

    private void handleReplyToComment(ThreadId threadId) {
        Consumer<String> handler = body -> {
            dispatch.execute(
                    addComment(projectId, threadId, body),
                    result -> handleCommentAdded(threadId, result.getComment()));
        };
        commentEditorModal.showModal("", handler);
    }

    private void handleEditComment(ThreadId threadId, Comment comment) {
        Consumer<String> handler = body -> dispatch.execute(editComment(projectId, threadId, comment.getId(), body),
                                                            result -> result
                                                                    .getEditedComment()
                                                                    .ifPresent(this::updateComment));
        commentEditorModal.showModal(comment.getBody(), handler);
    }

    private void updateComment(Comment comment) {
        CommentView view = commentViewMap.get(comment.getId());
        if (view != null) {
            view.setUpdatedAt(comment.getUpdatedAt());
            view.setBody(comment.getRenderedBody());
        }
    }

    private void handleDeleteComment(Comment comment) {
        messageBox.showYesNoConfirmBox(messages.deleteCommentConfirmationBoxTitle(),
                                       messages.deleteCommentConfirmationBoxText(),
                                       () -> dispatch.execute(deleteComment(projectId, comment.getId()), result -> {
                                       }));
    }

    private void handleCommentAdded(ThreadId threadId, Comment comment) {
        if (!threadId.equals(currentThreadId.orElse(null))) {
            return;
        }
        if (commentViewMap.containsKey(comment.getId())) {
            return;
        }
        addCommentView(threadId, comment);
    }

}
