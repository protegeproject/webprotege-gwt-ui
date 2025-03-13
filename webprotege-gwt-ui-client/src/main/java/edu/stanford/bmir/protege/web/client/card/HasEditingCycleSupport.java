package edu.stanford.bmir.protege.web.client.card;

public interface HasEditingCycleSupport {

    void beginEditing();

    void cancelEditing();

    void finishEditing(String commitMessage);

    boolean isDirty();
}
