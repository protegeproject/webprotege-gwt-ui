package edu.stanford.bmir.protege.web.client.commit;

import java.util.ArrayList;
import java.util.List;

public class CommitMessageLocalHistory {

    public static final int DEFAULT_MAX_SIZE = 20;

    private final List<String> messages = new ArrayList<>();

    private int maxSize = DEFAULT_MAX_SIZE;

    public static CommitMessageLocalHistory empty() {
        return new CommitMessageLocalHistory();
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public static CommitMessageLocalHistory of(List<String> messages) {
        CommitMessageLocalHistory history = new CommitMessageLocalHistory();
        history.setMessages(messages);
        return history;
    }

    public void setMessages(List<String> messages) {
        this.messages.clear();
        messages.stream()
                .distinct()
                .limit(maxSize)
                .forEach(this.messages::add);
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public void pushMessage(String message) {
        List<String> next = new ArrayList<>(messages);
        next.add(0, message);
        setMessages(next);
    }
}
