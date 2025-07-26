package edu.stanford.bmir.protege.web.client.commit;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.storage.client.Storage;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ProjectSingleton
public class CommitMessageLocalHistoryStorage {

    private static final Logger logger = Logger.getLogger(CommitMessageLocalHistoryStorage.class.getName());

    private static final String STORAGE_KEY = "webprotege.commit-history";

    @Inject
    public CommitMessageLocalHistoryStorage() {
    }

    private static void logLocalStorageNotAvailable() {
        logger.warning("No local history storage available" );
    }

    public void saveLocalHistory(CommitMessageLocalHistory localHistory) {
        Storage localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage == null) {
            logLocalStorageNotAvailable();
            return;
        }
        List<String> messages = localHistory.getMessages();
        JSONArray array = new JSONArray();
        for (int i = 0; i < messages.size(); i++) {
            array.set(i, new JSONString(messages.get(i)));
        }
        localStorage.setItem(STORAGE_KEY, array.toString());
    }

    public CommitMessageLocalHistory loadLocalHistory() {
        List<String> result = new ArrayList<>();
        Storage localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage == null) {
            logLocalStorageNotAvailable();
            return CommitMessageLocalHistory.empty();
        }
        String json = localStorage.getItem(STORAGE_KEY);
        if (json == null) {
            return CommitMessageLocalHistory.empty();
        }
        List<String> messages = new ArrayList<>();
        try {
            JSONValue parsed = JSONParser.parseStrict(json);
            JSONArray array = parsed.isArray();
            if (array != null) {
                for (int i = 0; i < array.size(); i++) {
                    JSONString str = array.get(i).isString();
                    if (str != null) {
                        messages.add(str.stringValue());
                    }
                }
            }
        } catch (Exception e) {
            logger.warning("Failed to parse commit history from local storage: " + e.getMessage());
        }
        return CommitMessageLocalHistory.of(messages);
    }

}
