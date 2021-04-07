package edu.stanford.bmir.protege.web.shared.merge_add;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;


public class NewOntologyMergeAddResult implements Result {

    private NewOntologyMergeAddResult() {
    }

    public static NewOntologyMergeAddResult create() {
        return new NewOntologyMergeAddResult();
    }
}
