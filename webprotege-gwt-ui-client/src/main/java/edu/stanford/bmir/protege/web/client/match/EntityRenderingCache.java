package edu.stanford.bmir.protege.web.client.match;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@ApplicationSingleton
public class EntityRenderingCache {

    private final DispatchServiceManager dispatch;
    private final DispatchErrorMessageDisplay errorDisplay;

    private final Cache<CacheKey, GetEntityRenderingResult> cache = CacheBuilder.newBuilder()
            .maximumSize(500)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    private final List<PendingRequest> pendingRequests = new ArrayList<>();

    @Inject
    public EntityRenderingCache(@Nonnull DispatchServiceManager dispatch,
                                @Nonnull DispatchErrorMessageDisplay errorDisplay) {
        this.dispatch = dispatch;
        this.errorDisplay = errorDisplay;
    }

    public void load(@Nonnull ProjectId projectId,
                     @Nonnull OWLEntity entity,
                     @Nonnull Consumer<GetEntityRenderingResult> callback) {
        CacheKey key = new CacheKey(projectId, entity.getIRI());
        GetEntityRenderingResult cached = cache.getIfPresent(key);
        if (cached != null) {
            callback.accept(cached);
            return;
        }

        PendingRequest pending = findOrCreatePending(key);
        pending.callbacks.add(callback);
        if (!pending.loading) {
            pending.loading = true;
            dispatch.execute(GetEntityRenderingAction.create(projectId, entity),
                    new DispatchServiceCallback<GetEntityRenderingResult>(errorDisplay) {
                        @Override
                        public void handleSuccess(GetEntityRenderingResult result) {
                            cache.put(key, result);
                            pending.loading = false;
                            for (Consumer<GetEntityRenderingResult> cb : pending.callbacks) {
                                cb.accept(result);
                            }
                            removePending(pending);
                        }

                        @Override
                        public void handleFinally() {
                            pending.loading = false;
                            if (cache.getIfPresent(key) == null) {
                                removePending(pending);
                            }
                        }
                    });
        }
    }

    private synchronized void removePending(PendingRequest pending) {
        pendingRequests.remove(pending);
    }

    private synchronized PendingRequest findOrCreatePending(CacheKey key) {
        for (PendingRequest p : pendingRequests) {
            if (p.key.equals(key)) {
                return p;
            }
        }
        PendingRequest p = new PendingRequest(key);
        pendingRequests.add(p);
        return p;
    }

    private static class CacheKey {
        final ProjectId projectId;
        final IRI entity;

        CacheKey(ProjectId projectId, IRI entity) {
            this.projectId = projectId;
            this.entity = entity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return projectId.equals(cacheKey.projectId) && entity.equals(cacheKey.entity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(projectId, entity);
        }
    }

    private static class PendingRequest {
        final CacheKey key;
        final List<Consumer<GetEntityRenderingResult>> callbacks = new ArrayList<>();
        boolean loading = false;

        PendingRequest(CacheKey key) {
            this.key = key;
        }
    }
}
