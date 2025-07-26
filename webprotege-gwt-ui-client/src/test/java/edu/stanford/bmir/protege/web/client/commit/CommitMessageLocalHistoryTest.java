package edu.stanford.bmir.protege.web.client.commit;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CommitMessageLocalHistoryTest {

    @Test
    public void shouldCreateEmptyHistory() {
        CommitMessageLocalHistory history = CommitMessageLocalHistory.empty();
        assertTrue(history.getMessages().isEmpty());
    }

    @Test
    public void shouldInitializeFromMessages() {
        List<String> initialMessages = new ArrayList<>();
        initialMessages.add("Initial commit" );
        initialMessages.add("Add README" );
        CommitMessageLocalHistory history = CommitMessageLocalHistory.of(initialMessages);
        assertEquals(initialMessages, history.getMessages());
    }

    @Test
    public void shouldRemoveDuplicatesAndRespectOrderInSetMessages() {
        CommitMessageLocalHistory history = CommitMessageLocalHistory.empty();
        List<String> messages = new ArrayList<>();
        messages.add("Fix bug" );
        messages.add("Fix bug" );
        messages.add("Refactor" );
        messages.add("Fix bug" );
        history.setMessages(messages);
        List<String> expected = new ArrayList<>();
        expected.add("Fix bug" );
        expected.add("Refactor" );
        assertEquals(expected, history.getMessages());
    }

    @Test
    public void shouldLimitMessagesToMaxSize() {
        CommitMessageLocalHistory history = CommitMessageLocalHistory.empty();
        List<String> bigList = generateMessages(CommitMessageLocalHistory.DEFAULT_MAX_SIZE + 5);
        history.setMessages(bigList);
        assertEquals(CommitMessageLocalHistory.DEFAULT_MAX_SIZE, history.getMessages().size());
        assertEquals("msg-0", history.getMessages().get(0));
    }

    @Test
    public void shouldPrependNewMessageOnPush() {
        List<String> messages = new ArrayList<>();
        messages.add("Commit A" );
        messages.add("Commit B" );
        CommitMessageLocalHistory history = CommitMessageLocalHistory.of(messages);
        history.pushMessage("Commit C");
        List<String> expected = new ArrayList<>();
        expected.add("Commit C" );
        expected.add("Commit A" );
        expected.add("Commit B" );
        assertEquals(expected, history.getMessages());
    }

    @Test
    public void shouldAvoidDuplicateOnPush() {
        List<String> messages = new ArrayList<>();
        messages.add("Commit A" );
        messages.add("Commit B" );
        CommitMessageLocalHistory history = CommitMessageLocalHistory.of(messages);
        history.pushMessage("Commit A");
        List<String> expected = new ArrayList<>();
        expected.add("Commit A" );
        expected.add("Commit B" );
        assertEquals(expected, history.getMessages());
    }

    @Test
    public void getMessagesShouldReturnDefensiveCopy() {
        List<String> messages = new ArrayList<>();
        messages.add("Hello" );
        CommitMessageLocalHistory history = CommitMessageLocalHistory.of(messages);
        List<String> copy = history.getMessages();
        copy.add("This should not be added");
        assertEquals(1, history.getMessages().size());
        assertEquals("Hello", history.getMessages().get(0));
    }

    private List<String> generateMessages(int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String s = "msg-" + i;
            list.add(s);
        }
        return list;
    }
}