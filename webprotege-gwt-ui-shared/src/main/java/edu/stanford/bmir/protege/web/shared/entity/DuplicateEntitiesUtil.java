package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DuplicateEntitiesUtil {

    private final static Logger logger = Logger.getLogger(DuplicateEntitiesUtil.class.getName());


    public static Page<EntitySearchResult> concatenateDuplicateSearchResults(List<PerformEntitySearchResult> resultList){
        int pageCount = 0;
        List<EntitySearchResult> finalResultList = new ArrayList<>();
        for(PerformEntitySearchResult searchResult : resultList){
            pageCount+=searchResult.getResults().getPageCount();
            finalResultList.addAll(searchResult.getResults().getPageElements());
        }

        return Page.create(1,pageCount,finalResultList, finalResultList.size());
    }


}
