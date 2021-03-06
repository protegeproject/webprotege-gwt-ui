package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public interface AbstractCreateEntitiesAction<R extends AbstractCreateEntityResult<E>, E extends OWLEntity> extends ProjectAction<R> {

    @Nonnull
    String getSourceText();

    @Nonnull
    String getLangTag();
}
