package edu.stanford.bmir.protege.web.client.searchIcd;


import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.inject.Inject;

@ProjectSingleton
public class SearchInputManagerImpl implements SearchInputManager {

    private String searchInputText = "";

    @Inject
    public SearchInputManagerImpl() {

    }


    public void setSearchInputText(String text) {
        if (checkStringHasValue(text)) {
            searchInputText = text;
        }
    }

    public String getSearchInputText() {
        if (checkStringHasValue(searchInputText)) {
            return searchInputText;
        }

        return "";
    }


    private boolean checkStringHasValue(String text) {
        return text != null && !text.isEmpty();
    }
}
