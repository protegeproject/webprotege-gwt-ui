package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.PerformEntitySearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DuplicateEntitiesUtil {

    private final static Logger logger = Logger.getLogger(DuplicateEntitiesUtil.class.getName());


    public static Page<EntitySearchResult> concatenateDuplicateSearchResults(List<PerformEntitySearchResult> resultList) {
        int pageCount;
        int pageSize = 10;
        List<EntitySearchResult> finalResultList = new ArrayList<>();
        for (PerformEntitySearchResult searchResult : resultList) {
            pageSize = searchResult.getResults().getPageSize();
            finalResultList.addAll(searchResult.getResults().getPageElements());
        }

        finalResultList.forEach(result -> {
            logger.info(result.getEntity().getBrowserText());
        });
        pageCount = calculatePageCount(finalResultList.size(), pageSize);

        logger.info("here is page count:" + pageCount);
        logger.info("here is finalResultList size:" + finalResultList.size());

        return Page.create(1, pageCount, finalResultList, finalResultList.size());
    }

    private static int calculatePageCount(int totalElements, int pageSize) {
        return (totalElements + pageSize - 1) / pageSize;
    }
}
