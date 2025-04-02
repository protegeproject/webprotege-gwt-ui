package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.ui.SuggestOracle;
import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestOracle;
import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2013
 */
public class PrimitiveDataEditorSuggestOracle extends SuggestOracle {

    private final Logger logger = Logger.getLogger(PrimitiveDataEditorSuggestOracle.class.getName());

    private EntitySuggestOracle delegate;

    private Set<PrimitiveType> allowedPrimitiveTypes = new LinkedHashSet<>();

//    private FreshEntitySuggestMode mode;

    private FreshEntitySuggestStrategy freshEntityStrategy
            = new NullFreshEntitySuggestStrategy();

    private Request lastRequest = null;

    @Inject
    public PrimitiveDataEditorSuggestOracle(EntitySuggestOracle delegate,
                                            @EntitySuggestOracleSuggestMode FreshEntitySuggestMode mode) {
        this.delegate = delegate;
//        this.mode = mode;
        allowedPrimitiveTypes.add(PrimitiveType.CLASS);
        allowedPrimitiveTypes.add(PrimitiveType.NAMED_INDIVIDUAL);
    }

    public void setCriteria(CompositeRootCriteria entityMatchCriteria) {
        delegate.setCriteria(entityMatchCriteria);
    }

    public void setFreshEntityStrategy(FreshEntitySuggestStrategy strategy) {
        this.freshEntityStrategy = strategy;
    }

    public void setAllowedPrimitiveTypes(Set<PrimitiveType> primitiveTypes) {
        allowedPrimitiveTypes.clear();
        allowedPrimitiveTypes.addAll(primitiveTypes);
        Set<EntityType<?>> entityTypes = getAllowedEntityTypes();
        delegate.setEntityTypes(entityTypes);
    }

    private Set<EntityType<?>> getAllowedEntityTypes() {
        Set<EntityType<?>> entityTypes = Sets.newHashSet();
        for(PrimitiveType primitiveType : allowedPrimitiveTypes) {
            if(primitiveType.isEntityType()) {
                entityTypes.add(primitiveType.getEntityType());
            }
        }
        return entityTypes;
    }

//    public FreshEntitySuggestMode getMode() {
//        return mode;
//    }

//    public void setMode(FreshEntitySuggestMode mode) {
//        this.mode = mode;
//    }

    /**
     * Generate a {@link Response} based on a specific {@link
     * Request}. After the
     * {@link Response} is created, it is passed into
     * {@link Callback#onSuggestionsReady(Request,
     * Response)}.
     * @param request the request
     * @param callback the callback to use for the response
     */
    @Override
    public void requestSuggestions(Request request, final Callback callback) {
        lastRequest = request;
        delegate.requestSuggestions(request, (req, response) -> {
            if(req != lastRequest) {
                logger.info("[PrimitiveDataEditorSuggestOracle] Ignoring stale request: " + request.getQuery());
                return;
            }
            List<EntitySuggestion> suggestions = (List<EntitySuggestion>) response.getSuggestions();
            if (shouldOfferCreateSuggestions(req, suggestions)) {
                suggestions.addAll(freshEntityStrategy.getSuggestions(req.getQuery(),
                                                                      getSuggestedTypesForQuery(req.getQuery())));
                callback.onSuggestionsReady(req, new Response(suggestions));
            }
            else {
                callback.onSuggestionsReady(req, response);
            }

        });
    }

    private List<EntityType<?>> getSuggestedTypesForQuery(String query) {
        if(query.length() == 0) {
            return Lists.newArrayList(getAllowedEntityTypes());
        }
        List<EntityType<?>> result = Lists.newArrayList(getAllowedEntityTypes());
        if(Character.isLowerCase(query.charAt(0))) {
            if(result.remove(EntityType.NAMED_INDIVIDUAL)) {
                result.add(0, EntityType.NAMED_INDIVIDUAL);
            }
        }
        else {
            if(result.remove(EntityType.CLASS)) {
                result.add(0, EntityType.CLASS);
            }
        }
        return result;
    }

    private boolean shouldOfferCreateSuggestions(Request request, List<EntitySuggestion> existingSuggestions) {
        if(freshEntityStrategy.getMode() != FreshEntitySuggestMode.SUGGEST_CREATE_FRESH_ENTITIES) {
            return false;
        }
        if(canParseAsNumericLiteral(request)) {
            return false;
        }
        if(canParseAsStringLiteral(request)) {
            return false;
        }
        for(EntitySuggestion existingSuggestion : existingSuggestions) {
            if(existingSuggestion.getEntity().getBrowserText().equalsIgnoreCase(request.getQuery())) {
                return false;
            }
        }
        return true;
    }

    private boolean canParseAsNumericLiteral(Request request) {
        if(!allowedPrimitiveTypes.contains(PrimitiveType.LITERAL)) {
            return false;
        }
        OWLLiteral lit = DataFactory.parseLiteral(request.getQuery(), Optional.empty());
        return !(lit.getDatatype().isString() || lit.getDatatype().isRDFPlainLiteral());
    }

    private boolean canParseAsStringLiteral(Request request) {
        return allowedPrimitiveTypes.contains(PrimitiveType.LITERAL) && (
                allowedPrimitiveTypes.size() == 1
                || allowedPrimitiveTypes.contains(PrimitiveType.DATA_TYPE) && allowedPrimitiveTypes.size() == 2);
    }

    /**
     * Should {@link Suggestion} display strings be treated as HTML? If true,
     * this
     * all suggestions' display strings will be interpreted as HTML, otherwise as
     * text.
     * @return by default, returns false
     */
    @Override
    public boolean isDisplayStringHTML() {
        return delegate.isDisplayStringHTML();
    }

    public void clearCriteria() {
        delegate.clearCriteria();
    }
}
