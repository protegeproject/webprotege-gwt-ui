package edu.stanford.bmir.protege.web.client.renderer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.client.match.EntityRenderingCache;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2018
 */
public class ClassIriRendererImpl implements ClassIriRenderer {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityRenderingCache entityRenderingCache;

    private final Multimap<IRI, Consumer<OWLClassData>> pending = HashMultimap.create();

    @Inject
    public ClassIriRendererImpl(@Nonnull ProjectId projectId,
                                @Nonnull EntityRenderingCache entityRenderingCache) {
        this.projectId = checkNotNull(projectId);
        this.entityRenderingCache = checkNotNull(entityRenderingCache);
    }

    @Override
    public void renderClassIri(@Nonnull IRI iri,
                               @Nonnull Consumer<OWLClassData> renderingConsumer) {
        if (!pending.get(iri).isEmpty()) {
            pending.put(iri, renderingConsumer);
            return;
        }
        pending.put(iri, renderingConsumer);
        entityRenderingCache.load(projectId, DataFactory.getOWLClass(iri),
                result -> {
                    OWLClassData ed = (OWLClassData) result.getEntityData();
                    pending.removeAll(iri).forEach(consumer -> consumer.accept(ed));
                });
    }
}
