package edu.stanford.bmir.protege.web.client.card;

public interface CardPortletPresenter {

    boolean isEditor();

    void applyEdits();

    void cancelEdits();

    void requestFocus();
}
